package io.github.cotide.dapper.query;
import io.github.cotide.dapper.core.dialect.Dialect;
import io.github.cotide.dapper.core.dialect.MySQLDialect;
import io.github.cotide.dapper.core.functions.TypeFunction;
import io.github.cotide.dapper.core.unit.Sql2oCache;
import io.github.cotide.dapper.core.unit.Sql2oUtils;
import io.github.cotide.dapper.core.utility.Guard;
import io.github.cotide.dapper.core.utility.StringUtils;
import io.github.cotide.dapper.exceptions.SqlBuildException;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.query.enums.OrderBy;
import io.github.cotide.dapper.query.parm.SQLParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.cotide.dapper.core.utility.Functions.ifThen;

/**
 * SQL
 */
public class Sql {


    private String _sql;

    private String _sqlFinal;


    private String selectColumns;

    private final static Pattern SQL_WHERE_BRACKET = Pattern.compile( "(?<!WHERE)WHERE+",Pattern.CASE_INSENSITIVE);

    private final static Pattern SQL_FROM_BRACKET = Pattern.compile( "(?<!FROM)FROM+",Pattern.CASE_INSENSITIVE);


    /**
     * 查询条件
     */
    protected StringBuilder conditionSQL = new StringBuilder();


    /**
     * 查询条件值
     */
    protected List<Object> paramValues = new ArrayList<>(8);

    /**
     * Join Sql
     */
    protected StringBuilder joinSQL = new StringBuilder();

    /**
     * 排序
     */
    protected StringBuilder orderBySQL = new StringBuilder();

    /**
     * 表名
     */
    protected String tableName;



    private Dialect dialect = new MySQLDialect();

    public Sql() {

    }

    public Dialect dialect() {
        return this.dialect;
    }


    public Sql(String str , Object... obj) {
        this._sqlFinal = str;
        if(obj!=null && obj.length>0)
        {
            Arrays.stream(obj).forEach(x-> this.paramValues.add(x));
        }
    }

    public String getFinalSql() {
        build();
        return _sql;
    }

    public List<Object> getFinalArgs() {
        return paramValues;
    }

    public static Sql builder(){  return  new Sql(); }


    public SelectClause select(String columns) {
        Guard.isNotNullOrEmpty(columns,"select columns");
        this.selectColumns = columns;
        return new SelectClause(this);
    }

    public <T extends Entity,R> SelectClause select(TypeFunction<T,R>... functions)
    {
        String columnNames = "";
        for (int j = 0;j<functions.length;j++)
        {
            columnNames += Sql2oUtils.getLambdaColumnName(functions[j]);
            if(j != functions.length -1 )
            {
                columnNames += ',';
            }
        }
        return select(columnNames);
    }

    public SelectClause select() {
        return select("*");
    }

    public SqlWhereClause where(String statement) {
        conditionSQL.append(" \nAND ").append(statement);
        return new SqlWhereClause(this);
    }

    public <T extends Entity,R> SqlWhereClause where(TypeFunction<T, R> function) {
        return where(Sql2oUtils.getLambdaColumnName(function));
    }


    public Sql where(String statement,Object value) {
        conditionSQL.append(" \nAND ").append(statement);
        if (!statement.contains("?")) {
            conditionSQL.append(" = ?");
        }
        paramValues.add(value);
        return this;
    }

    public <T extends Entity,R>  Sql where(String asName, TypeFunction<T, R> function,Object value) {
        Guard.isNotNullOrEmpty(asName,"asName");
        return where(asName +"."+ Sql2oUtils.getLambdaColumnName(function),value);
    }


    public <T extends Entity,R>  Sql where(TypeFunction<T, R> function,Object value) {
        return where(Sql2oUtils.getLambdaColumnName(function),value);
    }

    public Sql whereBetween(String columnName,Object a, Object b) {
        conditionSQL.append(" \nAND ").append(columnName).append(" BETWEEN ? and ?");
        paramValues.add(a);
        paramValues.add(b);
        return this;
    }

    public <T extends Entity,R> Sql whereBetween(String asName,TypeFunction<T, R> function,Object a,Object b) {
        return this.whereBetween(asName +"."+Sql2oUtils.getLambdaColumnName(function),a,b);
    }

    public <T extends Entity,R> Sql whereBetween(TypeFunction<T, R> function,Object a,Object b) {
        return this.whereBetween(Sql2oUtils.getLambdaColumnName(function),a,b);
    }

    public Sql whereIn(String column,Object... values) {
        if (null == values || values.length == 0) {
            throw new SqlBuildException("Column query params is not null");
        }
        return this.where(column).in(values);
    }

