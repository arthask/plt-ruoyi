package com.ruoyi.system.gencode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 卡包闪卡关系表
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("package_card_ref")
public class PackageCardRef implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 记录uuid
     */
    @TableField("uuid")
    private String uuid;

    /**
     * 卡包uuid
     */
    @TableField("package_uuid")
    private String packageUuid;

    /**
     * 闪卡uuid
     */
    @TableField("card_uuid")
    private String cardUuid;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
