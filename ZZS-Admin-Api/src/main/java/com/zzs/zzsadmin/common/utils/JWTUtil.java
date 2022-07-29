package com.zzs.zzsadmin.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JWTUtil {

    //私钥
    private static String mysecret = "zzs@$%!*";

    /**
     * 生成token
     * @param username 登录名
     * @param userJson
     * @param expiresTime 过期时长(毫秒)
     * @return
     */
    public static String CreateToken(String username, String userJson,Long expiresTime) {
        String token = null;
        Date expiresAt = new Date(System.currentTimeMillis() + expiresTime);
        token = JWT.create()
                .withIssuer("auth0")
                .withClaim("username", username)
                .withClaim("token", userJson)
                .withExpiresAt(expiresAt)
                // 使用了HMAC256加密算法。
                // mysecret是用来加密数字签名的密钥。
                .sign(Algorithm.HMAC256(mysecret));

        return token;
    }

    public static DecodedJWT deToken(final String token) {
        DecodedJWT jwt = null;
        // 使用了HMAC256加密算法。
        // mysecret是用来加密数字签名的密钥。
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(mysecret)).withIssuer("auth0").build();
        jwt = verifier.verify(token);

        return jwt;
    }

}
