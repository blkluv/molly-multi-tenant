package com.xaaef.molly.core.tenant;

import com.xaaef.molly.core.tenant.props.MultiTenantProperties;

import javax.sql.DataSource;

public interface DataSourceManager {

    default String getOldDbName(String url) {
        var startInx = url.lastIndexOf("?");
        var sub = url.substring(0, startInx);
        int startInx2 = sub.lastIndexOf("/") + 1;
        return sub.substring(startInx2);
    }

    /**
     * TODO 获取默认的数据源
     *
     * @author WangChenChen
     * @date 2022/12/11 11:04
     */

    DataSource getDefaultDataSource();


    /**
     * TODO 根据租户ID 获取对应的数据源
     *
     * @author WangChenChen
     * @date 2022/12/11 11:04
     */

    DataSource getDataSource(String tenantId);


    /**
     * TODO 租户创建表结构
     *
     * @author WangChenChen
     * @date 2022/12/11 11:04
     */
    void createTable(String tenantId);


    /**
     * TODO 获取多租户信息
     *
     * @author WangChenChen
     * @date 2022/12/11 11:04
     */
    MultiTenantProperties getMultiTenantProperties();


}
