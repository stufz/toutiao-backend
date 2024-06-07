package com.stufz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stufz.pojo.Type;
import com.stufz.service.TypeService;
import com.stufz.mapper.TypeMapper;
import com.stufz.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 10632
* @description 针对表【news_type】的数据库操作Service实现
* @createDate 2024-06-06 20:08:41
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{
    @Autowired
    private TypeMapper typeMapper;

    /**
     * 查询所有类别
     * @return
     */
    @Override
    public Result findAllTypes() {

        List<Type>types = typeMapper.selectList(null);
        return Result.ok(types);
    }
}




