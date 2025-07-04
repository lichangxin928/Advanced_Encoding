package com.lcx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcx.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : lichangxin
 * @create : 2024/4/26 15:04
 * @description
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
