package com.izumi.process.service;

import com.izumi.model.process.ProcessRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批记录 服务类
 * </p>
 *
 * @author izumi
 * @since 2023-06-11
 */
public interface ProcessRecordService extends IService<ProcessRecord> {
    void record(Long processId, Integer status, String description);
}
