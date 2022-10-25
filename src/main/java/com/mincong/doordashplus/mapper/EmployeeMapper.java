package com.mincong.doordashplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mincong.doordashplus.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
