package org.templateproject.boot.service;

import me.wuwenbin.pojo.page.Page;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by wuwenbin on 2017/6/8.
 */
public interface IPagingAndSortingService<T, ID extends Serializable> extends ICrudBootService<T, ID> {

    /**
     * 查找当前页分页对象信息，不带实体类型
     *
     * @param page
     * @param sql
     * @param arrayParameters
     * @return
     */
    Page findPage(Page page, String sql, Object... arrayParameters);

    /**
     * 查找当前页分页对象信息，不带实体类型
     *
     * @param page
     * @param sql
     * @param mapParameters
     * @return
     */
    Page findPage(Page page, String sql, Map<String, Object> mapParameters);


    /**
     * 查找当前页分页对象信息,带实体类型
     *
     * @param page
     * @param clazz
     * @param sql
     * @param arrayParameters
     * @return
     */
    Page<T> findPage(Page<T> page, Class<T> clazz, String sql, Object... arrayParameters);

    /**
     * 查找当前页分页对象信息,带实体类型
     *
     * @param page
     * @param clazz
     * @param sql
     * @param mapParameters
     * @return
     */
    Page<T> findPage(Page<T> page, Class<T> clazz, String sql, Map<String, Object> mapParameters);

    /**
     * 查找当前页分页对象信息,带实体类型
     *
     * @param page
     * @param clazz
     * @param sql
     * @param bean
     * @return
     */
    <S extends T> Page<S> findPage(Page<S> page, Class<S> clazz, String sql, S bean);
}
