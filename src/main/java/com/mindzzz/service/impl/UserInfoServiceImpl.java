package com.mindzzz.service.impl;

import com.mindzzz.entity.UserInfo;
import com.mindzzz.mapper.UserInfoMapper;
import com.mindzzz.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
