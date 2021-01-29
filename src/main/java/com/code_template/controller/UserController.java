package com.code_template.controller;

import com.code_template.annotation.Audit;
import com.code_template.model.po.User;
import com.code_template.model.vo.LoginVo;
import com.code_template.model.vo.NewUserVo;
import com.code_template.model.vo.UserVo;
import com.code_template.service.UserService;
import com.code_template.util.ReturnObject;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class UserController {
    @Autowired
    UserService userService;

    @Audit
    @GetMapping("")
    public ReturnObject getUserInfo(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int user_id  = (int) request.getAttribute("user_id");
        return userService.getUserInfoByPrimaryKey(user_id);
    }

    @Audit
    @PutMapping("/{id}")
    public ReturnObject updateUserInfo(@PathVariable int id, @RequestBody UserVo userVo){
        return userService.updateUserInfoByPrimaryKey(id, userVo);
    }

    @Audit
    @DeleteMapping("/{id}")
    public ReturnObject deleteUser(@PathVariable int id){
        return userService.deleteUserByPrimaryKey(id);
    }

    @PostMapping("/login")
    public ReturnObject postUserLogin(@RequestBody LoginVo loginVo){
       // 验证是否合法
       return userService.userLogin(loginVo);
    }
    @PostMapping("/register")
    public ReturnObject postUserRegister(@RequestBody NewUserVo newUserVo){
        return userService.userRegister(newUserVo);
    }
}
