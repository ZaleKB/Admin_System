package com.zale.service;

import com.zale.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Zale Junwei
 * @since 2021-07-02
 */
public interface SysRoleService extends IService<SysRole> {
    List<SysRole> listRolesByUserId (Long userId);
}
