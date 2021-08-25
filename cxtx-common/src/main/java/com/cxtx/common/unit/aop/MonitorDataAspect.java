package com.cxtx.common.unit.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class MonitorDataAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Pointcut("@annotation(com.cxtx.common.unit.aop.MonitorData)")
    public void asAnnotation() {}


    /**
     * 返回通知，可拿到方法返回值
     * @param joinPoint
     * @param monitorData
     * @param result
     */
    @AfterReturning(value = "asAnnotation() && @annotation(monitorData)",returning = "result")
    public void after(JoinPoint joinPoint, MonitorData monitorData,Object result) {
        try {

            String apiPath = monitorData.apiPath();
            String type = monitorData.type();
            Object[] args = joinPoint.getArgs();
            Object arg = args[0];

            HashMap params = new HashMap(4);
            if (type.equals("out")) {
                ResponseEntity responseEntity = (ResponseEntity) result;
                HashMap body = (HashMap) responseEntity.getBody();
                Object object = body.get("data");

                int recordNum = calculateDataSize(object);
                long dateSize = ObjectSizeCalculator.getObjectSize(object)/1024;
                params.put("recordNum",recordNum);
                params.put("dataSize",dateSize);
            } else {
                int recordNum = calculateDataSize(arg);
                long dateSize = ObjectSizeCalculator.getObjectSize(arg)/1024;
                params.put("recordNum",recordNum);
                params.put("dataSize",dateSize);
            }
            params.put("apiPath", apiPath);
            params.put("jointType", type);
        }catch (Exception e){
            log.info("切面监测接口出现异常:apiPath:"+ monitorData.apiPath() + ",Exception：" + e);
        }

    }

    @AfterThrowing(value = "asAnnotation() && @annotation(monitorData)",throwing = "e")
    public void afterThrowing(JoinPoint jp,MonitorData monitorData,Exception e){
        System.out.print("在目标方法抛异常时条用，可精确异常类型");
        String name = jp.getSignature().getName();
        System.out.print(name+"方法抛异常了"+e.getMessage());
    }

    /**
     * 计算数据量KB
     * @param object
     * @return
     */
    public int calculateDataSize(Object object){
        int recordNum = 0;
        if(object instanceof List){
            List list = (List)object;
            recordNum = list.size();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()){

                recordNum += calculateDataSize(iterator.next());

            }
        }
        if(object instanceof Map){
            Map<String,Object> map = (Map<String,Object>)object;

            for(Map.Entry<String, Object> entry : map.entrySet()){

                Object mapValue = entry.getValue();
                recordNum += calculateDataSize(mapValue);
            }
        }

        return recordNum;
    }
}
