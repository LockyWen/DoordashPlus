package com.mincong.doordashplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mincong.doordashplus.entity.SetmealDish;
import com.mincong.doordashplus.mapper.SetmealDishMapper;
import com.mincong.doordashplus.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>
          implements SetmealDishService {
}
