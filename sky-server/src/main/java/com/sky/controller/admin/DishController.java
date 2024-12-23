package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@Api("菜品相关接口")
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result newDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品信息：{}", dishDTO);

        dishService.saveDishWithFlavor(dishDTO);

        // 清除redis缓存
        cleanChace("dish_" + dishDTO.getCategoryId());

        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询：{}",dishPageQueryDTO);

        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @ApiOperation("批量删除菜品")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除菜品");

        dishService.deleteBatch(ids);

        // 清除所有菜品redis缓存
        cleanChace("dish_*");

        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品，id：{}", id);

        DishVO dishVO = dishService.getByIdWithFlavor(id);

        return Result.success(dishVO);
    }

    /**
     * 修改菜品信息
     * @param dishDTO
     * @return
     */
    @ApiOperation("修改菜品信息")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息：{}",dishDTO);

        dishService.updateWithFlavor(dishDTO);

        // 清除所有菜品redis缓存
        cleanChace("dish_*");

        return Result.success();
    }

    /**
     * 启售或禁售菜品
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启售或禁售菜品")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,
                              @RequestParam Long id) {
        log.info("启售或禁售菜品:{},{}",status,id);

        dishService.StartOrStop(status, id);

        // 清除所有菜品redis缓存
        cleanChace("dish_*");

        return Result.success();
    }

    /**
     * 根据分类ID查询菜品，或根据菜名模糊查询
     * @param dish
     * @return
     */
    @ApiOperation("根据分类ID或菜品名称查询菜品")
    @GetMapping("/list")
    public Result<List<Dish>> getByCategory(Dish dish) {
        log.info("根据分类ID或菜品名称查询菜品，categoryId，name：{}，{}", dish.getCategoryId(), dish.getName());

        List<Dish> dishs = dishService.selectByCategory(dish);

        return Result.success(dishs);
    }

    /**
     * 清除redis缓存
     * @param pattern
     */
    private void cleanChace(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
