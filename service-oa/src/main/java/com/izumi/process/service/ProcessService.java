package com.izumi.process.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.izumi.model.process.Process;
import com.izumi.vo.process.ProcessFormVo;
import com.izumi.vo.process.ProcessQueryVo;
import com.izumi.vo.process.ProcessVo;

import java.util.Map;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author izumi
 * @since 2023-06-04
 */
public interface ProcessService extends IService<Process> {
    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    // 部署流程定义
    void deployByZip(String deployPath);

    void startUp(ProcessFormVo processFormVo);

    // 查询待处理的任务列表
    IPage<ProcessVo> findPending(Page<Process> pageParam);

    Map<String, Object> show(Long id);
}
