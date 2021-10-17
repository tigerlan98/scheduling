package com.lh.scheduling.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lh.scheduling.config.CronTaskRegistrar;
import com.lh.scheduling.config.SchedulingRunnable;
import com.lh.scheduling.dao.SysJobDao;
import com.lh.scheduling.entity.SysJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: lanHu
 * @createTime: 2021/10/17 15:45
 */
@Slf4j
@Service
public class SysJobService {

    @Autowired
    SysJobDao sysJobDao;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    public List<SysJob> getJobsByStatus(int i) {
        QueryWrapper<SysJob> wrapper = new QueryWrapper<>();
        wrapper.eq("job_status",i);
        return sysJobDao.selectList(wrapper);
    }

    public List<SysJob> getAllJobs() {
        return sysJobDao.selectList(null);
    }

    public Boolean addJob(SysJob sysJob) {
        List<SysJob> all = sysJobDao.selectList(null);
        for (SysJob job : all) {
            if (job.equals(sysJob)) {
                //作业重复，添加失败
                return false;
            }
        }
        //添加
        int row = sysJobDao.insert(sysJob);
        if (row > 0) {
            //添加成功，如果新加的job是开启状态，就顺便开启
            SchedulingRunnable schedulingRunnable = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
            if (sysJob.getJobStatus() == 1) {
                cronTaskRegistrar.addCronTask(schedulingRunnable, sysJob.getCronExpression());
            }
            //添加成功
            return true;
        }
        return false;
    }

    public Boolean updateJob(SysJob sysJob) {
        log.info("updateJob() called with parameters => [sysJob = {}]",sysJob);
        int row = sysJobDao.updateById(sysJob);
        log.info("row = {}",row);
        if (row > 0) {
            SchedulingRunnable schedulingRunnable = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
            if (sysJob.getJobStatus() == 1) {
                cronTaskRegistrar.addCronTask(schedulingRunnable, sysJob.getCronExpression());
            } else {
                cronTaskRegistrar.removeCronTask(schedulingRunnable);
            }
            return true;
        }
        return false;
    }

    public Boolean deleteJobsById(Long id) {
        SysJob sysJob = sysJobDao.selectById(id);
        SchedulingRunnable schedulingRunnable = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
        cronTaskRegistrar.removeCronTask(schedulingRunnable);
        sysJobDao.deleteById(id);
        return true;
    }
}
