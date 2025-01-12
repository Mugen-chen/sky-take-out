package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额数据统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnover(LocalDate begin, LocalDate end) {
        // 构建时间列表
        List<LocalDate> dateList = getDateList(begin,end);

        // 统计dateList日期对应的营业额
        List<BigDecimal> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            BigDecimal turnover = orderMapper.sumAmount(date,Orders.COMPLETED);
            turnoverList.add(turnover);
        }

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList,','))
                .turnoverList(StringUtils.join(turnoverList,','))
                .build();
    }

    /**
     * 用户数量统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 构建时间列表
        List<LocalDate> dateList = getDateList(begin,end);

        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();

        for (int i = 0; i < dateList.size(); i++) {
            LocalDate date = dateList.get(i);

            newUserList.add(userMapper.countNewUser(date));
            totalUserList.add(userMapper.getTotalUser(date));
        }

        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList,','))
                .newUserList(StringUtils.join(newUserList,','))
                .totalUserList(StringUtils.join(totalUserList,','))
                .build();
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        // 构建时间列表
        List<LocalDate> dateList = getDateList(begin,end);

        //每天订单总数集合
        List<Integer> orderCountList = new ArrayList<>();
        //每天有效订单数集合
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            //查询每天的总订单数
            orderCountList.add(orderMapper.countOrders(date, null));
            //查询每天的有效订单数
            validOrderCountList.add(orderMapper.countOrders(date, Orders.COMPLETED));
        }

        //时间区间内的总订单数
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        //时间区间内的总有效订单数
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        //订单完成率
        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0){
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    public List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        // 构建时间列表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (begin.isBefore(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        return dateList;
    }
}
