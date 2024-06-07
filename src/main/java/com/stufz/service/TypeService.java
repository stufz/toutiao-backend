package com.stufz.service;

import com.stufz.pojo.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stufz.utils.Result;

/**
* @author 10632
* @description 针对表【news_type】的数据库操作Service
* @createDate 2024-06-06 20:08:41
*/
public interface TypeService extends IService<Type> {

    /**
     * 查询所有类别数据
     * @return
     */
    Result findAllTypes();
}
