package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