    public <T extends Entity,R> Sql whereIn(String asName,TypeFunction<T, R> function,Object... values) {
        return this.whereIn(asName+"." + Sql2oUtils.getLambdaColumnName(function),values);
    }

    public <T extends Entity,R> Sql whereIn(TypeFunction<T, R> function,Object... values) {
        return this.whereIn(Sql2oUtils.getLambdaColumnName(function),values);
    }

    public <S,T extends Entity,R> Sql whereIn(String asName,TypeFunction<T, R> function,List<S> values) {
        return this.whereIn(asName+"." +Sql2oUtils.getLambdaColumnName(function),values);
    }

    public <S,T extends Entity,R> Sql whereIn(TypeFunction<T, R> function,List<S> values) {
        return this.whereIn(Sql2oUtils.getLambdaColumnName(function),values);
    }


    public Sql whereNotEq(String columnName, Object value) {
        conditionSQL.append(" AND ").append(columnName).append(" != ?");
        paramValues.add(value);
        return this;
    }

    public  <T extends Entity,R> Sql whereNotEq(String asName,TypeFunction<T, R> function,Object value) {
        return this.whereNotEq(asName+"." +Sql2oUtils.getLambdaColumnName(function),value);
    }

    public  <T extends Entity,R> Sql whereNotEq(TypeFunction<T, R> function,Object value) {
        return this.whereNotEq(Sql2oUtils.getLambdaColumnName(function),value);
    }

    public Sql whereLike(String columnName, Object value) {
        conditionSQL.append(" AND ").append(columnName).append(" LIKE ?");
        paramValues.add(value);
        return this;
    }

    public <T extends Entity,R> Sql whereLike(String asName,TypeFunction<T, R> function,Object value) {
        return this.whereLike(asName+"." +Sql2oUtils.getLambdaColumnName(function),value);
    }

    public <T extends Entity,R> Sql whereLike(TypeFunction<T, R> function,Object value) {
        return this.whereLike(Sql2oUtils.getLambdaColumnName(function),value);
    }

    public Sql whereGt(String columnName,Object value) {
        conditionSQL.append(" AND ").append(columnName).append(" > ?");
        paramValues.add(value);
        return this;
    }

