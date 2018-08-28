package org.dapper.query;

import org.dapper.basic.domain.base.Entity;
import org.dapper.core.exceptions.SqlBuildException;
import org.dapper.core.unit.info.TableInfo;

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


    public Sql whereIn(String column, Object... paras) throws SqlBuildException {
        if (column == null || column.length() == 0)
            throw new SqlBuildException("query error: must have 'in' word");
        if (paras == null || paras.length == 0)
            throw new SqlBuildException("paras error");

        if(paras.length>0)
        {
//            if(paras[0] instanceof Integer )
//            {
//                StringBuffer appendValue =  new StringBuffer();
//                for (Object value: paras ) {
//                    appendValue.append(value+",");
//                }
//                String appendSql =   String.format("%s IN (%s)",column,trimEnd(appendValue.toString(),','));
//                return where(appendSql);
//            }else if (paras[0] instanceof String )
//            {
                    StringBuffer appendValue =  new StringBuffer();
                    int index = 0;
                    for (Object value: paras ) {
                        appendValue.append("@"+index+",");
                        ++index;
                    }
                    String appendSql =   String.format("%s in (%s)",column,trimEnd(appendValue.toString(),','));
                    return where(appendSql,paras);
//            }
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
        String tableName  = TableInfo.fromPoco(modelClass).getTableName();
        return append(new Sql("from " +  tableName));
    }


    public Sql orderBy(String columns) {
        return append(new Sql("order by " + columns));
    }

    public Sql groupBy(String columns) {
        return append(new Sql("order by " + columns));
    }


    public String getFinalSql() {
        build();
        return buildSql();
    }

    public Object[] getFinalArgs() {
        build();
        return _argsFinal;
    }

    //#region  Helper

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
            return;

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
                sql = "and " + sql.substring(6);
            if (is(lhs, "order by ") && is(this, "order by "))
                sql = ", " + sql.substring(9);

            for (Object arg : _args) {
                args.add(arg);
            }
            sb.append(sql);
        }

        if (_rhs != null)
            _rhs.build(sb, args, this);
    }

    private static boolean is(Sql sql, String sqlType) {
        return sql != null
                && sql._sql != null
                && sql._sql.toLowerCase().startsWith(sqlType.toLowerCase());
    }

    private String trimEnd(String s, char c) {
        int i;
        for(i = 0; i < s.length() && s.charAt(i) == c; ++i);
        return s.substring(i);
    }
    //#endregion

}
