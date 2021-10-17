package com.lh.scheduling.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: lanHu
 * @createTime: 2021/10/17 16:11
 */
@Slf4j
@Component
public class TaskDemo {

    public void taskWithParams(String params){
        log.info("执行带参数的定时任务... : {}",params);
    }

    public void taskWithoutParams(String params){
        log.info("执行不带参数的定时任务...");
    }

}
