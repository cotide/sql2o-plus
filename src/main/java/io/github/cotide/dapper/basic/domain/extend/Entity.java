package io.github.cotide.dapper.basic.domain.extend;

import io.github.cotide.dapper.core.attr.PrimaryKey;

/**
 * 实体抽象基类
 * @author cotide
 * @param <T1> 主键
 */
@lombok.Getter
@lombok.Setter
public abstract class Entity<T1> extends io.github.cotide.dapper.basic.domain.Entity {

    protected Entity(){
    }

    @PrimaryKey("id")
    private  T1 id;

}
