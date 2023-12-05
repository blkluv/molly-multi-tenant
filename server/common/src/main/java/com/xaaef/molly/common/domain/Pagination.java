package com.xaaef.molly.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 返回分页
 * </p>
 *
 * @author WangChenChen
 * @version 1.0.1
 * @date 2021/10/14 14:00
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagination<T> {

    /**
     * 总共，有多少条
     */
    @Schema(description = "总数")
    private Long total;

    /**
     * 数据
     */
    @Schema(description = "数据列表")
    private List<T> list;

}

