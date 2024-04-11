# 第一个spring boot项目
lifeshare是一个前后端分离的项目，前端部署在nginx服务器上，后端部署在tomcat上。实现了短信登录，商户类别、查看附近商家、探店分享、点赞、关注、优惠卷秒杀，签到等功能，业务可以帮助商家引流，增加曝光度，也可以为用户提供良好的生活体验。

# 对项目进行压力测试，看看在1000个用户秒杀200个优惠卷会不会出现问题


![image](https://github.com/Mindzzz/lifeshare/assets/100667194/ac931d28-a691-4515-8ab3-9806ebf2266c)
未出现异常情况，未抢到单的用户均已返回库存不足
![image](https://github.com/Mindzzz/lifeshare/assets/100667194/76e4295b-af8f-46d3-8a56-48685954fe84)
优惠卷15的库存为0，未出现超卖，
![image](https://github.com/Mindzzz/lifeshare/assets/100667194/0db81af6-f260-4681-8a19-d6d67a8e56b6)
订单增加200个，未出现超卖现象和一人多单问题。


