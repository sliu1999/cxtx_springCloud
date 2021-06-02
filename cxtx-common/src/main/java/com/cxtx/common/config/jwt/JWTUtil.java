package com.cxtx.common.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;


public class JWTUtil {
    public static final long TOKEN_EXPIRE_TIME = 3600 * 1000 * 1; //1h
     private static final String ISSUER = "admin";

     /**
       * 根据username生成Token
       * @param userId 用户标识（不一定是用户名，有可能是用户ID或者手机号什么的）
       * @param secretKey
       * @return
       */
     public static String generateTokenByUserId(String userId, String secretKey) {
         Algorithm algorithm = Algorithm.HMAC256(secretKey);
         Date now = new Date();
         Date expireTime = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

         String token = JWT.create()
                 .withIssuer(ISSUER) // issuer：jwt签发人
                 .withIssuedAt(now) // iat: jwt的签发时间
                 .withJWTId(userId) // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                 .withExpiresAt(expireTime)  //设置过期时间
                 .withClaim("userId", userId) // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                 .sign(algorithm);

         return token;

     }

    public static String generateTokenByUserInfo(String userId, String subject,String secretKey) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        String token = JWT.create()
                .withIssuer(ISSUER) // issuer：jwt签发人
                .withIssuedAt(now) // iat: jwt的签发时间
                .withJWTId(userId) // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .withExpiresAt(expireTime)  //设置过期时间
                .withClaim("userId", userId) // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .withSubject(subject) //// sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .sign(algorithm);

        return token;

    }

}