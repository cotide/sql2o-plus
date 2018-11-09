package io.github.cotide.dapper.query.map;


import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.functions.TypeFunction;
import io.github.cotide.dapper.core.unit.Sql2oUtils;
import io.github.cotide.dapper.query.Sql;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ResultMap {

    private StringBuilder selectColumn = new StringBuilder();


    public String getSelectColumn(){
        return selectColumn.toString();
    }

    public <T1 extends Entity, R1, T2, R2> void put(
            TypeFunction<T1, R1> source,
            TypeFunction<T2, R2> target) {

        String columnName = Sql2oUtils.getLambdaColumnName(source);
        if (target != null) {
            columnName = columnName + "  as " + Sql2oUtils.getDtoLambdaColumnName(target);
        }

        selectColumn.append(columnName + ",\n");
    }

    public <T1 extends Entity, R1> void put(
            TypeFunction<T1, R1> source,
            String asName) {

        String columnName = Sql2oUtils.getLambdaColumnName(source);
        if (asName != null && !asName.trim().equals("")) {
            columnName = columnName + "  as " + asName;
        }

        selectColumn.append(columnName + ",\n");
    }

    public <T1 extends Entity, R1> void put(
            TypeFunction<T1, R1> source) {
        put(source,"");
    }
}
