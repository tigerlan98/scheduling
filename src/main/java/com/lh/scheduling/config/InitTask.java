package com.lh.scheduling.config;

import cn.hutool.core.collection.CollUtil;
import com.lh.scheduling.entity.SysJob;
import com.lh.scheduling.service.SysJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: lanHu
 * @createTime: 2021/10/17 15:44
 */
@Component
public class InitTask implements CommandLineRunner {

    @Autowired
    CronTaskRegistrar cronTaskRegistrar;

    @Autowired
    SysJobService sysJobService;

    @Override
    public void run(String... args) throws Exception {
        List<SysJob> list = sysJobService.getJobsByStatus(1);

        if (CollUtil.isEmpty(list)){
            return;
        }

        for (SysJob sysJob : list) {
            cronTaskRegistrar.addCronTask(new SchedulingRunnable(sysJob.getBeanName(),
                    sysJob.getMethodName(),
                    sysJob.getMethodParams()),
                    sysJob.getCronExpression());
        }
    }
}
