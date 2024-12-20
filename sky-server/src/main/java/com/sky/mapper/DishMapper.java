package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @return
     */
    Page<DishVO> select(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据主键查询菜品数据
     * @param id
     * @return
     */
    @Select("select * from dish where id=#{id}")
    Dish selectById(Long id);

    /**
     * 根据主键删除菜品数据
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据主键更新菜品表
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);
}
