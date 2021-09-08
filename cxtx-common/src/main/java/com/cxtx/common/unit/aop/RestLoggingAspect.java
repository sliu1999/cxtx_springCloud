package com.cxtx.common.unit.aop;

import com.cxtx.common.domain.SysRestLog;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.common.unit.aop.db.SysRestLogQueue;
import com.cxtx.common.unit.aop.db.SysRestLogRunner;
import org.apache.ibatis.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
public class RestLoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static String UNKNOWN = "unknown";
    @Autowired
    private SysRestLogQueue sysRestLogQueue;


    @Pointcut("within(com.cxtx.*.web.rest..*)")
    public void loggingPointcut() {
    }

    @Around("loggingPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        SysRestLog restLog = new SysRestLog();

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String sessionId = request.getSession(true).getId();
                String user = HttpServletUtils.getUserInfo().getUserId();
                String url = request.getRequestURL().toString();
                String httpMethod = request.getMethod();
                String ip = getCliectIp(request);
                String userAgent = request.getHeader("User-Agent");
                String className = joinPoint.getSignature().getDeclaringTypeName();
                String classMethod = joinPoint.getSignature().getName();
                if (this.log.isInfoEnabled()) {
                    this.log.info("-------- 接口调用 --------");
                    this.log.info("SESSION-ID : {} ", sessionId);
                    this.log.info("User : {} ", user);
                    this.log.info("URL : {} ", url);
                    this.log.info("HTTP-METHOD : {} ", httpMethod);
                    this.log.info("IP : {} ", ip);
                    this.log.info("User-Agent : {} ", userAgent);
                    this.log.info("CLASS-METHOD : {} ", className + "." + classMethod);
                    this.log.info("ARGS : {} ", joinPoint.getArgs());
                    this.log.info("-------------------------");
                    LogFactory.useLog4J2Logging();
                }

                if (SysRestLogRunner.isLaunch()) {
                    restLog.setOperate(className);
                    //restLog.setTokenUser(SecurityUtils.getCurrentTokenUser());
                    restLog.setInfoUser(HttpServletUtils.getUserInfo().getUserId());
                    restLog.setDescription("参数：" + Arrays.toString(joinPoint.getArgs()));
                    restLog.setClassMethod(classMethod);
                    restLog.setOperateTimestamp(System.currentTimeMillis());
                    restLog.setIpAddr(ip);
                    restLog.setUserAgent(userAgent);
                    restLog.setUrl(url);
                    restLog.setHttpMethod(httpMethod);
                    restLog.setSessionId(sessionId);
                }
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        }

        try {
            Object result = joinPoint.proceed();
            if (SysRestLogRunner.isLaunch()) {
                restLog.setDescription(restLog.getDescription() + "/r/n结果：" + result);
                this.sysRestLogQueue.add(restLog);
            }

            return result;
        } catch (Exception var14) {
            var14.printStackTrace();
            if (SysRestLogRunner.isLaunch()) {
                restLog.setDescription(restLog.getDescription() + "/r/n错误：" + var14.getMessage());
                this.sysRestLogQueue.add(restLog);
            }

            throw var14;
        }
    }

    private static String getCliectIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim() == "" || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.trim() == "" || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.trim() == "" || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        String[] arr = ip.split(",");
        String[] var3 = arr;
        int var4 = arr.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String str = var3[var5];
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }

        return ip;
    }
}

