package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询关联的套餐id
     * @param ids
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> ids);

    /**
     * 新增套餐关联菜品
     * @param setmealDishs
     */
    void insert(List<SetmealDish> setmealDishs);

    /**
     * 根据套餐id查询关联菜品
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getBySetmealId(Long id);

    /**
     * 根据套餐id删除关联菜品
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{id}")
    void deleteBySetmealId(Long id);

    /**
     * 根据套餐id批量删除关联菜品
     * @param ids
     */
    void deleteBySetmealIds(List<Long> ids);
}
