package com.code_template.service;

import com.code_template.model.po.User;
import com.code_template.model.vo.LoginVo;
import com.code_template.dao.UserMapper;
import com.code_template.model.vo.NewUserVo;
import com.code_template.model.vo.UserVo;
import com.code_template.util.AES;
import com.code_template.util.JWTUtil;
import com.code_template.util.ResponseCode;
import com.code_template.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    private boolean isUsernameExist(String username){
        User user = userMapper.selectByUsername(username);
        return user != null;
    }
    private boolean isMobileExist(String mobile){
        User user = userMapper.selectByMobile(mobile);
        return user != null;
    }

    @Transactional
    public ReturnObject getUserInfoByPrimaryKey(int id){
        try {
           User user = userMapper.selectByPrimaryKey(id);
           if(user == null){
               return new ReturnObject((ResponseCode.USER_NOT_EXIST));
           }
           user.setUsername(AES.decrypt(user.getUsername(),AES.AESSECRET));
           user.setMobile(AES.decrypt(user.getMobile(), AES.AESSECRET));
           user.setPassword(null);
           return new ReturnObject(ResponseCode.OK, user);
        } catch (Exception exception){
            return new ReturnObject(ResponseCode.SERVER_ERROR);
        }
    }
    @Transactional
    public ReturnObject updateUserInfoByPrimaryKey(int id, UserVo userVo){
        try {
            // 1. 根据id选择对应的user
            User user1 = userMapper.selectByPrimaryKey(id);
            user1.setUsername(AES.encrypt(userVo.getUsername(),AES.AESSECRET));
            user1.setMobile(AES.encrypt(userVo.getMobile(),AES.AESSECRET));
            user1.setGmtModify(new Date());
            // 2. 根据userVo对内容进行修改
            userMapper.updateByPrimaryKey(user1);
            return new ReturnObject(ResponseCode.OK);
        } catch (Exception exception){
            return new ReturnObject(ResponseCode.SERVER_ERROR);
        }
    }

    @Transactional
    public ReturnObject deleteUserByPrimaryKey(int id){
        try {
            userMapper.deleteByPrimaryKey(id);
            return new ReturnObject(ResponseCode.OK);
        } catch (Exception exception){
            return new ReturnObject(ResponseCode.SERVER_ERROR);
        }
    }
    @Transactional
    public ReturnObject userLogin(LoginVo loginVo){
        String encodedUsername = AES.encrypt(loginVo.getUsername(), AES.AESSECRET);
        String encodedPassword = AES.encrypt(loginVo.getPassword(), AES.AESSECRET);
        JWTUtil jwtUtil = new JWTUtil();
        // 1. 根据username查询数据
        User user = userMapper.selectByUsername(encodedUsername);
        // 2. 判断encode之后的password和数据库中存储的password是否相同
        if(user == null){
            return new ReturnObject(ResponseCode.USER_NOT_EXIST);
        }
        if(!encodedPassword.equals(user.getPassword())){
            System.out.println("密码错误,重新输入密码");
            // 封装一个返回类
            return new ReturnObject(ResponseCode.PASSWORD_ERROR);
        }
        System.out.println(user);
        // redis判断重复登录
        String u_id = "user_" + user.getId();
        String token = jwtUtil.createToken(user);
        if(redisTemplate.hasKey(u_id)){
            redisTemplate.delete(u_id);
        }
        // 更改redis状态, 修改登录时间、登录ip
        redisTemplate.opsForSet().add(u_id, token);
        user.setLastLoginTime(new Date());
        userMapper.updateByPrimaryKey(user);
        return new ReturnObject(ResponseCode.OK,token);
    }

    @Transactional
    public ReturnObject userRegister(NewUserVo newUserVo){
        // 1. 判断数据是否合法

        // 2. 判断数据库内是否有重复的
        if(isUsernameExist(AES.encrypt(newUserVo.getUsername(), AES.AESSECRET))){
            return new ReturnObject(ResponseCode.USERNAME_REGISTERED);
        }
        if(isMobileExist(AES.encrypt(newUserVo.getMobile(), AES.AESSECRET))){
            return new ReturnObject(ResponseCode.MOBILE_REGISTERED);
        }
        // 3. 新建po，插入数据
        User newUser = new User();
        newUser.setUsername(AES.encrypt(newUserVo.getUsername(), AES.AESSECRET));
        newUser.setPassword(AES.encrypt(newUserVo.getPassword(), AES.AESSECRET));
        newUser.setMobile(AES.encrypt(newUserVo.getMobile(), AES.AESSECRET));
        newUser.setDepart(0);
        newUser.setState(0);
        newUser.setGmtCreate(new Date());
        newUser.setGmtModify(new Date());
        userMapper.insert(newUser);
        return new ReturnObject(ResponseCode.OK);
    }
}
