package com.lcx.controller;

import com.lcx.common.Result;
import com.lcx.entity.User;
import com.lcx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : lichangxin
 * @create : 2024/4/26 14:58
 * @description
 */

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserService userService;

    @GetMapping
    Result<List<User>> userInfo() {
        return Result.ok(userService.list());
    }

}
