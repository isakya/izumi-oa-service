package com.izumi.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izumi.auth.mapper.SysMenuMapper;
import com.izumi.auth.service.SysMenuService;
import com.izumi.auth.service.SysRoleMenuService;
import com.izumi.auth.utils.MenuHelper;
import com.izumi.common.config.exception.IzumiException;
import com.izumi.model.system.SysMenu;
import com.izumi.model.system.SysRole;
import com.izumi.model.system.SysRoleMenu;
import com.izumi.vo.system.AssginMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenu> findNodes() {
        // 1 查询所有菜单数据
        List<SysMenu> menuList = baseMapper.selectList(null);
        // 2 构建树形结构
         List<SysMenu> resultList = MenuHelper.buildTree(menuList);
        return resultList;
    }
    
    // 删除菜单
    @Override
    public void removeMenuById(Long id) {
        // 判断当前菜单是否有下一层菜单
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysMenu::getParentId, id);
        Integer count = baseMapper.selectCount(lambdaQueryWrapper);
        if(count > 0) {
            throw new IzumiException(201, "菜单不能删除");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        // 1 查询所有菜单 - 添加条件 status = 1 表示菜单可用
        LambdaQueryWrapper<SysMenu> wrapperSysMenu = new LambdaQueryWrapper<>();
        wrapperSysMenu.eq(SysMenu::getStatus, 1);
        List<SysMenu> allSysMenuList = baseMapper.selectList(wrapperSysMenu);
        // 2 根据角色id roleId查询 角色菜单关系表里面 角色id对应所有的菜单id
        LambdaQueryWrapper<SysRoleMenu> wrapperSysRoleMenu = new LambdaQueryWrapper<>();
        wrapperSysRoleMenu.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(wrapperSysRoleMenu);

        // 3 根据获取菜单id，获取对应菜单对象
        List<Long> menuIdList = sysRoleMenuList.stream().map(c -> c.getMenuId()).collect(Collectors.toList());

        // 3.1 拿着菜单id 和所有菜单集合里面id进行比较，如果相同则进行封装
        allSysMenuList.stream().forEach(item -> {
            if(menuIdList.contains(item.getId())) {
                item.setSelect(true);
            } else {
                item.setSelect(false);
            }
        });

        // 4 返回规定树型格式菜单列表
        List<SysMenu> sysMenuList = MenuHelper.buildTree(allSysMenuList);

        return sysMenuList;
    }

    // 角色分配菜单
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        // 1 根据角色id 删除菜单角色表里面已分配的菜单
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, assginMenuVo.getRoleId());
        sysRoleMenuService.remove(wrapper);

        // 2 从参数里面获取角色新分配菜单id列表，进行遍历，把每个id数据添加到菜单角色表
        for(Long menuId : assginMenuVo.getMenuIdList()) {
            if(StringUtils.isEmpty(menuId)) {
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }
}
