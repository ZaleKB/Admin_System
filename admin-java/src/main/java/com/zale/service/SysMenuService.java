package com.zale.service;

import com.zale.common.dto.SysMenuDto;
import com.zale.entity.SysMenu;
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
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenuDto> getCurrentUserNav();

    List<SysMenu> tree();
}
