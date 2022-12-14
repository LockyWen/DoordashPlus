package com.mincong.doordashplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mincong.doordashplus.entity.DishFlavor;
import com.mincong.doordashplus.mapper.DishFlavorMapper;
import com.mincong.doordashplus.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
        implements DishFlavorService {
}
