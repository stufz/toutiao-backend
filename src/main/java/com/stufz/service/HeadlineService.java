package com.stufz.service;

import com.stufz.pojo.Headline;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stufz.pojo.vo.PortalVO;
import com.stufz.utils.Result;

/**
* @author 10632
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2024-06-06 20:08:41
*/
public interface HeadlineService extends IService<Headline> {

    /**
     * 首页数据查询
     * @param portalVO
     * @return
     */
    Result findNewsPage(PortalVO portalVO);

    /**
     * 根据id查询头条详情
     * @param hid
     * @return
     */
    Result showHeadlineDetail(Integer hid);

    /**
     * 发布头条
     * @param headline
     * @return
     */
    Result publish(Headline headline,String token);

    /**
     * 更新头条
     * @param headline
     * @return
     */
    Result updateData(Headline headline);
}
