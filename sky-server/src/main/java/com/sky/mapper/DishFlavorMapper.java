package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 插入口味表
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据多条菜品id删除口味数据
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据菜品id查询口味
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> selectByDishId(Long dishId);

    /**
     * 根据菜品id删除口味数据
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id=#{id}")
    void deleteById(Long id);
}
