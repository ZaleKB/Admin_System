package com.zale.service;

import com.zale.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *
 * </p>
 *
 * @author Zale Junwei
 * @since 2021-07-02
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    String getUserAuthorityInfo(Long userId);

    void clearUserAuthorityInfo(String username);

    void clearUserAuthorityInfoByRoleId(Long roleId);

    void clearUserAuthorityInfoByMenuId(Long menuId);
}
