package io.github.cotide.dapper.query;

import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.functions.TypeFunction;
import io.github.cotide.dapper.core.unit.Sql2oUtils;
import io.github.cotide.dapper.core.utility.Guard;
import io.github.cotide.dapper.exceptions.SqlBuildException;


public class SqlWhereClause extends BaseClause {


    public SqlWhereClause(Sql sql) {
        super(sql);
    }

    public  <T extends Entity,R> Sql whereGt(TypeFunction<T, R> function, Object param) {
        return where(Sql2oUtils.getLambdaColumnName(function)+"  > ? ",param);
    }

    public  <T extends Entity,R> Sql whereLt(TypeFunction<T, R> function,Object param) {
        return where(Sql2oUtils.getLambdaColumnName(function)+"  < ? ",param);
    }


    public  <T extends Entity,R> Sql whereGte(TypeFunction<T, R> function,Object param) {
        return where(Sql2oUtils.getLambdaColumnName(function)+"  >= ? ",param);
    }

    public  <T extends Entity,R> Sql whereLte(TypeFunction<T, R> function,Object param) {
        return where(Sql2oUtils.getLambdaColumnName(function)+"  <= ? ",param);
    }


    public Sql where(String sql, Object... params) {
        Guard.isNotNullOrEmpty(sql,"where sql");
        return  _sql.append(new Sql("where " + sql, params));

    }



    public  <T extends Entity,R> Sql where(String asName,TypeFunction<T, R> function,Object param) {
        return where((asName!=null&&!asName.isEmpty()?asName+".":"")+Sql2oUtils.getLambdaColumnName(function)+"  = ? ",param);
    }

    public  <T extends Entity,R> Sql where(TypeFunction<T, R> function,Object param) {
        return where(Sql2oUtils.getLambdaColumnName(function)+"  = ? ",param);
    }

    public  <T extends Entity,R>  Sql whereIn(String asName,TypeFunction<T, R> function, Object... paras) throws SqlBuildException {

        return whereIn((asName!=null&&!asName.isEmpty()?asName+".":"")+Sql2oUtils.getLambdaColumnName(function),paras);
    }

    public  <T extends Entity,R>  Sql whereIn(TypeFunction<T, R> function, Object... paras) throws SqlBuildException {

        return whereIn(Sql2oUtils.getLambdaColumnName(function),paras);
    }

    public Sql whereIn(String column, Object... paras) throws SqlBuildException {
        if (column == null || column.length() == 0){
            throw new SqlBuildException("query error: must have 'in' word");
        }
        if (paras == null || paras.length == 0)
        {
            throw new SqlBuildException("paras error");
        }
        if(paras.getClass().isArray())
        {
            StringBuffer appendValue =  new StringBuffer();

            for (int i=0 ;i<paras.length;i++)
            {
                appendValue.append("@"+i);
                if(i != paras.length-1)
                {
                    appendValue.append(",");
                }
            }

            String appendSql =  String.format("%s in (%s)",column,appendValue);
            where(appendSql,paras);
        }else{
            where(String.format("%s in (?)",column,paras));
        }

       return _sql;
    }


    public  <T extends Entity,R>  Sql whereLike(String asName,TypeFunction<T, R> function, String parm) throws SqlBuildException {

        return whereLike((asName!=null&&!asName.isEmpty()?asName+".":"")+Sql2oUtils.getLambdaColumnName(function),parm);
    }

    public  <T extends Entity,R>  Sql whereLike(TypeFunction<T, R> function, String parm) throws SqlBuildException {

        return whereLike(Sql2oUtils.getLambdaColumnName(function),parm);
    }

    public Sql whereLike(String column, String parm) throws SqlBuildException {
      return   where(column+" like ? ","%"+parm+"%");

    }
}
