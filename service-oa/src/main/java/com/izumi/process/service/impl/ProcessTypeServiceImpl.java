package com.izumi.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izumi.model.process.ProcessType;
import com.izumi.process.mapper.ProcessTypeMapper;
import com.izumi.process.service.ProcessTypeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author izumi
 * @since 2023-06-03
 */
@Service
public class ProcessTypeServiceImpl extends ServiceImpl<ProcessTypeMapper, ProcessType> implements ProcessTypeService {

}
