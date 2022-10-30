package com.mincong.doordashplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mincong.doordashplus.entity.ShoppingCart;
import com.mincong.doordashplus.mapper.ShoppingCartMapper;
import com.mincong.doordashplus.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}