package org.dapper.basic.domain;

import org.dapper.basic.domain.base.BaseEntityByType;
import org.dapper.core.attr.PrimaryKey;

/**
 * 实体抽象基类
 * @param <T1>
 */
@lombok.Getter
@lombok.Setter
public abstract class EntityByType<T1> extends BaseEntityByType {

    protected  EntityByType(){
    }

    @PrimaryKey("id")
    private  T1 id;

}
