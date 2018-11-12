package io.github.cotide.dapper.query;

import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.functions.TypeFunction;
import io.github.cotide.dapper.core.unit.Sql2oCache;
import io.github.cotide.dapper.core.unit.Sql2oUtils;

public class SqlJoinClause extends BaseClause {


    public SqlJoinClause(Sql sql) {
        super(sql);
    }


    public SqlJoinClause as(String asName)
    {
        return new SqlJoinClause(_sql.append(new Sql(asName!=null?(asName+" "):"")));
    }

    public Sql on(String onClause, Object... args)
    {
        return _sql.append("on "+ onClause,args);
    }

    public   <T1 extends Entity,R1,T2 extends Entity,R2> Sql on(
            String asTableName1,
            TypeFunction<T1, R1> function1,
            String asTableName2,
            TypeFunction<T2, R2> function2) {

        String resultSql = "";
        if(asTableName1!=null && !asTableName1.trim().equals(""))
        {
            resultSql+= asTableName1+".";
        }
        resultSql+=Sql2oUtils.getLambdaColumnName(function1);
        resultSql+= " = ";
        if(asTableName2!=null && !asTableName2.trim().equals(""))
        {
            resultSql+= asTableName2+".";
        }
        resultSql+=Sql2oUtils.getLambdaColumnName(function2);
        return on(resultSql);
    }


    public   <T1 extends Entity,R1,T2 extends Entity,R2> Sql on(
            TypeFunction<T1, R1> function1,
            TypeFunction<T2, R2> function2) {
        return on(null,function1,null,function2);
    }


}
