package io.github.cotide.dapper.query;

import io.github.cotide.dapper.core.functions.TypeFunction;
import io.github.cotide.dapper.core.unit.Sql2oCache;
import io.github.cotide.dapper.core.unit.Sql2oUtils;
import io.github.cotide.dapper.core.utility.Guard;
import io.github.cotide.dapper.exceptions.SqlBuildException;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.query.enums.OrderBy;
import io.github.cotide.dapper.query.map.ResultMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Guard.isNotNullOrEmpty(sql,"where sql");
        return append(new Sql("where " + sql, params));
    }

    public  <T extends Entity,R> Sql where(String asName,TypeFunction<T, R> function,Object param) {
        return where((asName!=null&&!asName.isEmpty()?asName+".":"")+Sql2oUtils.getLambdaColumnName(function)+"  = @0 ",param);
    }

    public  <T extends Entity,R> Sql where(TypeFunction<T, R> function,Object param) {
        return where(Sql2oUtils.getLambdaColumnName(function)+"  = @0 ",param);
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
            where(String.format("%s in (@0)",column,paras));
        }

        return this;
    }

    public SelectClause select(String columns) {
        Guard.isNotNullOrEmpty(columns,"select columns");
        return new SelectClause(append(new Sql("select " + columns+" ")));
    }


    public  SelectClause select(
            ResultMap map)
    {
        String selectColumn =  map.getSelectColumn().trim();
        if(selectColumn == null || "".equals(selectColumn.trim())){
            return select();
        }
        selectColumn = selectColumn.substring(0, selectColumn.length() - 1);
        return select(selectColumn);
    }

    public   <T extends Entity,R> SelectClause select(TypeFunction<T, R>... function)
    {
        String columnNames = "";
        for (int j = 0; j<function.length; j++){
            columnNames += Sql2oUtils.getLambdaColumnName(function[j]);
            if(j !=function.length-1)
            {
                columnNames+=',';
            }
        }
        return select(columnNames);
    }




    public SelectClause select() {
        return select("*");
    }


    public <T> SelectClause selectTo(Class<T> modelClass){
        StringBuffer cols = new StringBuffer();
        Map<String, String> colums =  Sql2oCache.computeModelColumnMappings(modelClass);
        for (Map.Entry<String, String> entry : colums.entrySet()) {
            cols.append(
                    (entry.getKey().equals(entry.getValue()))?entry.getKey():
                     String.format("%s as %s",entry.getKey(),entry.getValue()))
                    .append(",\n");
        }
        String sqlColumn =cols.toString().trim();
        return select(sqlColumn.substring(0, sqlColumn.length() - 1));
    }

    public Sql as(String asName)
    {
        return append(new Sql(asName!=null?(asName+" "):""));
    }

    public Sql orderBy(String columns) {
        Guard.isNotNullOrEmpty(columns,"orderBy columns");
        return append(new Sql("order by " + columns));
    }

    public Sql orderBy(String columns, OrderBy orderBy) {
        Guard.isNotNullOrEmpty(columns,"orderBy columns");
        return append(new Sql("order by " + columns + " "+ orderBy.toString()));
    }

    public  <T extends Entity,R> Sql orderBy(String asName,TypeFunction<T, R> function) {
        return orderBy(asName,function,OrderBy.DESC);
    }

    public  <T extends Entity,R> Sql orderBy(String asName,TypeFunction<T, R> function,OrderBy orderBy) {
        String columnName = Sql2oUtils.getLambdaColumnName(function);
        return orderBy((asName!=null&&!asName.isEmpty()?asName+".":"")+columnName,orderBy);
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
        return innerJoin(table,null);
    }
    public SqlJoinClause innerJoin(String table,String asName){
        Guard.isNotNullOrEmpty(table,"innerJoin table name");
        return joinClause("inner join ",table + " " + (asName!=null?asName+" ":""));
    }

    public <T extends Entity> SqlJoinClause innerJoinDb(String dbName,Class<T> modelClass){
        Guard.isNotNullOrEmpty(dbName,"innerJoin dbName");
        return innerJoin(dbName+"."+Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause innerJoinDb(String dbName,Class<T> modelClass,String asName){
        Guard.isNotNullOrEmpty(dbName,"innerJoin dbName");
        return innerJoin(dbName+"."+Sql2oCache.getTableName(modelClass),asName);
    }

    public <T extends Entity> SqlJoinClause innerJoin(Class<T> modelClass){
        return innerJoin(Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause innerJoin(Class<T> modelClass,String asName){
        return innerJoin(Sql2oCache.getTableName(modelClass),asName);
    }


    public SqlJoinClause rightJoin(String table,String asName)
    {
        Guard.isNotNullOrEmpty(table,"rightJoin table name");
        return joinClause("right join ",table + " " + (asName!=null?asName+" ":""));
    }

    public SqlJoinClause rightJoin(String table)
    {
        return rightJoin(table,null);
    }

    public <T extends Entity> SqlJoinClause rightJoin(Class<T> modelClass){
        return rightJoin(Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause rightJoin(Class<T> modelClass,String asName){
        return rightJoin(Sql2oCache.getTableName(modelClass),asName);
    }

    public <T extends Entity> SqlJoinClause rightJoinDb(String dbName,Class<T> modelClass){
        Guard.isNotNullOrEmpty(dbName,"rightJoin dbName");
        return rightJoin(dbName+"."+Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause rightJoinDb(String dbName,Class<T> modelClass,String asName){
        Guard.isNotNullOrEmpty(dbName,"rightJoin dbName");
        return rightJoin(dbName+"."+Sql2oCache.getTableName(modelClass),asName);
    }




    public SqlJoinClause leftJoin(String table,String asName)
    {
        Guard.isNotNullOrEmpty(table,"leftJoin table name");
        return joinClause("left join ",table + " " + (asName!=null?asName+" ":""));
    }

    public SqlJoinClause leftJoin(String table)
    {
       return leftJoin(table,null);
    }

    public <T extends Entity> SqlJoinClause leftJoin(Class<T> modelClass){
        return leftJoin(Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause leftJoin(Class<T> modelClass,String asName){
        return leftJoin(Sql2oCache.getTableName(modelClass),asName);
    }

    public <T extends Entity> SqlJoinClause leftJoinDb(String dbName,Class<T> modelClass){
        Guard.isNotNullOrEmpty(dbName,"leftJoin dbName");
        return leftJoin(dbName+"."+Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause leftJoinDb(String dbName,Class<T> modelClass,String asName){
        Guard.isNotNullOrEmpty(dbName,"leftJoin dbName");
        return leftJoin(dbName+"."+Sql2oCache.getTableName(modelClass),asName);
    }


    public SqlJoinClause join(String table,String asName)
    {
        return joinClause("left join ",table+ " " + (asName!=null?asName+" ":""));
    }

    public SqlJoinClause join(String table)
    {
        return join(table,null);
    }
    public <T extends Entity> SqlJoinClause join(Class<T> modelClass){
        return join(Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause join(Class<T> modelClass,String asName){
        return join(Sql2oCache.getTableName(modelClass),asName);
    }



    public <T extends Entity> SqlJoinClause joinDb(String dbName,Class<T> modelClass){
        Guard.isNotNullOrEmpty(dbName,"join dbName");
        return join(dbName+"."+Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause joinDb(String dbName,Class<T> modelClass,String asName){
        Guard.isNotNullOrEmpty(dbName,"join dbName");
        return join(dbName+"."+Sql2oCache.getTableName(modelClass),asName);
    }


    //#region  Helper

    private SqlJoinClause joinClause(String joinType,String table)
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
