package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@Api("C端订单接口")
@Slf4j
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @ApiOperation("用户下单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}",ordersSubmitDTO);

        OrderSubmitVO order = orderService.submit(ordersSubmitDTO);

        return Result.success(order);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);

        // 为了跳过商户支付
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());

        return Result.success(orderPaymentVO);
    }

    /**
     * 查询历史订单 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     * @param ordersPageQueryDTO
     * @return
     */
    @ApiOperation("查询历史订单")
    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("查询历史订单:{}",ordersPageQueryDTO);

        PageResult pageResult = orderService.historyOrders(ordersPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 根据订单id查询订单详情
     * @param id
     * @return
     */
    @ApiOperation("查询订单详情")
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> orderDetails(@PathVariable Long id) {
        log.info("查询订单详情,id:{}",id);

        OrderVO orderVO = orderService.details(id);

        return Result.success(orderVO);
    }

    /**
     * 取消订单
     * @return
     */
    @ApiOperation("取消订单")
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id) throws Exception {
        log.info("取消订单：{}", id);

        orderService.cancel(id);

        return Result.success();
    }

    /**
     * 再来一单
     * @param id
     * @return
     */
    @ApiOperation("再来一单")
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable Long id) {
        log.info("再来一单：{}",id);

        orderService.repet(id);

        return Result.success();
    }

    /**
     * 客户崔单
     * @param id
     * @return
     */
    @ApiOperation("客户崔单")
    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable Long id) {
        log.info("客户崔单：{}",id);

        orderService.remind(id);

        return Result.success();
    }
}
