package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */
    void saveDishWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品及口味
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品信息(包括口味表)
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 启售或禁售菜品
     * @param status
     * @param id
     */
    void StartOrStop(Integer status, Long id);

    /**
     * 根据分类ID查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> selectByCategory(Long categoryId);
}
