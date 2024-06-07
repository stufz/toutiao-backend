package com.stufz.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stufz.pojo.Headline;
import com.stufz.pojo.vo.PortalVO;
import com.stufz.service.HeadlineService;
import com.stufz.mapper.HeadlineMapper;
import com.stufz.utils.JwtHelper;
import com.stufz.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 10632
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2024-06-06 20:08:41
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{

    @Autowired
    private HeadlineMapper headlineMapper;

    @Autowired
    private JwtHelper jwtHelper;

    /**
     * 首页数据查询
     * 1. 进行分页数据查询
     * 2. 分页数据，拼接到result中
     * 1.查询需要自定义语句，自定义mapper的方法，携带分页
     * 2.返回的结果List<Map>
     * @param portalVO
     * @return
     */
    @Override
    public Result findNewsPage(PortalVO portalVO) {
        //1.条件拼接 需要非空判断
        LambdaQueryWrapper<Headline> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(portalVO.getKeyWords()),Headline::getTitle,portalVO.getKeyWords())
                .eq(portalVO.getType()!= 0,Headline::getType,portalVO.getType());

        //2.分页参数
        IPage<Headline> page = new Page<>(portalVO.getPageNum(),portalVO.getPageSize());

        //3.分页查询
        //查询的结果 "pastHours":"3"   // 发布时间已过小时数 我们查询返回一个map
        //自定义方法
        headlineMapper.selectMyPage(page, portalVO);

        //4.结果封装
        //分页数据封装
        Map<String,Object> pageInfo =new HashMap<>();
        pageInfo.put("pageData",page.getRecords());
        pageInfo.put("pageNum",page.getCurrent());
        pageInfo.put("pageSize",page.getSize());
        pageInfo.put("totalPage",page.getPages());
        pageInfo.put("totalSize",page.getTotal());

        Map<String,Object> pageInfoMap=new HashMap<>();
        pageInfoMap.put("pageInfo",pageInfo);
        // 响应JSON
        return Result.ok(pageInfoMap);
    }

    /**
     * 根据id查询详情
     * 1.修改阅读量+1
     * 2.查询对应的数据即可
     * @param hid
     * @return
     */
    @Override
    public Result showHeadlineDetail(Integer hid) {
        Map data = headlineMapper.queryDetailMap(hid);
        Map headlineMap = new HashMap();
        headlineMap.put("headline",data);
        //修改阅读量+1
        Headline headline =  new Headline();
        headline.setHid((Integer) data.get("hid"));
        headline.setVersion((Integer)data.get("version"));
        headline.setPageViews((Integer)data.get("pageViews")+1);
        headlineMapper.updateById(headline);
        return Result.ok(headlineMap);
    }

    /**
     * 发布头条方法
     * @param headline
     * @return
     */
    @Override
    public Result publish(Headline headline,String token) {

        //根据token查询用户Id
        int userId = jwtHelper.getUserId(token).intValue();
        //数据装配
        headline.setPublisher(userId);
        headline.setPageViews(0);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());

        headlineMapper.insert(headline);

        return Result.ok(null);
    }

    /**
     * 修改头条数据
     * 1.hid查询数据的最新version
     * 2.修改数据的修改时间为当前节点
     * @param headline
     * @return
     */
    @Override
    public Result updateData(Headline headline) {
        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();
        headline.setVersion(version);
        headline.setUpdateTime(new Date());

        headlineMapper.updateById(headline);
        return Result.ok(null);
    }
}




