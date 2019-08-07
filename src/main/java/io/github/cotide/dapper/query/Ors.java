package io.github.cotide.dapper.query;

import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.functions.TypeFunction;
import io.github.cotide.dapper.core.unit.Sql2oUtils;

import java.util.List;

public class Ors  {

    protected Sql sql;

    protected SqlWhereClause sqlWhereClause;

    public static Ors sql(){
        return new Ors();
    }

    public Ors(){
        sql = new Sql();
        sqlWhereClause = new SqlWhereClause(sql);
    }


    public Ors where(String statement, Object value) {
        sql.where(statement,value);
        return this;
    }

    public <T extends Entity,R>  Ors where(String asName,TypeFunction<T, R> function,Object value) {
        sql.where(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public <T extends Entity,R>  Ors where(TypeFunction<T, R> function,Object value) {
        sql.where(function,value);
        return this;
    }

    public Ors whereBetween(String columnName,Object a,Object b) {
        sql.whereBetween(columnName,a,b);
        return this;
    }

    public  <T extends Entity,R> Ors whereBetween(String asName,TypeFunction<T, R> function,Object a,Object b) {
        sql.whereBetween(asName+"."+Sql2oUtils.getLambdaColumnName(function),a,b);
        return this;
    }
    public  <T extends Entity,R> Ors whereBetween(TypeFunction<T, R> function,Object a,Object b) {
        sql.whereBetween(function,a,b);
        return this;
    }

    public <T extends Entity,R> Ors whereIn(String asName,TypeFunction<T, R> function, Object... values) {
        sql.whereIn(asName + "."+Sql2oUtils.getLambdaColumnName(function),values);
        return this;
    }


    public <T extends Entity,R> Ors whereIn(TypeFunction<T, R> function, Object... values) {
        sql.whereIn(function,values);
        return this;
    }

    public <S,T extends Entity,R> Ors whereIn(String asName,TypeFunction<T, R> function,List<S> values) {
        sql.whereIn(asName + "."+Sql2oUtils.getLambdaColumnName(function),values);
        return this;
    }

   public <S,T extends Entity,R> Ors whereIn(TypeFunction<T, R> function,List<S> values) {
        sql.whereIn(function,values);
        return this;
   }

    public Ors whereIn(String column,Object... values) {
        sql.whereIn(column,values);
        return this;
    }

    public Ors whereNotEq(String columnName,Object value) {
        sql.whereNotEq(columnName,value);
        return this;
    }

    public  <T extends Entity,R> Ors whereNotEq(String asName, TypeFunction<T, R> function, Object value) {
        sql.whereNotEq(asName + "."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public  <T extends Entity,R> Ors whereNotEq(TypeFunction<T, R> function, Object value) {
        sql.whereNotEq(function,value);
        return this;
    }

    public Ors whereLike(String columnName,Object value) {
        sql.whereLike(columnName,value);
        return this;
    }

    public <T extends Entity,R> Ors whereLike(String asName,TypeFunction<T, R> function,Object value) {
        sql.whereLike(asName + "."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }


    public <T extends Entity,R> Ors whereLike(TypeFunction<T, R> function,Object value) {
        sql.whereLike(function,value);
        return this;
    }


    public Ors whereGt(String columnName,Object value) {
        sql.whereGt(columnName,value);
        return this;
    }


    public <T extends Entity,R> Ors whereGt(String asName,TypeFunction<T, R> function,Object value) {
        sql.whereGt(asName + "."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public <T extends Entity,R> Ors whereGt(TypeFunction<T, R> function,Object value) {
        sql.whereGt(function,value);
        return this;
    }

    public Ors whereGte(String column,Object value) {
        sql.whereGte(column,value);
        return this;
    }


    public <S extends Entity, R> Ors whereGte(String asName,TypeFunction<S, R> function,Object value) {
        sql.whereGte(asName + "."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public <S extends Entity, R> Ors whereGte(TypeFunction<S, R> function,Object value) {
        sql.whereGte(function,value);
        return this;
    }


    public Ors whereLt(String column,Object value) {
        sql.whereLt(column,value);
        return this;
    }

    public <S extends Entity, R> Ors whereLt(String asName,TypeFunction<S, R> function,Object value) {
        sql.whereLt(asName + "."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public <S extends Entity, R> Ors whereLt(TypeFunction<S, R> function,Object value) {
        sql.whereLt(function,value);
        return this;
    }

    public Ors whereLte(String column,Object value) {
        sql.whereLte(column,value);
        return this;
    }

    public <S extends Entity, R> Ors whereLte(String asName,TypeFunction<S, R> function,Object value) {
        sql.whereLte(asName + "."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public <S extends Entity, R> Ors whereLte(TypeFunction<S, R> function,Object value) {
        sql.whereLte(function,value);
        return this;
    }

    // region public method

    public String getFinalSql(){
        if(sql.conditionSQL!=null)
        {
            return sql.conditionSQL.toString();
        }else{
            return "";
        }
    }

    public List<Object> getFinalArgs(){
        return sql.paramValues;
    }

    // endregion

}
