package com.zale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zale.entity.SysRole;
import com.zale.mapper.SysRoleMapper;
import com.zale.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Zale Junwei
 * @since 2021-07-02
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Override
    public List<SysRole> listRolesByUserId(Long userId) {

        List<SysRole> sysRoles = this.list(new QueryWrapper<SysRole>()
                .inSql("id", "select role_id from sys_user_role where user_id = " + userId));

        return sysRoles;
    }
}
