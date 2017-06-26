package org.templateproject.boot.service;

import java.io.Serializable;
import java.util.Collection;

/**
 * 用于特定类型的存储库上的通用CRUD操作的接口。
 * Created by wuwenbin on 2017/6/5.
 */
public interface ICrudBootService<T, ID extends Serializable> extends IBootService<T, ID> {

    /**
     * 保存给定的实体。
     * 使用返回的实例进行进一步操作，因为保存操作可能会完全更改实体实例。
     * 如果需要S和T的字段都插入到数据库，那么T类中字段修饰符请使用protected或者public
     *
     * @param entity 插入的实体
     * @param <S>    实体类型对象
     * @param clazz  实体类型
     * @return 已保存的实体
     */
    <S extends T> S save(S entity, Class<S> clazz) throws Exception;

    /**
     * 保存给的所有实体
     *
     * @param entities 插入的实体集合
     * @param clazz    实体类型
     * @return 保存的实体集合
     * @throws IllegalArgumentException 路给所给的实体参数集合为 {@literal null}.
     */
    <S extends T> Collection<S> save(Collection<S> entities, Class<S> clazz) throws Exception;

    /**
     * 根据id查找实体
     *
     * @param id 一定不能为 {@literal null}.
     * @return 根据id查找的实体或者 {@literal null} 如果没有查找到
     * @throws IllegalArgumentException 如果 {@code id} 为 {@literal null}
     */
    T findOne(ID id, Class<T> clazz);

    /**
     * 根据id判断返回是否存在这个实体
     *
     * @param id 不能为 {@literal null}.
     * @return 如果所给id能找到对应集合返回true，否则false
     * @throws IllegalArgumentException 如果 {@code id} 为 {@literal null}
     */
    boolean exists(ID id, Class<T> clazz);

    /**
     * 返回所有类型实例
     *
     * @return 所有实例集合
     */
    Collection<T> findAll(Class<T> clazz);

    /**
     * 根据所给的id集合返回对应的所有实体集合
     *
     * @param ids 查找条件中的id集合对象
     * @return 查找的对象结果结合
     */
    Collection<T> findAll(Collection<ID> ids, Class<T> clazz);

    /**
     * 查找实体个数
     *
     * @return 个数
     */
    long count(Class<T> clazz);

    /**
     * 根据所给的id删除对应的实体
     *
     * @param id 不能为 {@literal null}.
     * @throws IllegalArgumentException 如果 {@code id} 为 {@literal null}
     */
    void delete(ID id, Class<T> clazz) throws Exception;

    /**
     * 根据所给的id集合删除对应的实体
     *
     * @param ids
     */
    void deleteByIds(Iterable<ID> ids, Class<T> clazz);

    /**
     * 删除所给的参数实体
     *
     * @param entity
     * @throws IllegalArgumentException 如果所给参数实体为 {@literal null}.
     */
    void delete(T entity) throws Exception;

    /**
     * 删除所给参数实体集合
     *
     * @param entities
     * @throws IllegalArgumentException 如果所给参数集合 {@link Iterable} 为 {@literal null}.
     */
    void delete(Iterable<? extends T> entities);

    /**
     * 删除所有实体
     */
    void deleteAll(Class<T> clazz) throws Exception;


}
