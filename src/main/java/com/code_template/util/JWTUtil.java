package com.code_template.util;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.JWT;
import com.code_template.model.po.User;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class JWTUtil {
    static final String SECRET = "code_template";
    static final String ISSUER = "oniya24";
    static final String SUBJECT = "pro_token";
    public static final String AUTHORIZATION = "authorization";
    static final int EXPIRETIME = 3600;

    public String createToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            Calendar cal = new GregorianCalendar();
            Date nowDate = new Date();
            cal.setTime(nowDate);
            cal.add(Calendar.SECOND, EXPIRETIME);
            Date expireDate = cal.getTime();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            String token = JWT.create()
                    .withHeader(map)
                    .withClaim("id",user.getId())
                    .withClaim("depart",user.getDepart())
                    .withIssuer(ISSUER)
                    .withSubject(SUBJECT)
                    .withIssuedAt(nowDate)
                    .withExpiresAt(expireDate)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            exception.printStackTrace();
        }
        return null;
    }
//
    public Map verifyTokenAndGetClaim(String token) {
        try {
            // 获得HS256加密的密钥
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            // build一个验证器
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                                .withIssuer(ISSUER)
                                .withSubject(SUBJECT)
                                .build();
            DecodedJWT jwt = jwtVerifier.verify(token);
            Map<String , Claim> claimMap = jwt.getClaims();
            return claimMap;
        }catch (JWTCreationException exception) {
            return null;
        }
    }
}
