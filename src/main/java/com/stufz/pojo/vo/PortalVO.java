package com.stufz.pojo.vo;

import lombok.Data;
import org.springframework.web.util.pattern.PathPattern;

@Data
public class PortalVO {
    private String keyWords;

    private int type = 0;

    private int pageNum = 1;

    private int pageSize = 10;
}
