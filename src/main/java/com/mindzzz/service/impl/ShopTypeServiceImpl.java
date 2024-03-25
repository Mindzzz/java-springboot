package com.mindzzz.service.impl;

import cn.hutool.json.JSONUtil;
import com.mindzzz.dto.Result;
import com.mindzzz.entity.ShopType;
import com.mindzzz.mapper.ShopTypeMapper;
import com.mindzzz.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.mindzzz.utils.RedisConstants.CACHE_SHOP_TYPE_KEY;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryList() {
        List<String> shopTypeJsons = stringRedisTemplate.opsForList().range(CACHE_SHOP_TYPE_KEY,0,-1);
        List<ShopType> shopTypesRedis = new ArrayList<>();
        if(shopTypeJsons.size()!=0){
            for(String shopType:shopTypeJsons){
                ShopType type = JSONUtil.toBean(shopType,ShopType.class);
                shopTypesRedis.add(type);
            }
            return Result.ok(shopTypesRedis);
        }
        List<ShopType> typeListMysql = query().orderByAsc("sort").list();
        for(ShopType type:typeListMysql){
            String s = JSONUtil.toJsonStr(type);
            stringRedisTemplate.opsForList().rightPushAll(CACHE_SHOP_TYPE_KEY,s);
        }
        return Result.ok(typeListMysql);


    }
}
