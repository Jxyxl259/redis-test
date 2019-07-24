import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.redis.Pair;
import test.redis.SentryRedisConnImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: redis写入测试
 * @author: jiangxy
 * @create: 2018-04-22 16:48
 */
public class RedisTest_BatchGetString {


    public static void main(String[] args) {
        ApplicationContext ioc = new ClassPathXmlApplicationContext("lettuce-1_matser-2_slave_3sentinel_masterHasPassword_test.xml");

        SentryRedisConnImpl conn = (SentryRedisConnImpl) ioc.getBean("sentryRedisConn");

        List<Pair<String,String>> pairs = new ArrayList<>();
        Pair p0 = new Pair("13","3");
        Pair p = new Pair("14","4");
//        Pair p2 = new Pair("15","5");

        pairs.add(p0);
        pairs.add(p);

        conn.batchSetString(pairs);

        String[] keys = new String[]{"10","11","12","13","14","15"};

        List<String> values = conn.batchGetString(keys);

        values.forEach( v -> {
            System.out.println(v);
        });

    }

    private static final String alph = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String[] source = alph.split("");


    private static String randomStr(int randomStrLen){
        StringBuilder res = new StringBuilder();
        for(int i = 0; i< randomStrLen ;i++){
            res.append(source[(int)Math.floor(Math.random() * 52 + 1) - 1]);
        }
        return res.toString();
    }

}
