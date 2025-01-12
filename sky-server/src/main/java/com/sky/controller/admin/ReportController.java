package com.sky.controller.admin;

import com.sky.mapper.OrderMapper;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@RestController
@Api("统计报表相关接口")
@Slf4j
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 营业额数据统计
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("营业额数据统计")
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("营业额数据统计:from {} to {}",begin,end);

        TurnoverReportVO turnoverReportVO = reportService.getTurnover(begin,end);

        return Result.success(turnoverReportVO);
    }

    /**
     * 用户数量统计
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("用户数量统计")
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        log.info("用户数量统计：from {} to {}",begin,end);

        UserReportVO userReportVO = reportService.getUserStatistics(begin,end);

        return Result.success(userReportVO);
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("用户数量统计")
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> orderStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        log.info("订单统计：from {} to {}",begin,end);

        OrderReportVO orderReportVO = reportService.getOrderStatistics(begin,end);

        return Result.success(orderReportVO);
    }

    /**
     * 销量统计
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("销量统计")
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> topStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        log.info("销量统计：from {} to {}",begin,end);

        SalesTop10ReportVO salesTop10ReportVO = reportService.getSalesTop10(begin,end);

        return Result.success(salesTop10ReportVO);
    }

    /**
     * 获取运营数据报表
     */
    @ApiOperation("获取运营数据报表")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        reportService.downBusinessData(response);
    }

}
