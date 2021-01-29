package com.code_template.annotation;

import com.auth0.jwt.interfaces.Claim;
import com.code_template.util.JWTUtil;
import com.code_template.util.ResponseCode;
import com.code_template.util.ReturnObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect
@Component
public class AuditAspect {
    @Autowired
    JWTUtil jwtUtil;

    @Pointcut("@annotation(com.code_template.annotation.Audit)")
    public void auditAspect(){

    }

    @Before("auditAspect()")
    public void Before(JoinPoint joinPoint) throws Throwable {
        // 1. 拿到token 如果token为空 则直接验证失败
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(JWTUtil.AUTHORIZATION);
        System.out.println(token);
        if(token == null){
            return;
//            return new ReturnObject(ResponseCode.TOKEN_NOT_EXIST);
        }
        // 2. 解token 将信息写在request中
        Map<String, Claim> claimMap = jwtUtil.verifyTokenAndGetClaim(token);
        request.setAttribute("user_id", claimMap.get("id").asInt());
        request.setAttribute("depart_id", claimMap.get("depart").asInt());
    }
}
