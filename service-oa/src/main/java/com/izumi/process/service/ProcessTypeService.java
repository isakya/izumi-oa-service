package com.izumi.process.service;

import com.izumi.model.process.ProcessType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author izumi
 * @since 2023-06-03
 */
public interface ProcessTypeService extends IService<ProcessType> {

    List<ProcessType> findProcessType();
}
