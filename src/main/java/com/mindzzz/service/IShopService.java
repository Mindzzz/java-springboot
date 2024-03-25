package com.mindzzz.service;

import com.mindzzz.dto.Result;
import com.mindzzz.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface IShopService extends IService<Shop> {

    Result queryById(Long id);

    Result updateSql(Shop shop);

    Result queryShopByType(Integer typeId, Integer current, Double x, Double y);
}
