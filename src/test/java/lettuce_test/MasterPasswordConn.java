package lettuce_test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

/**
 * @description:
 * @author:
 * @create: 2019-07-24 21:05
 */
public class MasterPasswordConn {

    /**
     * 一主 二从 三哨兵  主有密码 lettuce 连接配置测试
     */

    private static StatefulRedisConnection<String, String> connect;

    private static String REDIS_URI = "redis-sentinel://:jiangBUG@192.168.44.3:26379,192.168.44.4:26380,192.168.44.4:26381/0#mymaster";

    static {
        connect =  RedisClient.create(RedisURI.create(REDIS_URI)).connect();
    }





    public static void main(String[] args) {


        String set = connect.sync().set("test", "hahaha");
        System.out.println(set);


        connect.close();
    }

}
