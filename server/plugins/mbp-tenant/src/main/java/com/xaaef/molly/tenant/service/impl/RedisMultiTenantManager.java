package com.xaaef.molly.tenant.service.impl;

import com.xaaef.molly.tenant.props.MultiTenantProperties;
import com.xaaef.molly.tenant.service.MultiTenantManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static com.xaaef.molly.common.util.TenantUtils.X_TENANT_ID;


@Slf4j
@Component
@AllArgsConstructor
public class RedisMultiTenantManager implements MultiTenantManager {

    private final StringRedisTemplate redisTemplate;


    private final MultiTenantProperties multiTenantProperties;


    @Override
    public String getDefaultTenantId() {
        return multiTenantProperties.getDefaultTenantId();
    }


    @Override
    public Set<String> getListTenantId() {
        return redisTemplate.opsForHash().keys(X_TENANT_ID)
                .stream().map(Object::toString)
                .collect(Collectors.toSet());
    }


    @Override
    public boolean isDefaultTenantId(String tenantId) {
        return StringUtils.equals(multiTenantProperties.getDefaultTenantId(), tenantId);
    }


    @Override
    public boolean existById(String tenantId) {
        return redisTemplate.opsForHash().hasKey(X_TENANT_ID, tenantId);
    }


    @Override
    public void addTenantId(String tenantId) {
        redisTemplate.opsForHash().put(X_TENANT_ID, tenantId, tenantId);
    }


    @Override
    public void addTenantIdBatch(Set<String> tenantIds) {
        tenantIds.forEach(this::addTenantId);
    }


    @Override
    public void removeTenantId(String tenantId) {
        redisTemplate.opsForHash().delete(X_TENANT_ID, tenantId);
    }

    @Override
    public void removeAll() {
        redisTemplate.delete(X_TENANT_ID);
    }

}
