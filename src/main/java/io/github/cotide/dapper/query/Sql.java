package io.github.cotide.dapper.query;

import io.github.cotide.dapper.core.functions.TypeFunction;
import io.github.cotide.dapper.core.unit.Sql2oCache;
import io.github.cotide.dapper.core.unit.Sql2oUtils;
import io.github.cotide.dapper.exceptions.SqlBuildException;
import io.github.cotide.dapper.basic.domain.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL
 */
public class Sql {

    private String _sql;
    private Object[] _args;
    private Sql _rhs;
    private String _sqlFinal;
    private Object[] _argsFinal;

    private final static Pattern SQL_BRACKET = Pattern.compile("(?<!@)@\\w+");

    public Sql() {

    }


    public Sql(String sql, Object... params) {
        _sql = sql;
        _args = params;
    }


    public Sql append(Sql sql) {
        if (_rhs != null) {
            _rhs.append(sql);
        } else {
            _rhs = sql;
        }

        return this;
    }


    public static Sql builder(){  return  new Sql(); }

    public Sql append(String sql, Object... params) {
        return append(new Sql(sql, params));
    }

    public Sql where(String sql, Object... params) {
        return append(new Sql("where " + sql, params));
    }

    public  <T extends Entity,R> Sql where(TypeFunction<T, R> function,Object param) {
         String columnName = Sql2oUtils.getLambdaColumnName(function);
         return where(columnName+"  = @0 ",param);
    }

    public  <T extends Entity,R>  Sql whereIn(TypeFunction<T, R> function, Object... paras) throws SqlBuildException {

        String column = Sql2oUtils.getLambdaColumnName(function);
        return whereIn(column,paras);
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
            where(String.format("%s in (@0)",column,paras));
        }

        return this;
    }

    public Sql select(String columns) {
        return append(new Sql("select " + columns));
    }

    public Sql select() {
        return append(new Sql("select  * " ));
    }


    public Sql from(String tables) {
        return append(new Sql("from " + tables));
    }


    public <T extends Entity>  Sql  from(Class<T> modelClass) {
        String tableName  = Sql2oCache.getTableName(modelClass);
        return append(new Sql("from " +  tableName));
    }


    public <T extends Entity>  Sql  from(Class<T> modelClass,String asName) {
        String tableName  = Sql2oCache.getTableName(modelClass);
        return append(new Sql("from " +  tableName+" "+asName));
    }


    public Sql orderBy(String columns) {
        return append(new Sql("order by " + columns));
    }

    public Sql orderBy(String columns,OrderBy orderBy) {

        return append(new Sql("order by " + columns + " "+ orderBy.toString()));
    }


    public  <T extends Entity,R> Sql orderBy(TypeFunction<T, R> function) {
        return orderBy(function,OrderBy.DESC);
    }

    public  <T extends Entity,R> Sql orderBy(TypeFunction<T, R> function,OrderBy orderBy) {
        String columnName = Sql2oUtils.getLambdaColumnName(function);
        return orderBy(columnName,orderBy);
    }


    public String getFinalSql() {
        build();
        return buildSql();
    }

    public Object[] getFinalArgs() {
        build();
        return _argsFinal;
    }

    public SqlJoinClause innerJoin(String table){
        return join("INNER JOIN ",table);
    }

    public <T extends Entity> SqlJoinClause innerJoin(Class<T> modelClass){
        return join("INNER JOIN ",Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause innerJoin(Class<T> modelClass,String asName){
        return join("INNER JOIN ",Sql2oCache.getTableName(modelClass) + " " + asName);
    }



    public SqlJoinClause leftJoin(String table)
    {
        return join("LEFT JOIN ",table);
    }


    public <T extends Entity> SqlJoinClause leftJoin(Class<T> modelClass){
        return join("LEFT JOIN ",Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause leftJoin(Class<T> modelClass,String asName){
        return join("LEFT JOIN ",Sql2oCache.getTableName(modelClass) + " " + asName);
    }

    public SqlJoinClause join(String table)
    {
        return join("LEFT JOIN ",table);
    }
    public <T extends Entity> SqlJoinClause join(Class<T> modelClass){
        return join("LEFT JOIN ",Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause join(Class<T> modelClass,String asName){
        return join("LEFT JOIN ",Sql2oCache.getTableName(modelClass) + " " + asName);
    }


    //#region  Helper

    private SqlJoinClause join(String joinType,String table)
    {
        return new SqlJoinClause(append(new Sql(joinType+table)));
    }

    private String buildSql() {
        Matcher m = SQL_BRACKET.matcher(_sqlFinal);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while(m.find()){
            m.appendReplacement(sb,":p"+i);
            ++i;
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private void build() {
        if (_sqlFinal != null && _sqlFinal.length() > 0)
        {
           return;
        }

        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList<Object>();

        build(sb, args, null);

        _sqlFinal = sb.toString();
        _argsFinal = args.toArray();
    }

    private void build(StringBuilder sb, List<Object> args, Sql lhs) {
        if (_sql != null && _sql.length() > 0) {
            if (sb.length() > 0) {
                sb.append("\n");
            }

            String sql = _sql;
            if (is(lhs, "where ") && is(this, "where "))
            {
                sql = "and " + sql.substring(6);
            }
            if (is(lhs, "order by ") && is(this, "order by "))
            {
                sql = ", " + sql.substring(9);
            }

            for (Object arg : _args) {
                args.add(arg);
            }
            sb.append(sql);
        }

        if (_rhs != null)
        {
            _rhs.build(sb, args, this);
        }
    }

    private static boolean is(Sql sql, String sqlType) {
        return sql != null
                && sql._sql != null
                && sql._sql.toLowerCase().startsWith(sqlType.toLowerCase());
    }


    //#endregion

}
