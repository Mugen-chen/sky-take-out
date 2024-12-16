package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("公共字段自动填充...");

        // 1.确定当前SQL操作是insert还是update
            // (a)获得方法签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // (b)获得方法上的注解
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
            // (c)获得注解中的操作类型
        OperationType operationType = annotation.value();

        // 2.获取当前SQL操作的参数
        Object[] args = joinPoint.getArgs();

        if (args == null || args.length == 0) {
            return;
        }

        // 3.获得实体对象
        Object entity = args[0];

        // 4.准备赋值的数据
        LocalDateTime time = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

        // 5.为公共字段赋值
        if (operationType == OperationType.INSERT) {
            try {
                // 通过反射获取当前实体对象的set()方法
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射为对象属性赋值
                setCreateTime.invoke(entity, time);
                setUpdateTime.invoke(entity, time);
                setCreateUser.invoke(entity, id);
                setUpdateUser.invoke(entity, id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {

            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity, time);
                setUpdateUser.invoke(entity, id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
