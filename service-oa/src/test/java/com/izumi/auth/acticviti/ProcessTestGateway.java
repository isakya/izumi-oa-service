package com.izumi.auth.acticviti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排他网关
 */
@SpringBootTest
public class ProcessTestGateway {
    // 注入RepositoryService
    @Autowired
    private RepositoryService repositoryService;

    // 注入
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    // 1. 部署流程定义
    @Test
    public void deployProcess01() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia002.bpmn20.xml")
                .addClasspathResource("process/qingjia002.png")
                .name("请假申请流程002")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startUpProcess01() {
        Map<String, Object> map = new HashMap<>();
        map.put("day","2");
        //创建流程实例,我们需要知道流程定义的key
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia002", map);
        //输出实例的相关信息
        System.out.println("流程实例id：" + processInstance.getId());
    }

    // 查询个人的代办任务--zhao6
    @Test
    public void findTaskList() {
        String assignee = "xiaocui";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
        for(Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    /**
     * 处理任务
     */
    @Test
    public void completedTask(){
        // 查询负责人需要处理的任务，返回一条
        Task task = taskService.createTaskQuery()
                .taskAssignee("xiaocui")  //要查询的负责人
                .singleResult();//返回一条

        //处理任务,参数：任务id
        taskService.complete(task.getId());
    }
}
