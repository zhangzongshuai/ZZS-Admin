package com.zzs.zzsadmin.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JWTUtil {

    //私钥
    private static String mysecret = "zzs@$%!*";

    public static String CreateToken(String username, String userJson) {
        String token = null;
        //有效期 12小时
        Date expiresAt = new Date(System.currentTimeMillis() + 12 * 60L * 60L * 1000L);
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
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(mysecret)).withIssuer("auth0").build();// Reusable
        // verifier
        // instance
        jwt = verifier.verify(token);

        return jwt;
    }

}
