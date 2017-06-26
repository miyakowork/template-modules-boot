package org.templateproject.boot.service;

import java.io.Serializable;


/**
 * 核心数据存储接口。泛型为实体类类型和id的类型。
 * 保持通用类型，方便查询扩展.
 * <p>
 * 扩展此接口的域存储库可以通过简单地声明方法来选择性地公开CRUD方法
 *
 * @param <T>  实体类型
 * @param <ID> 实体中主键ID的类型
 * @author wuwenbin
 */
public interface IBootService<T, ID extends Serializable> {

}
