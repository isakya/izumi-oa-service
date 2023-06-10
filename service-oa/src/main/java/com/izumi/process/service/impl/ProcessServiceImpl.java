package com.izumi.process.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.izumi.auth.service.SysUserService;
import com.izumi.model.process.Process;
import com.izumi.model.process.ProcessTemplate;
import com.izumi.model.system.SysUser;
import com.izumi.process.mapper.ProcessMapper;
import com.izumi.process.service.ProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izumi.process.service.ProcessTemplateService;
import com.izumi.security.custom.LoginUserInfoHelper;
import com.izumi.vo.process.ProcessFormVo;
import com.izumi.vo.process.ProcessQueryVo;
import com.izumi.vo.process.ProcessVo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author izumi
 * @since 2023-06-04
 */
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProcessTemplateService processTemplateService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> page = baseMapper.selectPage(pageParam, processQueryVo);
        return page;
    }

    // 部署流程定义
    @Override
    public void deployByZip(String deployPath) {
        // 定义zip输入流
        InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(deployPath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    @Override
    public void startUp(ProcessFormVo processFormVo) {
        // 1 根据当前用户id获取用户信息
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());

        // 2 根据审批模板id把模板信息查询
        ProcessTemplate processTemplate = processTemplateService.getById(processFormVo.getProcessTemplateId());

        // 3 保存提交的审批信息到业务表，oa_process
        Process process = new Process();
        BeanUtils.copyProperties(processFormVo, process);
        process.setStatus(1); // 审批中
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
        baseMapper.insert(process);

        // 4 启动流程实例 - RuntimeService
        // 4.1 流程定义key
        String processDefinitionKey = processTemplate.getProcessDefinitionKey();
        // 4.2 业务key  processId
        String businessKey = String.valueOf(process.getId());
        // 4.3 流程参数form表单json数据，转换map集合
        String formValues = processFormVo.getFormValues();
        // formData
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formData = jsonObject.getJSONObject("formData");
        // 变量formData得到内容，封装map集合
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("data", map);
        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        // 5 查询下一个审批人
        // 审批人可能有多个
        List<Task> taskList = this.getCurrentTaskList(processInstance.getId());
        ArrayList<String> nameList = new ArrayList<>();
        for (Task task : taskList) {
            String assigneeName = task.getAssignee();
            SysUser user = sysUserService.getByUsername(assigneeName);
            String name = sysUser.getName();
            nameList.add(name);
            //TODO 6 推送消息

        }
        process.setProcessInstanceId(processInstance.getId());
        process.setDescription("等待" + StringUtils.join(nameList.toArray(), ",") + "审批");
        // 7 业务和流程关联 更新oa_process
        baseMapper.updateById(process);
    }

    // 当前任务列表
    private List<Task> getCurrentTaskList(String id) {
        List<Task> taskList = taskService.createTaskQuery().processDefinitionId(id).list();
        return taskList;
    }
}
