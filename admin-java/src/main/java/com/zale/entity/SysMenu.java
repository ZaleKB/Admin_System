package com.zale.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Zale Junwei
 * @since 2021-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Parent ID，level 1 menu is 0
     */
    @NotNull(message = "parent cannot be null")
    private Long parentId;

    @NotBlank(message = "nam cannot be null")
    private String name;

    /**
     * menu URL
     */
    private String path;

    /**
     * separate with comma，：user:list,user:create)
     */
    @NotBlank(message = "code cannot be null")
    private String perms;

    private String component;

    /**
     * type     0：catalog   1：menu   2：buttom
     */
    @NotNull(message = "type cannot be null")
    private Integer type;

    /**
     * Icon
     */
    private String icon;

    /**
     * Order
     */
    @TableField("orderNum")
    private Integer orderNum;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();


}
