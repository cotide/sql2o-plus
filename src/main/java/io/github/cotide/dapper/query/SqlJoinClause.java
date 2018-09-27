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
        return _sql.append("ON "+ onClause,args);
    }

//    public   <T1 extends Entity,R1,T2 extends Entity,R2> Sql on(
//            TypeFunction<T1, R1> function1,
//            TypeFunction<T2, R2> function2) {
//
//        Class<? extends  Entity> table1Class = (Class<? extends Entity>) function1.getClass();
//        String table1 = Sql2oCache.getTableName(table1Class.getClass());
//        String table2 = Sql2oCache.getTableName(function2.getClass());
//        return on(table1,function1,table2,function2);
//    }
//
//
//    public  <T1 extends Entity,R1,T2 extends Entity,R2>  Sql on(String asName,
//                                        TypeFunction<T1, R1> function1,
//                                        String asName2,
//                                        TypeFunction<T2, R2> function2) {
//        String columnName1 = Sql2oUtils.getLambdaColumnName(function1);
//        String columnName2 = Sql2oUtils.getLambdaColumnName(function2);
//        return _sql.append("ON "+asName+"."+columnName1 +" = "+asName2+"."+columnName2);
//    }
}
