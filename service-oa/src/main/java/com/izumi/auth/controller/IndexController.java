package com.izumi.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.izumi.auth.service.SysMenuService;
import com.izumi.auth.service.SysUserService;
import com.izumi.common.config.exception.IzumiException;
import com.izumi.common.jwt.JwtHelper;
import com.izumi.common.result.Result;
import com.izumi.common.utils.MD5;
import com.izumi.model.system.SysUser;
import com.izumi.vo.system.LoginVo;
import com.izumi.vo.system.RouterVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台登录登出
 * </p>
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    // login
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        // 1 获取输入用户名和密码
        String username = loginVo.getUsername();

        // 2 根据用户名查询数据库
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(wrapper);

        // 3 用户信息是否存在
        if(sysUser == null) {
            throw new IzumiException(201, "用户不存在");
        }

        // 4 判断密码是否正确
        // 数据库存的密码（MD5）
        String password_db = sysUser.getPassword();
        // 获取输入的密码
        String password_input = MD5.encrypt(loginVo.getPassword());
        if(!password_input.equals(password_db)) {
            throw new IzumiException(201, "用户名或密码不正确");
        }
        // 5 判断用户是否被禁用
        if(sysUser.getStatus().intValue() == 0) {
            throw new IzumiException(201, "用户被禁用，请联系管理员");
        }

        // 6 使用jwt根据用户id和用户名称生成token字符串
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());

        // 7 返回
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return Result.ok(map);
    }

    // info
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        //1 从请求头获取用户信息（获取请求头token字符串）
        String token = request.getHeader("header");
        //2 从token字符串获取用户id 或者 用户名称
        Long userId = JwtHelper.getUserId(token);
        //3 根据用户id查询数据库，把用户信息获取出来
        SysUser sysUser = sysUserService.getById(userId);
        //4 根据用户id获取用户可以操作的菜单列表
        // 查询数据库生成路由结构，进行显示
        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);

        //5 根据用户id获取用户可以操作菜单列表
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);
        
        //6 返回相应的数据


        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", sysUser.getName());
        map.put("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");

        // 返回用户可以操作菜单
        map.put("routers", routerList);
        // 返回用户可以操作按钮
        map.put("buttons", permsList);
        return Result.ok(map);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }
}
