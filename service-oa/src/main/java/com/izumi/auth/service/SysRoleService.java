package com.izumi.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.izumi.model.system.SysRole;
import com.izumi.vo.system.AssginRoleVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    Map<String, Object> finRoleDataByUserId(Long userId);

    void doAssign(AssginRoleVo assginRoleVo);
}
