package com.xaaef.molly.perms.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/1/6 14:16
 */

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuMetaVO implements java.io.Serializable {

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标！")
    private String icon;

    /**
     * 菜单标题
     */
    @Schema(description = "菜单标题！")
    private String title;

}
