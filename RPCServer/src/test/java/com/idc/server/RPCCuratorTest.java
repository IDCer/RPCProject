package com.idc.server;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class RPCCuratorTest {
    public static final String connect = "127.0.0.1:2181";
    private static CuratorFramework curatorFramework = null;
    private static String nodePath = "/curator1";
    private static String nodeChildPath = "/curator1/n1/n11/n111/n1111";

    @Test
    public void contextLoads() throws Exception {

        //初始化
        init(connect,5000);

        //监听节点数据改变或者子节点变化，只需要订阅一次，便可以一直使用。而原生zookeeper的监听是一次性的，需要重复注册。
        listener(nodePath);

        //新增
        create(nodePath,"n1");
        //递归新增
        createRecursion(nodeChildPath,"n1");

        //查询
        query(nodePath);

        TimeUnit.SECONDS.sleep(2);

        //修改
        update(nodePath,"n11");

        //单个节点删除
//        delete(nodePath);
        //递归删除
        deleteRecursion(nodePath);

    }

    private static void deleteRecursion(String path) throws Exception {
        Void aVoid = curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
        System.out.println("delete:"+"["+path+"],result:"+aVoid);
    }

    private static void delete(String path) throws Exception {
        Void aVoid = curatorFramework.delete().forPath(path);
        System.out.println("delete:"+"["+path+"],result:"+aVoid);

    }

    private static void update(String path, String data) throws Exception {
        Stat stat = curatorFramework.setData().forPath(path, data.getBytes());
        System.out.println("setData:"+"["+path+"],stat:"+stat);

    }

    private static void query(String path) throws Exception {
        byte[] bytes = curatorFramework.getData().forPath(path);
        System.out.println("query:"+"["+path+"],result:"+new String(bytes));

    }

    private static void createRecursion(String path,String data) throws Exception {
        String result = curatorFramework.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data.getBytes());
        System.out.println("create:"+"["+path+"-->"+data+"],result:"+result);

    }

    private static void create(String path, String data) throws Exception {
//        Stat stat = curatorFramework.checkExists().forPath(path);
//        if(null != stat){
//            System.out.println("节点["+path+"]已存在，不能新增");
//            return;
//        }
        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat == null) {
            String result = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data.getBytes());
            System.out.println("create:" + "[" + path + "-->" + data + "],result:" + result);
        } else {
            System.out.println("该结点已存在");
        }
    }

    private static void listener(String path) throws Exception {

        //监听节点内容改变
        final NodeCache nodeCache = new NodeCache(curatorFramework, path);
        nodeCache.start();
        /*nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("节点内容发生变化----->"+nodeCache.getCurrentData());
            }
        });*/

        //使用lambda表达式-jdk1.8以上
        nodeCache.getListenable().addListener(()->{System.out.println("节点内容发生变化----->"+nodeCache.getCurrentData());});


        //监听子节点改变
        final PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, path, true);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
      /*  pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch (pathChildrenCacheEvent.getType() ){
                    case CHILD_ADDED:
                        System.out.println("新增子节点"+pathChildrenCache.getCurrentData());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("更新子节点"+pathChildrenCache.getCurrentData());
                        break;
                    case CHILD_REMOVED:
                        System.out.println("删除子节点"+pathChildrenCache.getCurrentData());
                        break;
                    default:break;
                }
            }
        });*/
        //使用lambda表达式-jdk1.8以上
        pathChildrenCache.getListenable().addListener((curatorFramework,pathChildrenCacheEvent)->
        {switch (pathChildrenCacheEvent.getType() ){
            case CHILD_ADDED:
                System.out.println("新增子节点"+pathChildrenCache.getCurrentData());
                break;
            case CHILD_UPDATED:
                System.out.println("更新子节点"+pathChildrenCache.getCurrentData());
                break;
            case CHILD_REMOVED:
                System.out.println("删除子节点"+pathChildrenCache.getCurrentData());
                break;
            default:break;
        }});


    }

    private static void init(String connect, int sessionTimeout) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);//重试策略，初始等待1s,重试3次
        //通过工厂获得CuratorFramework
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(connect).connectionTimeoutMs(sessionTimeout).retryPolicy(retryPolicy).build();
        curatorFramework.start();//开启连接
        System.out.println("curatorFramework start :" + connect);
    }
}
