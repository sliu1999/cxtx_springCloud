package com.cxtx.common.unit;

import com.alibaba.fastjson.JSONObject;
import com.cxtx.common.domain.JwtModel;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class HttpServletUtils {

    public static JwtModel getUserInfo(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null){
            HttpServletRequest httpServletRequest = requestAttributes.getRequest();
            String userInfo = httpServletRequest.getHeader("userInfo");
            JSONObject jsonObject = JSONObject.parseObject(userInfo);
            JwtModel jwtModel = new JwtModel();
            jwtModel.setUserId(jsonObject.getString("userId"));
            jwtModel.setUsername(jsonObject.getString("username"));
            return jwtModel;
        }
        return null;
    }

}
