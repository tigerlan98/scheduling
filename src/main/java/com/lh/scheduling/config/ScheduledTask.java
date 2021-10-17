package com.lh.scheduling.config;

import java.util.concurrent.ScheduledFuture;

/**
 * @author: lanHu
 * @createTime: 2021/10/17 15:34
 */
public class ScheduledTask {

    volatile ScheduledFuture<?> future;

    public void cancel(){
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
