package org.dapper.basic.domain;

import org.dapper.core.attr.PrimaryKey;

/**
 * 实体抽象基类
 * @author cotide
 * @param <T1> 主键
 */
@lombok.Getter
@lombok.Setter
public abstract class Entity<T1> extends org.dapper.basic.domain.base.Entity {

    protected Entity(){
    }

    @PrimaryKey("id")
    private  T1 id;

}
