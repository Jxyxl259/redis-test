import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import test.redis.SentryRedisConnImpl;

import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description: redisTemplate测试类
 * @author: jiangxy
 * @create: 2018-05-09 16:22
 */
public class StringRedisTemplateTest {

    public static void main(String[] args) {
        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext-redis-springdata.xml");

        StringRedisTemplate template = (StringRedisTemplate) ioc.getBean("stringRedisTemplate");

        DefaultRedisScript<String> script = (DefaultRedisScript<String>)ioc.getBean("ledouMergeScript");

        Map<String,String> paramMap = new HashMap<>();

               paramMap.put("4001","100001");
               paramMap.put("4002","100002");
               paramMap.put("4003","100003");
               paramMap.put("4004","100004");
               paramMap.put("4005","100005");
               paramMap.put("4006","100006");
               paramMap.put("4007","100007");
               paramMap.put("4008","100008");
               paramMap.put("4009","100009");
               paramMap.put("4000","100000");

        paramMap.forEach(( uid, lid) ->{
            List<String> keys = new ArrayList<>();
            keys.add(uid);
            keys.add(lid);
            String execute = template.execute(script, keys, "2018", "2019");
            System.out.println(execute);
        });

//        List<String> keys = new ArrayList<>();
//        keys.add("1011168408");
//
//        String execute = template.execute(script, keys, "2018", "2019");
//
//        System.out.println(execute);

//        Map<String,String> multiSet = new HashMap<>();
//
//        for(int i = 0 ; i< 10000 ; i++){
//            template.opsForValue().set("SEQ|"+i, ""+i, 6, TimeUnit.MINUTES);
//        }
//
//        Long start = System.currentTimeMillis();
//
//        for(int i = 0 ; i< 10000 ; i++){
//            template.delete("SEQ|"+i);
//            //template.expireAt("SEQ|"+i, new Date());
//        }
//        Long end = System.currentTimeMillis() - start;
//        System.out.println("total time" + end);

        //两者差不多



    }

}
