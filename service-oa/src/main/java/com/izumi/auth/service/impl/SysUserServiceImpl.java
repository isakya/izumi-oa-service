package com.izumi.auth.service.impl;


import com.izumi.auth.mapper.SysUserMapper;
import com.izumi.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izumi.model.system.SysUser;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author izumi
 * @since 2023-05-31
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public void updateStatus(Long id, Integer status) {
        // 先根据userid查询用户对象
        SysUser sysUser = baseMapper.selectById(id);
        // 设置修改状态值
        sysUser.setStatus(status);
        // 调用方法进行修改
        baseMapper.updateById(sysUser);
    }
}
