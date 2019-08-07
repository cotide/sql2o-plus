package io.github.cotide.dapper.query;

import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.functions.TypeFunction;
import io.github.cotide.dapper.core.unit.Sql2oUtils;
import java.util.List;

public class Ands {

    protected Sql sql;

    protected SqlWhereClause sqlWhereClause;

    public static Ands sql(){
        return new Ands();
    }

    public Ands(){
        sql = new Sql();
        sqlWhereClause = new SqlWhereClause(sql);
    }

    public Ands where(String columnName, Object value) {
        sql.where(columnName,value);
        return this;
    }

    public <T extends Entity,R>  Ands where(String asName,TypeFunction<T, R> function, Object value) {

        sql.where(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public <T extends Entity,R>  Ands where(TypeFunction<T, R> function, Object value) {
        sql.where(function,value);
        return this;
    }

    public SqlWhereClause where(String statement) {
        return sql.where(statement);
    }

    public Ands whereBetween(String columnName,Object a,Object b) {
        sql.whereBetween(columnName,a,b);
        return this;
    }

    public  <T extends Entity,R> Ands whereBetween(String asName,TypeFunction<T, R> function,Object a,Object b) {
        sql.whereBetween(asName+"."+Sql2oUtils.getLambdaColumnName(function),a,b);
        return this;
    }


    public  <T extends Entity,R> Ands whereBetween(TypeFunction<T, R> function,Object a,Object b) {
        sql.whereBetween(function,a,b);
        return this;
    }


    public <T extends Entity,R> Ands whereIn(String asName,TypeFunction<T, R> function, Object... values) {
        sql.whereIn(asName+"."+Sql2oUtils.getLambdaColumnName(function),values);
        return this;
    }

    public <T extends Entity,R> Ands whereIn(TypeFunction<T, R> function, Object... values) {
        sql.whereIn(function,values);
        return this;
    }

    public <S,T extends Entity,R> Ands whereIn(String asName,TypeFunction<T, R> function,List<S> values) {
        sql.whereIn(asName+"."+Sql2oUtils.getLambdaColumnName(function),values);
        return this;
    }

    public <S,T extends Entity,R> Ands whereIn(TypeFunction<T, R> function,List<S> values) {
        sql.whereIn(function,values);
        return this;
    }


    public Ands whereIn(String column,Object... values) {
        sql.whereIn(column,values);
        return this;
    }

    public Ands whereNotEq(String columnName,Object value) {
        sql.whereNotEq(columnName,value);
        return this;
    }

    public  <T extends Entity,R> Ands whereNotEq(String asName,TypeFunction<T, R> function,Object value) {

        sql.whereNotEq(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public  <T extends Entity,R> Ands whereNotEq(TypeFunction<T, R> function,Object value) {
        sql.whereNotEq(function,value);
        return this;
    }

    public Ands whereLike(String columnName, Object value) {
        sql.whereLike(columnName,value);
        return this;
    }

    public <T extends Entity,R> Ands whereLike(String asName,TypeFunction<T, R> function,Object value) {
        sql.whereLike(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }


    public <T extends Entity,R> Ands whereLike(TypeFunction<T, R> function,Object value) {
        sql.whereLike(function,value);
        return this;
    }


    public Ands whereGt(String columnName,Object value) {
        sql.whereGt(columnName,value);
        return this;
    }

    public <T extends Entity,R> Ands whereGt(String asName,TypeFunction<T, R> function,Object value) {
        sql.whereGt(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public <T extends Entity,R> Ands whereGt(TypeFunction<T, R> function,Object value) {

        sql.whereGt(function,value);
        return this;
    }

    public Ands whereGte(String column,Object value) {
        sql.whereGte(column,value);
        return this;
    }


    public <S extends Entity, R> Ands whereGte(String asName,TypeFunction<S, R> function,Object value) {
        sql.whereGte(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public <S extends Entity, R> Ands whereGte(TypeFunction<S, R> function,Object value) {
        sql.whereGte(function,value);
        return this;
    }

    public Ands whereLt(String column,Object value) {
        sql.whereLt(column,value);
        return this;
    }

    public <S extends Entity, R> Ands whereLt(String asName,TypeFunction<S, R> function,Object value) {
        sql.whereLt(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public <S extends Entity, R> Ands whereLt(TypeFunction<S, R> function,Object value) {
        sql.whereLt(function,value);
        return this;
    }

    public Ands whereLte(String column,Object value) {
        sql.whereLte(column,value);
        return this;
    }

    public <S extends Entity, R> Ands whereLte(String asName,TypeFunction<S, R> function,Object value) {
        sql.whereLte(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }

    public <S extends Entity, R> Ands whereLte(TypeFunction<S, R> function,Object value) {
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
