package com.izumi.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izumi.auth.mapper.SysRoleMapper;
import com.izumi.auth.service.SysRoleService;
import com.izumi.model.system.SysRole;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

}
