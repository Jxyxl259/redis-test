import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.redis.SentryRedisConnImpl;

/**
 * @description: redis写入测试
 * @author: jiangxy
 * @create: 2018-04-22 16:48
 */
public class RedisTest {


    public static void main(String[] args) {
        ApplicationContext ioc = new ClassPathXmlApplicationContext("lettuce-1_matser-2_slave_3sentinel_masterHasPassword_test.xml");

        SentryRedisConnImpl conn = (SentryRedisConnImpl) ioc.getBean("sentryRedisConn");

        String resp = conn.set("test_return", "aaaaa");

        System.out.println(resp);

        /*Map<String,String> paramMap = new HashMap<>(16);
        paramMap.put("4700","0");
        paramMap.put("4701","0");
        paramMap.put("4702","0");
        paramMap.put("4703","0");
        conn.hmset("waitToKafkaInvokeUserId", paramMap);
*/

//        for(int i = 0 ; i< 5000 ; i++){
//            if(i%2 == 0){
//                conn.set(randomStr(5)+"_"+ i, "val_"+i);
//            }else{
//                conn.set(randomStr(5)+"_"+ i, "val_"+i);
//            }
//        }

       // System.out.println(randomStr(5));

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
