package com.izumi.auth.controller;

import com.izumi.auth.service.SysRoleService;
import com.izumi.common.result.Result;
import com.izumi.model.system.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    // 注入service
    @Autowired
    private SysRoleService sysRoleService;
    // http:localhost:8800/admin/system/sysRole/findAll
    // 查询所有的角色
    @GetMapping("/findAll")
    public Result findAll() {
        // 调用service的方法
        List<SysRole> list = sysRoleService.list();
        return  Result.ok(list);
    }
}
