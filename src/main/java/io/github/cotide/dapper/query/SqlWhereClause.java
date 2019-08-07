package io.github.cotide.dapper.query;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.functions.TypeFunction;
import io.github.cotide.dapper.core.unit.Sql2oUtils;
import io.github.cotide.dapper.exceptions.SqlBuildException;
import io.github.cotide.dapper.query.base.BaseClause;

import java.util.List;


public class SqlWhereClause extends BaseClause {


    public SqlWhereClause(Sql sql) {
        super(sql);
    }

    public Sql between(Object a, Object b) {
        _sql.conditionSQL.append(" BETWEEN ? and ?");
        _sql.addParamValue(a);
        _sql.addParamValue(b);
        return _sql;
    }


    public Sql in(Object... args) {
        if (null == args || args.length == 0) {
            throw new SqlBuildException("Column query params is not null");
        }
        _sql.conditionSQL.append(" IN (");
        _sql.setInArguments(args);
        _sql.conditionSQL.append(")");
        return _sql;
    }

    public <S> Sql in(List<S> list) {
        return this.in(list.toArray());
    }

    public <S> Sql in(String column, List<S> args) {
        return this.in(column, args.toArray());
    }


    public Sql eq(Object value){
        _sql.conditionSQL.append(" = ?");
        _sql.addParamValue(value);
        return _sql;
    }

    public Sql notNull() {
        _sql.conditionSQL.append(" IS NOT NULL");
        return _sql;
    }

    public Sql notEq(Object value) {
        _sql.conditionSQL.append(" != ?");
        _sql.addParamValue(value);
        return _sql;
    }

    public Sql notEmpty(String columnName) {
        _sql.conditionSQL.append(" AND ");
        _sql.conditionSQL.append(columnName);
        _sql.conditionSQL.append(" != ''");
        return _sql;
    }

    public <T extends Entity,R> Sql notEmpty(
            TypeFunction<T, R> function) {
        return this.notEmpty(Sql2oUtils.getLambdaColumnName(function));
    }

    public Sql notEmpty() {
        _sql.conditionSQL.append(" != ''");
        return _sql;
    }

    public Sql notNull(String columnName) {
        _sql.conditionSQL.append(" AND ");
        _sql.conditionSQL.append(columnName);
        _sql.conditionSQL.append(" IS NOT NULL");
        return _sql;
    }

    public Sql like(Object value) {
        _sql.conditionSQL.append(" LIKE ?");
        _sql.addParamValue(value);
        return _sql;
    }

    public Sql gt(Object value) {
        _sql.conditionSQL.append(" > ?");
        _sql.addParamValue(value);
        return _sql;
    }

    public Sql gte(Object value) {
        _sql.conditionSQL.append(" >= ?");
        _sql.addParamValue(value);
        return _sql;
    }

    public Sql lt(Object value) {
        _sql.conditionSQL.append(" < ?");
        _sql.addParamValue(value);
        return _sql;
    }

    public Sql lte(Object value) {
        _sql.conditionSQL.append(" <= ?");
        _sql.addParamValue(value);
        return _sql;
    }

}
