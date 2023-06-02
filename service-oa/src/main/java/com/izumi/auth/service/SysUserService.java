package com.izumi.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.izumi.model.system.SysUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author izumi
 * @since 2023-05-31
 */
public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long id, Integer status);

    SysUser getByUsername(String username);
}
