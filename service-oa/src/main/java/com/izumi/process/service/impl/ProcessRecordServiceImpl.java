package com.izumi.process.service.impl;

import com.izumi.auth.service.SysUserService;
import com.izumi.model.process.ProcessRecord;
import com.izumi.model.system.SysUser;
import com.izumi.process.mapper.ProcessRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izumi.process.service.ProcessRecordService;
import com.izumi.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批记录 服务实现类
 * </p>
 *
 * @author izumi
 * @since 2023-06-11
 */
@Service
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord> implements ProcessRecordService {
    @Autowired
    private SysUserService sysUserService;
    @Override
    public void record(Long processId, Integer status, String description) {
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = sysUserService.getById(userId);
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUser(sysUser.getUsername());
        processRecord.setOperateUserId(userId);
        baseMapper.insert(processRecord);
    }
}
