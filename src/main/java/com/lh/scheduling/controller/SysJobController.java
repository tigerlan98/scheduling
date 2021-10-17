package com.lh.scheduling.controller;

import com.lh.scheduling.entity.SysJob;
import com.lh.scheduling.service.SysJobService;
import com.lh.scheduling.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: lanHu
 * @createTime: 2021/10/17 16:59
 */
@RestController
@RequestMapping("/jobs")
public class SysJobController {

    @Autowired
    SysJobService sysJobService;

    @GetMapping
    public R getAllJobs() {
        return R.ok(sysJobService.getAllJobs());
    }

    @PostMapping
    public R addJob(@RequestBody SysJob sysJob) {
        Boolean flag = sysJobService.addJob(sysJob);
        if (flag) {
            return R.ok("作业添加成功");
        }
        return R.failed("作业重复，添加失败");
    }

    @PutMapping
    public R updateJob(@RequestBody SysJob sysJob) {
        Boolean flag = sysJobService.updateJob(sysJob);
        if (flag) {
            return R.ok("作业更新成功");
        }
        return R.failed("作业更新失败");
    }

    @DeleteMapping("/")
    public R deleteJobs(Long id) {
        Boolean flag = sysJobService.deleteJobsById(id);
        if (flag) {
            return R.ok("删除成功");
        }
        return R.failed("删除失败");
    }
}
