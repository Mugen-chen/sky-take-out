package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户信息
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    /**
     * 新增用户信息
     * @param user
     */
    void insert(User user);

    @Select("select * from user where id=#{userId}")
    User getById(Long userId);

    @Select("select count(id) from user where DATE(create_time) <= #{date}")
    Integer getTotalUser(LocalDate date);

    @Select("select count(id) from user where DATE(create_time) = #{date}")
    Integer countNewUser(LocalDate date);
}
