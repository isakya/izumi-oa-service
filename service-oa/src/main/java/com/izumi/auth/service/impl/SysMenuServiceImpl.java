package com.izumi.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izumi.auth.mapper.SysMenuMapper;
import com.izumi.auth.service.SysMenuService;
import com.izumi.auth.utils.MenuHelper;
import com.izumi.model.system.SysMenu;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author izumi
 * @since 2023-05-31
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> findNodes() {
        // 1 查询所有菜单数据
        List<SysMenu> menuList = baseMapper.selectList(null);
        // 2 构建树形结构
         List<SysMenu> resultList = MenuHelper.buildTree(menuList);
        return resultList;
    }
}
