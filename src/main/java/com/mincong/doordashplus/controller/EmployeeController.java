package com.mincong.doordashplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mincong.doordashplus.common.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import com.mincong.doordashplus.entity.*;
import com.mincong.doordashplus.service.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

//    @RequestBody接收前端 发送过来的JSON风格的数据，将其转化为相应的对象

    /**  登录功能处理逻辑如下:
        1、将页面提交的密码password进行 MD5 加密处理
        2、根据页面提交的用户名username查询数据库
        3、如果没有查询到数据，则返回登录失败的结果
        4、进行密码比对，如果不一致，则返回登录失败的结果
        5、查看员工状态，如果为 已禁用状态，则返回被禁用的结果信息
        6、登录成功，将员工id 存入Session并返回登录成功的结果
     * */
    @PostMapping("/login")
    public ResponseModel<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        // MD5 加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());

        Employee emp = employeeService.getOne(queryWrapper);

        if (emp == null){
            return ResponseModel.error("用户名不存在！");
        }

        if (!emp.getPassword().equals(password)){
            return ResponseModel.error("用户名或密码错误！");
        }

        if (emp.getStatus() != 1){  // 账号被禁用，status == 1,账号可以正常登录
            return ResponseModel.error("账号被禁用，请联系管理员或客服！");
        }

        request.getSession().setAttribute("employee", emp.getId());
        log.info("Current session Login: " + "employee");

        // 将从数据库
        return ResponseModel.success(emp);
    }

    //  退出功能实现
    //  1、LocalStorage 清理Session中的用户id
    //  2、返回结果

    @PostMapping("/logout")
    public ResponseModel<String> logout(HttpServletRequest request, @RequestBody Employee employee){

        request.getSession().removeAttribute("employee");
        log.info("Current session Logout: " + "employee");
        return ResponseModel.success("安全退出成功！");
    }

    @PostMapping
    public ResponseModel<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息:{}",employee.toString());

        // 在新增员工操作中，对员工的密码进行初始化( MD5加密 )
        log.info("Password: " + employee.getPassword());
        employee.setPassword(DigestUtils.md5DigestAsHex("12345".getBytes()));

        // 下面设置 公共属性的值(createTime、updateTime、createUser、updateUser)交给 MyMetaObjectHandler类处理
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return ResponseModel.success("成功新增员工");
    }

    /*  pageShow方法的返回对象 应该是MP 中的
       Page对象(包含分页数据集合records、数据总数、每页的大小)
         protected List<T> records;
         protected long total;
         protected long size;
     */

    // 分页展示功能的流程分析:
//     1、页面发送Ajax请求，将分页查询参数(page、pageSize、name)提交到服务端
//     2、服务端Controller接收页面提交的数据 并调用Service查询数据
//     3、Service调用Mapper操作数据库，查询分页的数据
//     4、Controller将查询的分页数据 响应给页面
//     5、页面接收到分页数据并通过前端(ElementUI)的table组件展示到页面上
    @GetMapping("/page")
    public ResponseModel<Page> pageShow(int page, int pageSize, String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

        // 创建分页构造器对象
        Page pageInfo = new Page(page,pageSize);

        //  构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //   name不为null，才会 比较 getUsername方法和前端传入的name是否匹配 的过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getUsername,name);
        //  根据 更新用户的时间升序 分页展示
//        queryWrapper.orderByAsc(Employee::getUpdateTime);

        // 去数据库查询
        employeeService.page(pageInfo,queryWrapper);
        return ResponseModel.success(pageInfo);
    }

    @PutMapping
    public ResponseModel<String> update(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString());

        // 下面设置 公共属性的值(createTime、updateTime、createUser、updateUser)交给 MyMetaObjectHandler类处理
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);

        employeeService.updateById(employee);
        return ResponseModel.success("员工信息修改成功！");

    }

    @GetMapping("/{id}")
    public ResponseModel<Employee> getById(@PathVariable Long id){
        log.info("根据id修改员工信息。。。。");

        Employee employee = employeeService.getById(id);

        if (employee != null){
            return ResponseModel.success(employee);
        }
        return ResponseModel.error("没有查询到员工信息！");
    }
}
