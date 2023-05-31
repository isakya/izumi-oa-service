package com.izumi.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izumi.auth.mapper.SysRoleMapper;
import com.izumi.auth.service.SysRoleService;
import com.izumi.auth.service.SysUserRoleService;
import com.izumi.auth.service.SysUserService;
import com.izumi.model.system.SysRole;
import com.izumi.model.system.SysUserRole;
import com.izumi.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
   @Autowired
   private SysUserRoleService sysUserRoleService;
    // 1. 查询所有角色 和 当前用户所属角色
    @Override
    public Map<String, Object> finRoleDataByUserId(Long userId) {
        // 1 查询所有角色，返回list集合
        List<SysRole> allRoleList = baseMapper.selectList(null);
        // 2 根据userid查询角色用户关系表，查询userid对应所有角色id
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper);
        // 从查询出来的用户id对应角色list集合，获取所有角色id
        List<Long> existRoleIdList = existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());
        // 3 根据查询所有角色id，找到对应角色信息
        // 根据角色id到所有的角色的list集合进行比较
        List<SysRole> assignRoleList = new ArrayList<>();
        for(SysRole sysRole : allRoleList) {
            // 比较
            if(existRoleIdList.contains(sysRole.getId())) {
                assignRoleList.add(sysRole);
            }
        }

        // 4 把得到两部分数据封装map集合，返回
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList", assignRoleList);
        roleMap.put("allRolesList", allRoleList);
        return roleMap;
    }
    // 2. 为用户分配角色
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        // 把用户之前分配的角色数据删除，用户角色关系表里面，根据userid删除
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);

        // 重新分配
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for(Long roleId : roleIdList) {
            if(StringUtils.isEmpty(roleId)) {
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }
    }
}
