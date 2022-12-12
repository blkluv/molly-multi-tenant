package com.xaaef.molly.core.tenant.base.service;


import com.xaaef.molly.core.tenant.base.BaseEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * <p>
 * 通用 service 实现类
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */


public interface BaseService<T extends BaseEntity, ID> {

    List<T> findAll();

    List<T> findAllById(Iterable<ID> ids);

    <S extends T> List<S> findAll(Example<S> example);

    <S extends T> List<S> findAll(Example<S> example, Sort sort);


    Page<T> findPage(Pageable pageable);


    <S extends T> Page<S> findPage(Example<S> example, Pageable pageable);


    Optional<T> findById(ID id);


    T save(T entity);


    Collection<T> saveBatch(Collection<T> entity);


    T updateById(T entity);


    Collection<T> updateByIdBatch(Collection<T> arr);


    void deleteById(ID id);


    void deleteAllById(Collection<ID> ids);


}
