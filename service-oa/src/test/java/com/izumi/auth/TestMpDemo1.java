package com.izumi.auth;

import com.izumi.auth.mapper.SysRoleMapper;
import com.izumi.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestMpDemo1 {
    // 注入
    @Autowired
    private SysRoleMapper mapper;

    // 查询所有记录
    @Test
    public void getAll() {
        List<SysRole> list = mapper.selectList(null);
        System.out.println(list);
    }

    // 添加操作
    @Test
    public void add() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员1");
        sysRole.setRoleCode("role1");
        sysRole.setDescription("角色管理员1");

        int rows = mapper.insert(sysRole);
        // 影响的行数
        System.out.println(rows);
        System.out.println(sysRole.getId());
    }

    // 修改操作
    // 添加操作
    @Test
    public void update() {
        // 根据id查询
        SysRole role = mapper.selectById(10);
        // 设置修改值
        role.setRoleName("izumi角色管理员");
        // 调用方法实现最终修改
        int rows = mapper.updateById(role);
        // 影响的行数
        System.out.println(rows); // id自动回填
    }

    // 删除操作
    @Test
    public void deleteId() {
        int rows = mapper.deleteById(10);
    }

    // 批量删除
    @Test
    public void testDeleteBatchIds() {
        int result = mapper.deleteBatchIds(Arrays.asList(1, 1));
        System.out.println(result);
    }
}
