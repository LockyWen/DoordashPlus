package com.mincong.doordashplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mincong.doordashplus.common.Result;
import com.mincong.doordashplus.dto.DishDto;
import com.mincong.doordashplus.entity.Category;
import com.mincong.doordashplus.entity.Dish;
import com.mincong.doordashplus.service.CategoryService;
import com.mincong.doordashplus.service.DishFlavorService;
import com.mincong.doordashplus.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        //DishDto是前端要的东西和后端的Dish不一样，要扩展一下
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝，忽略record对象，因为record就是查出来的记录数，也就是pageInfo
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        //将List集合搬入Dto中
        //这里是流式编程的内容，或者用foreach来进行搬运也可以解决
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);

        return Result.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public Result<DishDto> set(@PathVariable Long id){
        //因为是直接查Dto数据嘛，用现成的肯定不行了，在Service层自己写，这是个多表联查的过程
        DishDto dishDto=dishService.getByDishIdWithFlavor(id);

        return Result.success(dishDto);
    }

    @PutMapping()
    public Result<String> updateDish(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);

        return Result.success("Update success!!");
    }



    /**
     * 更新菜品为停售
     * @param ids Dish的id
     * @return
     */
    @PostMapping("/status/0")
    public Result<String> updateStatusStop(Long ids){
        Dish dish=dishService.getById(ids);
        dish.setStatus(0);
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getId, ids);
        dishService.update(dish, lambdaQueryWrapper);
        return Result.success("Updated Success");
    }

    /**
     * 更新菜品状态为起售
     * @param ids Dish的id
     * @return
     */
    @PostMapping("/status/1")
    public Result<String> updateStatusStart(Long ids){
        Dish dish=dishService.getById(ids);
        dish.setStatus(1);
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getId, ids);
        dishService.update(dish, lambdaQueryWrapper);
        return Result.success("Updated Success");
    }

    /**
     * 这里是逻辑删除，不是真删，把isDeleted字段更新为1就不显示了，间接完成了逻辑删除
     * @param ids Dish的id
     * @return
     */
    @DeleteMapping()
    public Result<String> deleteDish(Long ids){
        Dish dish=dishService.getById(ids);
        dish.setIsDeleted(1);
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getId, ids);
        dishService.update(dish, lambdaQueryWrapper);
        return Result.success("Deleted Success!");
    }


    /**
     * 新增套餐时 根据条件查询并填充菜品列表
     * @param dish 这里本来是categoryId的，但是为了保证通用性，这里用Dish对象进行封装 有更好的通用性
     *             Dish本身里面也是有categoryId的
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> listCategory(Dish dish){
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //只查在售状态的菜品，1为启售状态
        lambdaQueryWrapper.eq(Dish::getStatus, 1);
        //排序，多个字段排序，先按Sort排，再按UpdateTime排
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(lambdaQueryWrapper);
        return Result.success(dishList);
    }
}
