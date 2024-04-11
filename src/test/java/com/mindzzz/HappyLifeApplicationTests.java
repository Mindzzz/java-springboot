package com.mindzzz;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.mindzzz.dto.LoginFormDTO;
import com.mindzzz.dto.Result;
import com.mindzzz.entity.Shop;
import com.mindzzz.entity.User;
import com.mindzzz.service.IUserService;
import com.mindzzz.service.impl.ShopServiceImpl;
import com.mindzzz.service.impl.UserServiceImpl;
import com.mindzzz.utils.RandomPhoneNumber;
import com.mindzzz.utils.RedisIdWorker;
import com.mindzzz.utils.RegexUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.mindzzz.utils.RedisConstants.CACHE_SHOP_KEY;
import static com.mindzzz.utils.RedisConstants.SHOP_GEO_KEY;
import static com.mindzzz.utils.SystemConstants.USER_NICK_NAME_PREFIX;

@SpringBootTest
@AutoConfigureMockMvc
class HappyLifeApplicationTests {
    @Resource
    private ShopServiceImpl shopService;
    @Resource
    private RedisIdWorker redisIdWorker;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserServiceImpl userServiceI;

    @Test
    public void createUserBy1000(){
        List<String> phones = RandomPhoneNumber.randomCreatePhone(1000);
        phones.stream().forEach(phone -> {
            if(!RegexUtils.isPhoneInvalid(phone)){
                User login_user = new User();
                login_user.setPhone(phone);
                login_user.setCreateTime(LocalDateTime.now());
                login_user.setUpdateTime(LocalDateTime.now());
                String nickName_suf = RandomUtil.randomString(10);
                login_user.setNickName(USER_NICK_NAME_PREFIX + nickName_suf);
                userServiceI.save(login_user);
            }
        });
    }

    @Test
    public void tokenBy1000() throws Exception {
        String phone = "";
        String code = "";
        //注意！这里的绝对路径设置为自己想要的地方
        OutputStreamWriter osw = null;
        osw = new OutputStreamWriter(new FileOutputStream("token.txt"));
        //先模拟10个用户的登录
        for (int i = 10; i <= 1009; i++) {
            User user = userService.getById(i);
            phone = user.getPhone();
            //创建虚拟请求，模拟通过手机号，发送验证码
            ResultActions perform1 = mockMvc.perform(MockMvcRequestBuilders
                    .post("/user/code?phone=" + phone));
            //获得Response的body信息
            String resultJson1 = perform1.andReturn().getResponse().getContentAsString();
            //将结果转换为result对象
            Result result = JSONUtil.toBean(resultJson1, Result.class);
            //获得验证码
            code = result.getData().toString();
            //创建登录表单
            LoginFormDTO loginFormDTO = new LoginFormDTO();
            loginFormDTO.setCode(code);
            loginFormDTO.setPhone(phone);
            //将表单转换为json格式的字符串
            String loginFormDtoJson = JSONUtil.toJsonStr(loginFormDTO);
            //创建虚拟请求，模拟登录
            ResultActions perform2 = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                    //设置contentType表示为json信息
                    .contentType(MediaType.APPLICATION_JSON)
                    //放入json对象
                    .content(loginFormDtoJson));
            String resultJson2 = perform2.andReturn().getResponse().getContentAsString();
            Result result2 = JSONUtil.toBean(resultJson2, Result.class);
            //获得token
            String token = result2.getData().toString();
            //写入
            osw.write(token+"\n");
        }
        //关闭输出流
        osw.close();
    }













    private ExecutorService es= Executors.newFixedThreadPool(500);

    @Test
    void testIdWorker() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);
        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                long id = redisIdWorker.nextId("order");
                System.out.println("id:"+id);
            }
            latch.countDown();
        };
        long begin =System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            es.submit(task);
        }
        latch.await();
        long end =System.currentTimeMillis();
        System.out.println(end-begin);
    }
    @Test
    void testSaveShop(){
        stringRedisTemplate.delete(CACHE_SHOP_KEY + 1L);
        //shopService.saveShop2Redis(1L,10L);
    }

    @Test
    void loadShopData() {
        // 1.查询店铺信息
        List<Shop> list = shopService.list();
        // 2.把店铺分组，按照typeId分组，typeId一致的放到一个集合
        Map<Long, List<Shop>> map = list.stream().collect(Collectors.groupingBy(Shop::getTypeId));
        // 3.分批完成写入Redis
        for (Map.Entry<Long, List<Shop>> entry : map.entrySet()) {
            // 3.1.获取类型id
            Long typeId = entry.getKey();
            String key = SHOP_GEO_KEY + typeId;
            // 3.2.获取同类型的店铺的集合
            List<Shop> value = entry.getValue();
            List<RedisGeoCommands.GeoLocation<String>> locations = new ArrayList<>(value.size());
            // 3.3.写入redis GEOADD key 经度 纬度 member
            for (Shop shop : value) {
                // stringRedisTemplate.opsForGeo().add(key, new Point(shop.getX(), shop.getY()), shop.getId().toString());
                locations.add(new RedisGeoCommands.GeoLocation<>(
                        shop.getId().toString(),
                        new Point(shop.getX(), shop.getY())
                ));
            }
            stringRedisTemplate.opsForGeo().add(key, locations);
        }
    }




}
