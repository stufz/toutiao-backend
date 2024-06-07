package com.stufz.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stufz.pojo.User;
import com.stufz.service.UserService;
import com.stufz.mapper.UserMapper;
import com.stufz.utils.JwtHelper;
import com.stufz.utils.MD5Util;
import com.stufz.utils.Result;
import com.stufz.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author 10632
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2024-06-06 20:08:41
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;
    /**
     * 登录业务
     * 1.根据账号，查询用户对象
     * 2.如果账号为空，查询失败，账号错误
     * 3.对比，密码，失败，返回503
     * 4.正确，根据用户id，生成token，将token返回result
     * @param user
     * @return
     */
    @Override
    public Result login(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,user.getUsername());
        User loginUser = userMapper.selectOne(lambdaQueryWrapper);
        if(loginUser == null){
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }
        //对比密码
        if(!StringUtils.isEmpty(user.getUserPwd())&& MD5Util.encrypt(user.getUserPwd()).equals(loginUser.getUserPwd())){
            //登录成功
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));
            Map data = new HashMap();
            data.put("token",token);
            return Result.ok(data);
        }
        //密码错误
        return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
    }

    /**
     * 根据token获取用户数据
     * 1.token是否在有效期
     * 2.根据toekn解析userId
     * 3.根据用户id查询用户数据
     * 4.去掉密码，封装result结果返回
     * @param token
     * @return
     */
    @Override
    public Result getUserInfo(String token) {
        //token是否过期
        boolean expiration = jwtHelper.isExpiration(token);
        if(expiration){
            //失效，未登录看待
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int userId = jwtHelper.getUserId(token).intValue();
        User user = userMapper.selectById(userId);
        if(user!=null){
            user.setUserPwd(null);
            HashMap map = new HashMap();
            map.put("loginUser",user);

            return Result.ok(map);
        }
        return Result.build(null,ResultCodeEnum.NOTLOGIN);
    }

    /**
     * 检查账号是否可用
     * 1.根据账号进行count查询
     * @param usernmae
     * @return
     */
    @Override
    public Result checkUserName(String usernmae) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,usernmae);
        Long count = userMapper.selectCount(queryWrapper);
        if(count==0){
            return Result.ok(null);
        }
        return Result.build(null,ResultCodeEnum.USERNAME_USED);
    }

    /**
     * 用户注册
     * 1.检查账号是否已经被注册
     * 2.密码加密处理
     * 3.将账号数据保存
     * 4.返回结果
     * @param user
     * @return
     */
    @Override
    public Result regist(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        Long count = userMapper.selectCount(queryWrapper);
        if(count>0){
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }
        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        userMapper.insert(user);
        return Result.ok(null);
    }
}




