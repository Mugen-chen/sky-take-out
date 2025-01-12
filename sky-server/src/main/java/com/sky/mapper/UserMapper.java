package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.Map;

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

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    @Select("select * from user where id=#{userId}")
    User getById(Long userId);

    /**
     * 统计用户总数
     * @param date
     * @return
     */
    @Select("select count(id) from user where DATE(create_time) <= #{date}")
    Integer getTotalUser(LocalDate date);

    /**
     * 统计新增用户数量
     * @param date
     * @return
     */
    @Select("select count(id) from user where DATE(create_time) = #{date}")
    Integer countNewUser(LocalDate date);

    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
