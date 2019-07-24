package lettuce_test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

/**
 * @description:
 *      * 一主 二从 三哨兵  主有密码 lettuce 连接配置测试
 *      *
 *      *|=================================================================
 *      *|
 *      *| 一主 二从 三哨兵 配置
 *      *| 主  c_01  192.168.44.3:6379
 *      *|
 *      *| 从_1 c_01 192.168.44.3:6380
 *      *| 从_2 c_02 192.168.44.4:6380
 *      *|
 *      *| 哨_1 c_01 192.168.44.3:26379
 *      *| 哨_2 c_02 192.168.44.4:26380
 *      *| 哨_3 c_02 192.168.44.4:26381
 *      *|=================================================================
 *      *| 主库密码 jiangBUG
 *
 * @author:
 * @create: 2019-07-24 21:05
 */
public class MasterPasswordConn {

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
    /**
     * 注 ：
     *
            base.conf 如下

     ☆      protected-mode no
     ☆      port 6379
            tcp-backlog 511
            timeout 0
            tcp-keepalive 300
     ☆      daemonize yes
            supervised no
     ☆      pidfile /var/run/redis_6379.pid
            loglevel notice
     ☆      logfile ""
            databases 16
            save 900 1
            save 300 10
            save 60 10000
            stop-writes-on-bgsave-error yes
            rdbcompression yes
            rdbchecksum yes
     ☆      dbfilename dump.rdb
     ☆       dir ./
            slave-serve-stale-data yes
            slave-read-only yes
            repl-diskless-sync no
            repl-diskless-sync-delay 5
            repl-disable-tcp-nodelay no
     ☆      slave-priority 100
     ☆      appendonly no
            appendfilename "appendonly.aof"
            appendfsync everysec
            no-appendfsync-on-rewrite no
            auto-aof-rewrite-percentage 100
            auto-aof-rewrite-min-size 64mb
            aof-load-truncated yes
     ☆      lua-time-limit 5000
            slowlog-log-slower-than 10000
            slowlog-max-len 128
            latency-monitor-threshold 0
            notify-keyspace-events ""
            hash-max-ziplist-entries 512
            hash-max-ziplist-value 64
            list-max-ziplist-size -2
            list-compress-depth 0
            set-max-intset-entries 512
            zset-max-ziplist-entries 128
            zset-max-ziplist-value 64
            hll-sparse-max-bytes 3000
            activerehashing yes
            client-output-buffer-limit normal 0 0 0
            client-output-buffer-limit slave 256mb 64mb 60
            client-output-buffer-limit pubsub 32mb 8mb 60
            hz 10
            aof-rewrite-incremental-fsync yes

======================================================================================================
            master.conf 如下

            # master config
            include /opt/module/base.conf
            port 6379
            dbfilename "dump_master_6379.rdb"
            pidfile "/opt/module/redisData/redis_master_6379.pid"
            dir "/opt/module/redisData/master"
            protected-mode no
            # 可选项，从机登基优先级，数值越小优先级越高，继承原始配置文件为 100
            slave-priority 10

            # 给 master 设置 密码 如果给 master设置了密码，从机及哨兵 需要设置 masterauth <password>
            requirepass jiangBUG
======================================================================================================
            slave_1/2.conf 如下

            # slave_1 config
            include /opt/module/base.conf
            port 6380
            dbfilename "dump_slave_1_6380.rdb"
            pidfile "/opt/module/redisData/redis_slave_1_6380.pid"
            dir "/opt/module/redisData/slave_1"
            protected-mode no
            # 可选项，从机登基优先级，数值越小优先级越高，继承原始配置文件为 100
            slave-priority 10

            # 指定 master
            slaveof 192.168.44.3 6379

            # master 访问密码
            masterauth jiangBUG
======================================================================================================
            sentinel_1/2/3.conf 如下

            protected-mode no
            daemonize yes
            port 26379
            masterauth "jiangBUG"
            dir "/opt/module"
            sentinel monitor mymaster 192.168.44.3 6379 1
======================================================================================================
     */

}
