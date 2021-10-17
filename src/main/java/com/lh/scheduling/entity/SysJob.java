package com.lh.scheduling.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.lh.scheduling.dao.SysJobDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * @author: lanHu
 * @createTime: 2021/10/17 15:46
 *
 * 定时任务实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysJob extends Model<SysJob> {

    @TableId(value = "job_id",type = IdType.AUTO)
    private Long jobId;

    private String beanName;

    private String methodName;

    private String methodParams;

    private String cronExpression;

    private Integer jobStatus;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysJob sysJob = (SysJob) o;
        return Objects.equals(beanName, sysJob.beanName) && Objects.equals(methodName, sysJob.methodName) && Objects.equals(methodParams, sysJob.methodParams) && Objects.equals(cronExpression, sysJob.cronExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanName, methodName, methodParams, cronExpression);
    }
}
