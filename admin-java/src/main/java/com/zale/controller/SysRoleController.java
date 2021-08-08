package com.zale.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zale.common.lang.Const;
import com.zale.common.lang.Result;
import com.zale.entity.SysRole;
import com.zale.entity.SysRoleMenu;
import com.zale.entity.SysUserRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 *
 * </p>
 *
 * @author Zale Junwei
 * @since 2021-07-02
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {

    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id) {

        SysRole sysRole = sysRoleService.getById(id);

        // fetch role related menu id
        List<SysRoleMenu> roleMenus = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        List<Long> menuIds = roleMenus.stream().map(p -> p.getMenuId()).collect(Collectors.toList());

        sysRole.setMenuIds(menuIds);
        return Result.succ(sysRole);
    }

    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/list")
    public Result list(String name) {

        Page<SysRole> pageData = sysRoleService.page(getPage(),
                new QueryWrapper<SysRole>()
                        .like(StrUtil.isNotBlank(name), "name", name)
        );

        return Result.succ(pageData);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result save(@Validated @RequestBody SysRole sysRole) {

        sysRole.setCreated(LocalDateTime.now());
        sysRole.setStatu(Const.STATUS_ON);

        sysRoleService.save(sysRole);
        return Result.succ(sysRole);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result update(@Validated @RequestBody SysRole sysRole) {

        sysRole.setUpdated(LocalDateTime.now());

        sysRoleService.updateById(sysRole);

        // update cache
        sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());

        return Result.succ(sysRole);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @Transactional
    public Result delete(@RequestBody Long[] ids) {

        sysRoleService.removeByIds(Arrays.asList(ids));

        // delete intermediate table
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", ids));
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id", ids));

        // cache deleted
        Arrays.stream(ids).forEach(id -> {
            sysUserService.clearUserAuthorityInfoByRoleId(id);
        });

        return Result.succ("");
    }

    @Transactional
    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:perm')")
    public Result perm(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {

        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();

        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);

            sysRoleMenus.add(roleMenu);
        });

        // first delete old then save current
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
        sysRoleMenuService.saveBatch(sysRoleMenus);

        // clear cache
        sysUserService.clearUserAuthorityInfoByRoleId(roleId);

        return Result.succ(menuIds);
    }

}
