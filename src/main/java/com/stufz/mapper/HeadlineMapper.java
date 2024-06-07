package com.stufz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.stufz.pojo.Headline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stufz.pojo.vo.PortalVO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author 10632
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2024-06-06 20:08:41
* @Entity com.stufz.pojo.Headline
*/
public interface HeadlineMapper extends BaseMapper<Headline> {
    IPage<Map> selectMyPage(IPage page, @Param("portalVO")PortalVO portalVO);

    Map queryDetailMap(Integer hid);
}




