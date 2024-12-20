package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     *新增员工
     * @param employeeDTO
     * @return
     */
    @ApiOperation("新增员工")
    @PostMapping
    public Result<String> addEmp(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);

        employeeService.addEmp(employeeDTO);

        return Result.success();
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页查询，参数：{}",employeePageQueryDTO);

        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 启用或禁用员工账号
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启用或禁用员工账号")
    @PostMapping("/status/{status}")
    public Result StartOrStop(@PathVariable Integer status,
                              @RequestParam Long id) {
        log.info("启用或禁用员工账号:{},{}",status,id);

        employeeService.StartOrStop(status, id);

        return Result.success();
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @ApiOperation("根据id查询员工信息")
    @GetMapping("/{id}")
    public Result<Employee> idQuery(@PathVariable Long id) {
        log.info("根据id查询员工信息：{}",id);

        Employee emp = employeeService.getById(id);

        return Result.success(emp);
    }

    /**
     * 修改员工基本信息
     * @param employeeDTO
     * @return
     */
    @ApiOperation("修改员工基本信息")
    @PutMapping
    public Result updateEmp(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工基本信息：{}",employeeDTO);
        employeeService.updateEmp(employeeDTO);
        return Result.success();
    }

}
