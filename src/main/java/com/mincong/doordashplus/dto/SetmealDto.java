package com.mincong.doordashplus.dto;


import com.mincong.doordashplus.entity.Setmeal;
import com.mincong.doordashplus.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
