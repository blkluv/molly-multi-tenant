package com.xaaef.molly.core.tenant;

import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER;


/**
 * <p>
 *
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/11/29 15:02
 */


@Slf4j
@AllArgsConstructor
public class CustomTenantProvider implements MultiTenantConnectionProvider, HibernatePropertiesCustomizer {

    private final DataSource dataSource;

    private final MultiTenantProperties props;

    @Override
    public Connection getConnection(String tenantId) throws SQLException {
        var conn = getAnyConnection();
        // 切换数据库
        String sql = String.format("USE %s%s", props.getPrefix(), tenantId);
        conn.createStatement().execute(sql);
        return conn;
    }


    @Override
    public void releaseConnection(String tenantId, Connection conn) throws SQLException {
        // 切换数据库
        String sql = String.format("USE %s%s", props.getPrefix(), props.getDefaultTenantId());
        conn.createStatement().execute(sql);
        conn.close();
    }


    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }


    @Override
    public void releaseAnyConnection(Connection conn) throws SQLException {
        conn.close();
    }


    @SuppressWarnings("rawtypes")
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }


    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }


    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }


    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MULTI_TENANT_CONNECTION_PROVIDER, this);
    }


}
