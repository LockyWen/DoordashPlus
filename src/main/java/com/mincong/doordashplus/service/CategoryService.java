package com.mincong.doordashplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mincong.doordashplus.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}


