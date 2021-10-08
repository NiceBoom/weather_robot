package com.robot.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Jwt工具类
 */
public class JwtUtil {
    //有效期
    public static final long JWT_TTL = 3600000L;//60*60*1000 一小时
    //秘钥明文
    public static final String JWT_KEY = "weather";




    /**
     * 生成加密后的秘钥
     * @return
     */
    public static SecretKey generalKey(){
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;

    }

    /**
     * 解析jwt
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
