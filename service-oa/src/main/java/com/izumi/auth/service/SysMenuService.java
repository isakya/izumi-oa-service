package com.izumi.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.izumi.model.system.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author izumi
 * @since 2023-05-31
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();
}