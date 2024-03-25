package com.mindzzz.service;

import com.mindzzz.dto.Result;
import com.mindzzz.entity.ShopType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface IShopTypeService extends IService<ShopType> {

    Result queryList();
}
