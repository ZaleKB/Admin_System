package com.zale.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
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
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "name cannot be null")
    private String name;

    @NotBlank(message = "code cannot be null")
    private String code;

    /**
     * remark
     */
    private String remark;

    @TableField(exist = false)
    private List<Long> menuIds = new ArrayList<>();

}
