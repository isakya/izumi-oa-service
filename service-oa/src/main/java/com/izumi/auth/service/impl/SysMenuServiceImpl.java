package com.izumi.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izumi.auth.mapper.SysMenuMapper;
import com.izumi.auth.service.SysMenuService;
import com.izumi.auth.utils.MenuHelper;
import com.izumi.common.config.exception.IzumiException;
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
}
