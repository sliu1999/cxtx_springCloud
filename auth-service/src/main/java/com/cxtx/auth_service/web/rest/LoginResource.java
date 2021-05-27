package com.cxtx.auth_service.web.rest;



import com.cxtx.auth_service.service.AuthUserService;
import com.cxtx.auth_service.unit.HttpServletUtils;
import com.cxtx.common.config.jwt.JWTUtil;
import com.cxtx.common.config.jwt.vo.*;
import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.unit.Md5;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api")
public class LoginResource {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    StringRedisTemplate redisTemplate;

    private ObjectMapper objectMapper;
    public LoginResource(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

     @Value("${secretKey:123456}")
      private String secretKey;

     @Autowired
      private StringRedisTemplate stringRedisTemplate;

        @PostMapping("/login")
        public ResponseResult login(@RequestBody @Validated LoginRequest request, BindingResult bindingResult) throws JsonProcessingException {
         if (bindingResult.hasErrors()) {
             //如果实体类的注解验证不通过
             return ResponseResult.error(ResponseCodeEnum.PARAMETER_ILLEGAL.getErrorCode(), ResponseCodeEnum.PARAMETER_ILLEGAL.getMessage());
         }

         String username = request.getUsername();
         String password = request.getPassword();

         //根据用户名和密码去数据库验证 获取到userId
        String md5PW;
        if (password != null && !"".equals(password)) {
            md5PW = Md5.md5Encode(password);
        } else {
            md5PW = Md5.md5Encode(username);
        }
        request.setPassword(md5PW);
        JwtModel jwtModel = authUserService.isUser(request);
        if(jwtModel == null){
            return ResponseResult.error (401,"用户名或密码错误");
        }

         String token = JWTUtil.generateTokenByUserInfo(jwtModel.getUserId().toString(),objectMapper.writeValueAsString(jwtModel), secretKey);
         //  生成Token
         //String token = JWTUtil.generateTokenByUserId(userId, secretKey);

         //  生成刷新Token
         String refreshToken = UUID.randomUUID().toString().replace("-", "");

         //放入缓存,并给缓存设置过期时间为token有效时间;
         stringRedisTemplate.opsForHash().put(jwtModel.getUserId().toString(), "token", token);
         stringRedisTemplate.expire(jwtModel.getUserId().toString(), JWTUtil.TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

         LoginResponse loginResponse = new LoginResponse();
         loginResponse.setToken(token);
         loginResponse.setRefreshToken(refreshToken);
         loginResponse.setUsername(username);
         loginResponse.setUserId(jwtModel.getUserId().toString());

         return ResponseResult.success(loginResponse);

     }
        /**
        * 退出
        */
      @GetMapping("/logout")
      public ResponseResult logout() {
          JwtModel jwtModel = HttpServletUtils.getUserInfo();
          System.out.println("退出"+jwtModel.getUserId());
         String key = jwtModel.getUserId().toString();
         //清除缓存
         stringRedisTemplate.delete(key);
         return ResponseResult.success();
      }
}