package com.stufz.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stufz.utils.JwtHelper;
import com.stufz.utils.Result;
import com.stufz.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器，
 * 又有效的token，放行
 * 没有，返回504
 */
@Component
public class LoginProtectedInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtHelper jwtHelper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中获取token
        String token = request.getHeader("token");
        //检查是否有效
        boolean expiration = jwtHelper.isExpiration(token);
        //有效放行
        if(!expiration){
            return true;
        }
        //无效返回504的状态json
        Result result = Result.build(null, ResultCodeEnum.NOTLOGIN);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().print(json);
        return false;
    }
}
