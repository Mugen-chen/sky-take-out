package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 根据用户id条件查询历史订单数据
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> getByUserId(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单id查询
     * @param id
     * @return
     */
    @Select("select * from orders where id=#{id}")
    Orders getByOrderId(Long id);

    @Select("select count(id) from orders where status=#{status}")
    Integer countStatus(Integer status);

    /**
     * 查询支付超时订单
     * @param status
     * @param time
     * @return
     */
    @Select("select * from orders where status=#{status} and order_time < #{time}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime time);

    @Select("select ifnull(sum(amount),0.0) from orders where status=#{completed} and DATE(order_time)=#{date}")
    BigDecimal sumAmount(LocalDate date, Integer completed);
}
