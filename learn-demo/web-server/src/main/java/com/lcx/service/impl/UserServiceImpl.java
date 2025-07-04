package com.lcx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.entity.User;
import com.lcx.mapper.UserMapper;
import com.lcx.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author : lichangxin
 * @create : 2024/4/26 15:03
 * @description
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
