package com.izumi.process.controller.api;

import com.izumi.common.result.Result;
import com.izumi.model.process.ProcessTemplate;
import com.izumi.model.process.ProcessType;
import com.izumi.process.service.ProcessTemplateService;
import com.izumi.process.service.ProcessTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "审批流管理")
@RestController
@RequestMapping(value = "/admin/process")
@CrossOrigin
public class ProcessApiController {
    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private ProcessTemplateService processTemplateService;

    // 获取审批模板数据
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result getProcessTemplate(@PathVariable Long processTemplateId) {
        ProcessTemplate processTemplate = processTemplateService.getById(processTemplateId);
        return Result.ok(processTemplate);
    }

    // 查询所有审批分类和每个分类所有的审批模板
    @GetMapping("findProcessType")
    public Result findProcessType() {
       List<ProcessType> list = processTypeService.findProcessType();

        return Result.ok(list);
    }


}