    public <T extends Entity,R> Sql whereGt(String asName,TypeFunction<T, R> function,Object value) {
        return whereGt(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
    }

    public <T extends Entity,R> Sql whereGt(TypeFunction<T, R> function,Object value) {
        return whereGt(Sql2oUtils.getLambdaColumnName(function),value);
    }

    public Sql whereGte(String columnName,Object value) {
        conditionSQL.append(" AND ").append(columnName).append(" >= ?");
        paramValues.add(value);
        return this;
    }

    public <S extends Entity, R> Sql whereGte(String asName,TypeFunction<S, R> function,Object value) {
        return this.whereGte(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
    }

    public <S extends Entity, R> Sql whereGte(TypeFunction<S, R> function,Object value) {
        return this.whereGte(Sql2oUtils.getLambdaColumnName(function),value);
    }

    public Sql whereLt(String columnName, Object value) {
        conditionSQL.append(" AND ").append(columnName).append(" < ?");
        paramValues.add(value);
        return this;
    }

    public <S extends Entity, R> Sql whereLt(String asName,TypeFunction<S, R> function,Object value) {
        return this.whereLt(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
    }

    public <S extends Entity, R> Sql whereLt(TypeFunction<S, R> function,Object value) {
        return this.whereLt(Sql2oUtils.getLambdaColumnName(function),value);
    }

    public Sql whereLte(String columnName,Object value) {
        conditionSQL.append(" AND ").append(columnName).append(" <= ?");
        paramValues.add(value);
        return this;
    }

    public <S extends Entity, R> Sql whereLte(TypeFunction<S, R> function,Object value) {
        return whereLte(Sql2oUtils.getLambdaColumnName(function),value);
    }

    public <S extends Entity, R> Sql whereLte(String asName,TypeFunction<S, R> function,Object value) {
        return whereLte(asName+"."+Sql2oUtils.getLambdaColumnName(function),value);
    }

    public Sql or(Ors orParam)
    {
        String beginSql = "      \nOR";
        if(this.conditionSQL == null || this.conditionSQL.length()<=0){
           // throw new SqlBuildException("Or query is error");
            beginSql =  "WHERE ";
        }
        if(orParam==null)
        {
            throw new SqlBuildException("Or param not null");
        }
        String orWhereSql = orParam.getFinalSql();
        if(StringUtils.isNullOrEmpty(orWhereSql))
        {
            throw new SqlBuildException("Or param not null");
        }
        boolean isAddBracket = orParam.getFinalArgs().size()>1;

        if(isAddBracket)
        {
            conditionSQL.append(beginSql+" ( \n");
        }else{
            conditionSQL.append(beginSql+" \n");
        }
        conditionSQL.append(orWhereSql.substring(5));
        addParamValues(orParam.getFinalArgs());
        if(isAddBracket)
        {
            conditionSQL.append(" )");
        }
        return this;
    }


    public Sql and(Ands ands)
    {
        if(this.conditionSQL == null || this.conditionSQL.length()<=0){
            throw new SqlBuildException("And query is error");
        }
        if(ands==null)
        {
            throw new SqlBuildException("And param not null");
        }
        String orWhereSql = ands.getFinalSql();
        if(StringUtils.isNullOrEmpty(orWhereSql))
        {
            throw new SqlBuildException("And param not null");
        }
        conditionSQL.append("      \nAND (");
        conditionSQL.append(orWhereSql);
        addParamValues(ands.getFinalArgs());
        conditionSQL.append(" \n)");
        return this;
    }


    public Sql order(String order) {
        if (this.orderBySQL.length() > 0) {
            this.orderBySQL.append(',');
        }
        this.orderBySQL.append(' ').append(order);
        return this;
    }

    public Sql order(String columnName,OrderBy orderBy) {
        if (this.orderBySQL.length() > 0) {
            this.orderBySQL.append(',');
        }
        this.orderBySQL.append(' ')
                .append(columnName)
                .append(' ')
                .append(orderBy.toString());
        return this;
    }


    public <T extends Entity,R>  Sql order(String asName,TypeFunction<T, R> function) {

        return order(asName+"."+Sql2oUtils.getLambdaColumnName(function));
    }

    public <T extends Entity,R>  Sql order(String asName,TypeFunction<T, R> function,OrderBy orderBy) {
        return order(asName+"."+Sql2oUtils.getLambdaColumnName(function),orderBy);
    }

    public <T extends Entity,R>  Sql order(TypeFunction<T, R> function,OrderBy orderBy) {
        return order(Sql2oUtils.getLambdaColumnName(function),orderBy);
    }


    public <T extends Entity,R>  Sql order(TypeFunction<T, R> function) {
        return order(Sql2oUtils.getLambdaColumnName(function),OrderBy.ASC);
    }

    // region innerJoin

    public SqlJoinClause innerJoin(String tableName,String asName)
    {
        return  joinClause("INNER JOIN",tableName + " " + asName);
    }

    public SqlJoinClause innerJoin(String tableName)
    {
        return  innerJoin(tableName,"");
    }

     public <T extends Entity> SqlJoinClause innerJoin(Class<T> modelClass)
    {
        return innerJoin(modelClass,"");
    }

    public <T extends Entity> SqlJoinClause innerJoin(Class<T> modelClass,String asName)
    {
        return innerJoin(Sql2oCache.getTableName(modelClass),asName);
    }

    public <T extends Entity> SqlJoinClause innerJoinDb(String dbName,Class<T> modelClass)
    {
        Guard.isNotNullOrEmpty(dbName,"dbName");
        return innerJoin(dbName+"."+Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause innerJoinDb(String dbName,Class<T> modelClass,String asName)
    {
        Guard.isNotNullOrEmpty(dbName,"dbName");
        return innerJoin(dbName+"."+Sql2oCache.getTableName(modelClass),asName);
    }

    // endregion

    // region rightJoin

    public SqlJoinClause rightJoin(String tableName,String asName)
    {
        return  joinClause("RIGHT JOIN",tableName + " " + asName);
    }

    public SqlJoinClause rightJoin(String tableName)
    {
        return  rightJoin(tableName,"");
    }

    public <T extends Entity> SqlJoinClause rightJoin(Class<T> modelClass)
    {
        return rightJoin(modelClass,"");
    }

    public <T extends Entity> SqlJoinClause rightJoin(Class<T> modelClass,String asName)
    {
        return rightJoin(Sql2oCache.getTableName(modelClass),asName);
    }


    public <T extends Entity> SqlJoinClause rightJoinDb(String dbName,Class<T> modelClass)
    {
        Guard.isNotNullOrEmpty(dbName,"dbName");
        return rightJoin(dbName+"."+Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause rightJoinDb(String dbName,Class<T> modelClass,String asName)
    {
        Guard.isNotNullOrEmpty(dbName,"dbName");
        return rightJoin(dbName+"."+Sql2oCache.getTableName(modelClass),asName);
    }


    // endregion

    // region leftJoin

    public SqlJoinClause leftJoin(String tableName,String asName)
    {
        return  joinClause("LEFT JOIN",tableName + " " + asName);
    }

    public SqlJoinClause leftJoin(String tableName)
    {
        return  leftJoin(tableName,"");
    }

    public <T extends Entity> SqlJoinClause leftJoin(Class<T> modelClass)
    {
        return leftJoin(modelClass,"");
    }

    public <T extends Entity> SqlJoinClause leftJoin(Class<T> modelClass,String asName)
    {
        return leftJoin(Sql2oCache.getTableName(modelClass),asName);
    }


    public <T extends Entity> SqlJoinClause leftJoinDb(String dbName,Class<T> modelClass)
    {
        Guard.isNotNullOrEmpty(dbName,"dbName");
        return leftJoin(dbName+"."+Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause leftJoinDb(String dbName,Class<T> modelClass,String asName)
    {
        Guard.isNotNullOrEmpty(dbName,"dbName");
        return leftJoin(dbName+"."+Sql2oCache.getTableName(modelClass),asName);
    }


    // endregion

    // region join


    public SqlJoinClause join(String tableName,String asName)
    {
        return  joinClause("JOIN",tableName + " " + asName);
    }

    public SqlJoinClause join(String tableName)
    {
        return  leftJoin(tableName,"");
    }

    public <T extends Entity> SqlJoinClause join(Class<T> modelClass)
    {
        return join(modelClass,"");
    }

    public <T extends Entity> SqlJoinClause join(Class<T> modelClass,String asName)
    {
        return join(Sql2oCache.getTableName(modelClass),asName);
    }


    public <T extends Entity> SqlJoinClause joinDb(String dbName,Class<T> modelClass)
    {
        Guard.isNotNullOrEmpty(dbName,"dbName");
        return join(dbName+"."+Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> SqlJoinClause joinDb(String dbName,Class<T> modelClass,String asName)
    {
        Guard.isNotNullOrEmpty(dbName,"dbName");
        return join(dbName+"."+Sql2oCache.getTableName(modelClass),asName);
    }

    // endregion


    // region Protected Method

    protected void addParamValue(Object value)
    {
        this.paramValues.add(value);
    }

    protected void addParamValues(List<Object> values)
    {
        values.stream().forEach(x-> addParamValue(x));
    }

    /**
     * 设置In 参数值
     * @param args
     */
    protected void setInArguments(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            ifThen(i == args.length - 1,
                    () -> conditionSQL.append("?"),
                    () -> conditionSQL.append("?, "));
            paramValues.add(args[i]);
        }
    }

    // endregion


    // region Helper

    private void build() {

        if(StringUtils.isNullOrEmpty(_sqlFinal)){
            SQLParams sqlParams = SQLParams.builder()
                    .selectColumns(this.selectColumns)
                    .tableName(this.tableName)
                    .joinSQL(this.joinSQL)
                    .conditionSQL(this.conditionSQL)
                    .orderBy(this.orderBySQL.toString())
                    .build();
            // 生成SQL
            _sql = dialect().select(sqlParams);

        }else {
            StringBuilder sqlBuild = new StringBuilder(_sqlFinal);

            // Join条件
            if(this.joinSQL!=null && this.joinSQL.length()>0)
            {
                Matcher m  = SQL_FROM_BRACKET.matcher(_sqlFinal);
                if(m.find())
                {
                    sqlBuild.append(this.joinSQL);
                }
            }

            // Where条件
            if(this.conditionSQL!= null && this.conditionSQL.length()>0)
            {
               Matcher m  = SQL_WHERE_BRACKET.matcher(_sqlFinal);
               if(m.find())
               {
                   sqlBuild.append(this.conditionSQL);
               }else{
                   sqlBuild.append(" \nWHERE " + this.conditionSQL.substring(5));
               }
            }

            // 排序
            String orderSql = this.orderBySQL.toString();
            if (!StringUtils.isNullOrEmpty(orderSql)) {
                sqlBuild.append(" \nORDER BY "+ orderSql);
            }
            _sql = sqlBuild.toString();
        }
    }


    private SqlJoinClause joinClause(String joinType,String table)
    {
        this.joinSQL.append(" \n"+joinType+ " " + table);
        return new SqlJoinClause(this);
    }

    // endregion


}
