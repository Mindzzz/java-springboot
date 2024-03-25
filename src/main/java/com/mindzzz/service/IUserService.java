package com.mindzzz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindzzz.dto.LoginFormDTO;
import com.mindzzz.dto.Result;
import com.mindzzz.entity.User;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);

    Result sign();

    Result signCount();
}
