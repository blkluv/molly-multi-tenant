package com.xaaef.molly.core.tenant.props;

import com.xaaef.molly.core.tenant.enums.DbStyle;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 多租户全局配置
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 11:53
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "multi.tenant")
public class MultiTenantProperties {

    /**
     * 数据库名称前缀
     */
    private String prefix = "molly_";


    /**
     * 默认租户ID
     */
    private String defaultTenantId = "master";


    /**
     * 多租户的类型。默认是 Table
     */
    private DbStyle dbStyle = DbStyle.Table;


    /**
     * 创建表结构
     */
    private Boolean createTable = Boolean.TRUE;


    /**
     * 其他 数据库 创建表结构的 Liquibase 文件地址
     */
    private String otherChangeLog = "classpath:db/changelog-other.xml";


    /**
     * 主 数据库 创建表结构的 Liquibase 文件地址
     */
    private String masterChangeLog = "classpath:db/changelog-master.xml";


}
