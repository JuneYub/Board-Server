package com.campus.boardserver.boardserver.aop;

import com.campus.boardserver.boardserver.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
@Log4j2
public class LoginCheckAspect {

    @Around("@annotation(com.campus.boardserver.boardserver.aop.LoginCheck) && @ annotation(loginCheck)")
    public Object adminLoginCheck(ProceedingJoinPoint joinPoint, LoginCheck loginCheck) throws Throwable {
        HttpSession session = (HttpSession)((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
        String id = null;
        int idIndex = 0;

        String userType = loginCheck.type().toString();
        switch (userType) {
            case "ADMIN": {
                id = SessionUtil.getLoginAdminId(session);
                break;
            }
            case "USER": {
                id = SessionUtil.getLoginMemberId(session);
                break;
            }
        }
        if (id == null) {
            log.error(joinPoint.toString() + "accountName : " + id);
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인한 ID값을 확인해주세요") {};
        }

        Object[] modifiedArgs = joinPoint.getArgs();

        if(joinPoint != null)
            modifiedArgs[idIndex] = id;

        return joinPoint.proceed(modifiedArgs);
    }
}
