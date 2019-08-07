package io.github.cotide.dapper.query;

import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.functions.TypeFunction;
import io.github.cotide.dapper.core.unit.Sql2oUtils;
import io.github.cotide.dapper.core.utility.StringUtils;
import io.github.cotide.dapper.exceptions.SqlBuildException;
import io.github.cotide.dapper.query.base.BaseClause;

public class SqlJoinClause extends BaseClause {


    public SqlJoinClause(Sql sql) {
        super(sql);
    }


    public Sql on(String statement){
        _sql.joinSQL
                .append(" \nON ")
                .append(statement);
        return _sql;
    }

    public  <T1 extends Entity,R1,T2 extends Entity,R2> Sql on(
            TypeFunction<T1, R1> function1,
            TypeFunction<T2, R2> function2) {

         return on(null,function1,null,function2);
    }


    public  <T1 extends Entity,R1,T2 extends Entity,R2> Sql on(
            String asName1,
            TypeFunction<T1, R1> function1,
            String asName2,
            TypeFunction<T2, R2> function2) {

        _sql.joinSQL.append(" \nON ");
        if(!StringUtils.isNullOrEmpty(asName1))
        {
            _sql.joinSQL.append(asName1 + ".");
        }
        _sql.joinSQL.append(Sql2oUtils.getLambdaColumnName(function1));
        _sql.joinSQL.append(" = ");
        if(!StringUtils.isNullOrEmpty(asName2))
        {
            _sql.joinSQL.append(asName2 + ".");
        }
        _sql.joinSQL.append(Sql2oUtils.getLambdaColumnName(function2));
        return _sql;
    }


    public SqlJoinClause as(String asName)
    {
        if(StringUtils.isNullOrEmpty(asName)){
            throw new SqlBuildException("As name is not null");
        }
        _sql.conditionSQL.append(asName);
        return this;
    }

}
