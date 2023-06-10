package com.izumi.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izumi.model.process.ProcessTemplate;
import com.izumi.model.process.ProcessType;
import com.izumi.process.mapper.ProcessTypeMapper;
import com.izumi.process.service.ProcessTemplateService;
import com.izumi.process.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private ProcessTemplateService processTemplateService;

    @Override
    public List<ProcessType> findProcessType() {
        // 1 查询所有审批分类，返回list集合
        List<ProcessType> processTypeList = baseMapper.selectList(null);
        // 2 遍历返回所有审批分类list集合
        for (ProcessType processType : processTypeList) {
            // 3 得到每个审批分类，根据审批分类id查询对应审批模板
            Long id = processType.getId();
            LambdaQueryWrapper<ProcessTemplate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessTemplate::getProcessTypeId, id);
            List<ProcessTemplate> processTemplateList = processTemplateService.list(wrapper);
            // 4 根据审批分类id查询对应审批模板数据（list），封装到每个审批分类对象里面
            processType.setProcessTemplateList(processTemplateList);
        }
        return processTypeList;
    }
}
