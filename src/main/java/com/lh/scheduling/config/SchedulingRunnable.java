package com.lh.scheduling.config;

import cn.hutool.core.util.StrUtil;
import com.lh.scheduling.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author: lanHu
 * @createTime: 2021/10/17 15:20
 *
 * 每一个定时任务对应一个子线程
 */
@Slf4j
public class SchedulingRunnable implements Runnable{

    private String beanName;

    private String methodName;

    private String params;

    private Object targetBean;

    private Method method;

    public SchedulingRunnable(String beanName, String methodName) {
        this(beanName,methodName,null);
    }

    public SchedulingRunnable(String beanName, String methodName, String params) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
        init();
    }

    private void init() {
        try {
            targetBean = SpringContextHolder.getBean(beanName);
            if (StringUtils.hasText(params)){
                // 只有一个参数，并且参数类型是String
                method = targetBean.getClass().getDeclaredMethod(methodName,String.class);
            }else {
                method = targetBean.getClass().getDeclaredMethod(methodName);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        // 使方法可访问
        ReflectionUtils.makeAccessible(method);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingRunnable that = (SchedulingRunnable) o;
        return Objects.equals(beanName, that.beanName) && Objects.equals(methodName, that.methodName) && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanName, methodName, params);
    }

    @Override
    public void run() {
        log.info("定时任务开始执行 - bean:{},方法:{},参数:{}",beanName,method,params);
        long startTime = System.currentTimeMillis();
        try {
            if (StringUtils.hasText(params)) {
                method.invoke(targetBean,params);
            }else {
                method.invoke(targetBean);
            }
        } catch (Exception e) {
           log.error(StrUtil.format("定时任务执行异常 - ,方法:{},方法:{},参数:{}",beanName,method,params));
        }
        long endTime = System.currentTimeMillis();
        log.info("定时任务开始执行 - bean:{},方法:{},参数:{},耗时:{}ms",beanName,method,params,endTime-startTime);
    }
}
