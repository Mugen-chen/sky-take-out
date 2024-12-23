package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("套餐相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;


    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询：{}",setmealPageQueryDTO);

        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 新增菜品
     * @param setmealDTO
     * @return
     */
    @ApiOperation("新增套餐")
    @PostMapping
    @CacheEvict(cacheNames = "SetmealCache",key = "#setmealDTO.categoryId")
    public Result newSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐:{}",setmealDTO);

        setmealService.addSetmeal(setmealDTO);

        return Result.success();
    }

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @ApiOperation("根据id查询套餐信息")
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据id查询套餐信息，id：{}", id);

        SetmealVO setmeal = setmealService.getById(id);

        return Result.success(setmeal);
    }

    /**
     * 修改套餐信息
     * @param setmealDTO
     * @return
     */
    @ApiOperation("修改套餐信息")
    @PutMapping
    @CacheEvict(cacheNames = "SetmealCache",allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐信息：{}",setmealDTO);

        setmealService.update(setmealDTO);

        return Result.success();
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @ApiOperation("批量删除套餐")
    @DeleteMapping
    @CacheEvict(cacheNames = "SetmealCache",allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除套餐:{}",ids);

        setmealService.deleteBatch(ids);

        return Result.success();
    }

    /**
     * 起售或禁售套餐
     * @return
     */
    @ApiOperation("起售或禁售套餐")
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "SetmealCache",allEntries = true)
    public Result startOrStop(@PathVariable Integer status,
                              Long id) {
        log.info("起售或禁售套餐，status:{}，id:{}",status,id);

        setmealService.startOrStop(status, id);

        return Result.success();
    }

}
