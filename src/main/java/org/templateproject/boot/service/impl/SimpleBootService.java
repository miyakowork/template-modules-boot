package org.templateproject.boot.service.impl;

import me.wuwenbin.pojo.page.Page;
import me.wuwenbin.sql.entrance.SQLFactory;
import me.wuwenbin.sql.factory.SQLBeanBuilder;
import me.wuwenbin.sql.factory.SQLStrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.templateproject.boot.service.IPagingAndSortingService;
import org.templateproject.dao.ancestor.AncestorDao;
import org.templateproject.dao.factory.business.DataSourceX;
import org.templateproject.dao.factory.business.DbType;
import org.templateproject.dao.posterity.h2.H2Template;
import org.templateproject.dao.posterity.mysql.MysqlTemplate;
import org.templateproject.dao.posterity.oracle.OracleTemplate;
import org.templateproject.dao.posterity.postgresql.PostgreSqlTemplate;
import org.templateproject.dao.posterity.sqlite.SqliteTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 多数据源默认dao的一个service实现
 * Created by wuwenbin on 2017/6/10.
 */
@Transactional
public class SimpleBootService<T, ID extends Serializable> implements IPagingAndSortingService<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleBootService.class);

    private AncestorDao ancestorDao;
    private DataSourceTransactionManager txManager;

    /**
     * 构造注入dao和bean对象
     *
     * @param dataSourceX 高级数据源对象
     * @param txManager   事务对象
     */
    public SimpleBootService(DataSourceX dataSourceX, DataSourceTransactionManager txManager) {
        if (dataSourceX.getInitDbType() == DbType.H2) {
            this.ancestorDao = new H2Template(dataSourceX.getDataSource());
        } else if (dataSourceX.getInitDbType() == DbType.Oracle) {
            this.ancestorDao = new OracleTemplate(dataSourceX.getDataSource());
        } else if (dataSourceX.getInitDbType() == DbType.Sqlite) {
            this.ancestorDao = new SqliteTemplate(dataSourceX.getDataSource());
        } else if (dataSourceX.getInitDbType() == DbType.Postgresql) {
            this.ancestorDao = new PostgreSqlTemplate(dataSourceX.getDataSource());
        } else {
            this.ancestorDao = new MysqlTemplate(dataSourceX.getDataSource());
        }
        this.txManager = txManager;
    }


    @Override
    public <S extends T> S save(S entity, Class<S> clazz) throws Exception {
        SQLBeanBuilder sbb = SQLFactory.builder(clazz);
        String saveSQL = sbb.insertAllWithoutPk();
        LOGGER.info("SQL:[{}]", saveSQL);
        return ancestorDao.insertBeanAutoGenKeyOutBean(saveSQL, entity, clazz, sbb.getTableName());
    }

    @Override
    public <S extends T> Collection<S> save(Collection<S> entities, Class<S> clazz) {
        SQLBeanBuilder sbb = SQLFactory.builder(clazz);
        String saveSQL = sbb.insertAllWithoutPk();
        LOGGER.info("SQL:[{}]", saveSQL);
        Collection<S> s = new ArrayList<>();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务
        TransactionStatus status = txManager.getTransaction(def); // 获得事务状态
        try {
            for (S next : entities) {
                s.add(ancestorDao.insertBeanAutoGenKeyOutBean(saveSQL, next, clazz, sbb.getTableName()));
            }
            txManager.commit(status);
            return s;
        } catch (Exception e) {
            txManager.rollback(status);
            LOGGER.error("插入集合发生错误: {}", e);
            return null;
        }
    }

    @Override
    public T findOne(ID id, Class<T> clazz) {
        SQLBeanBuilder sbb = SQLFactory.builder(clazz);
        SQLStrBuilder ssb = SQLFactory.builder();
        String findSQL = ssb.selectAllByColumns(sbb.getTableName(), sbb.getPkField().getName());
        LOGGER.info("SQL:[{}]", findSQL);
        return ancestorDao.findBeanByArray(findSQL, clazz, id);
    }

    @Override
    public boolean exists(ID id, Class<T> clazz) {
        return findOne(id, clazz) != null;
    }

    @Override
    public Collection<T> findAll(Class<T> clazz) {
        SQLBeanBuilder sbb = SQLFactory.builder(clazz);
        String findSQL = sbb.selectAll();
        LOGGER.info("SQL:[{}]", findSQL);
        return ancestorDao.findListBeanByArray(findSQL, clazz);
    }

    @Override
    public Collection<T> findAll(Collection<ID> ids, Class<T> clazz) {
        Collection<T> t = new ArrayList<>();
        for (ID id : ids) {
            t.add(findOne(id, clazz));
        }
        return t;
    }

    @Override
    public long count(Class<T> clazz) {
        SQLBeanBuilder sbb = SQLFactory.builder(clazz);
        String countSQL = sbb.countAll();
        LOGGER.info("SQL:[{}]", countSQL);
        return ancestorDao.queryNumberByArray(countSQL, Long.class);
    }

    @Override
    public void delete(ID id, Class<T> clazz) throws Exception {
        SQLBeanBuilder sbb = SQLFactory.builder(clazz);
        SQLStrBuilder ssb = SQLFactory.builder();
        String deleteSQL = ssb.deleteByColumns(sbb.getTableName(), sbb.getPkField().getName());
        LOGGER.info("SQL:[{}]", deleteSQL);
        ancestorDao.executeArray(deleteSQL, id);
    }

    @Override
    public void deleteByIds(Iterable<ID> ids, Class<T> clazz) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务
        TransactionStatus status = txManager.getTransaction(def); // 获得事务状态
        try {
            for (ID id : ids) {
                delete(id, clazz);
            }
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            LOGGER.error("删除过程中发生错误: {}", e);
        }
    }

    @Override
    public void delete(T entity) throws Exception {
        SQLBeanBuilder sbb = SQLFactory.builder(entity.getClass());
        String deleteSQL = sbb.deleteByPk();
        LOGGER.info("SQL:[{}]", deleteSQL);
        ancestorDao.executeBean(deleteSQL, entity);
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务
        TransactionStatus status = txManager.getTransaction(def); // 获得事务状态
        try {
            for (T entity : entities) {
                delete(entity);
            }
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            LOGGER.error("删除过程中发生错误: {}", e);
        }
    }

    @Override
    public void deleteAll(Class<T> clazz) throws Exception {
        SQLBeanBuilder sbb = SQLFactory.builder(clazz);
        SQLStrBuilder ssb = SQLFactory.builder();
        String deleteSQL = ssb.deleteByColumns(sbb.getTableName());
        LOGGER.info("SQL:[{}]", deleteSQL);
        ancestorDao.executeArray(deleteSQL);
    }

    @Override
    public Page findPage(Page page, String sql, Object... arrayParameters) {
        return ancestorDao.findPageListMapByArray(sql, page, arrayParameters);

    }

    @Override
    public Page findPage(Page page, String sql, Map<String, Object> mapParameters) {
        return ancestorDao.findPageListMapByMap(sql, page, mapParameters);
    }

    @Override
    public Page<T> findPage(Page<T> page, Class<T> clazz, String sql, Object... arrayParameters) {
        return ancestorDao.findPageListBeanByArray(sql, clazz, page, arrayParameters);
    }

    @Override
    public Page<T> findPage(Page<T> page, Class<T> clazz, String sql, Map<String, Object> mapParameters) {
        return ancestorDao.findPageListBeanByArray(sql, clazz, page, mapParameters);
    }

    @Override
    public <S extends T> Page<S> findPage(Page<S> page, Class<S> clazz, String sql, S bean) {
        return ancestorDao.findPageListBeanByArray(sql, clazz, page, bean);
    }
}
