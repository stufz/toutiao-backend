package com.stufz.service;

import com.stufz.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stufz.utils.Result;

/**
* @author 10632
* @description 针对表【news_user】的数据库操作Service
* @createDate 2024-06-06 20:08:41
*/
public interface UserService extends IService<User> {

    Result login(User user);

    /**
     * 根据token获取用户数据
     * @param token
     * @return
     */
    Result getUserInfo(String token);

    /**
     * 检查账号是否可用
     * @param usernmae
     * @return
     */
    Result checkUserName(String usernmae);

    /**
     * 用户注册
     * @param user
     * @return
     */
    Result regist(User user);
}
