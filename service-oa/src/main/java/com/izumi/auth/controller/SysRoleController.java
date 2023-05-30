package com.izumi.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.izumi.auth.service.SysRoleService;
import com.izumi.common.result.Result;
import com.izumi.model.system.SysRole;

import com.izumi.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    // 注入service
    @Autowired
    private SysRoleService sysRoleService;


    // http:localhost:8800/admin/system/sysRole/findAll
    // 查询所有的角色
    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result findAll() {
        // 调用service的方法
        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }

    // 条件分页查询
    // page 当前页 limit 每页显示记录数
    // SysRoleQueryVo条件对象
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page, @PathVariable Long limit, SysRoleQueryVo sysRoleQueryVo) {
        // 调用service的方法实现
        // 1. 创建一个Page对象，传递分页相关参数
        Page<SysRole> pageParam = new Page<>(page, limit);
        // 2. 封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)) {
            // 封装
            wrapper.like(SysRole::getRoleName, roleName);
        }
        // 3. 调用方法实现
        sysRoleService.page(pageParam, wrapper);

        return Result.ok(pageParam);
    }
}
