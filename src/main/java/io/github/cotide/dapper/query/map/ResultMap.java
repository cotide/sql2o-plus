package io.github.cotide.dapper.query.map;


import com.sun.org.apache.bcel.internal.generic.RET;
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


    public <T1 extends Entity, R1> ResultMap put(
            String fromAsTable,
            TypeFunction<T1, R1> source,
            String asName){

        if(fromAsTable !=null && !fromAsTable.trim().equals(""))
        {
            selectColumn.append(fromAsTable+".");
        }

        String columnName = Sql2oUtils.getLambdaColumnName(source);
        if (asName != null && !asName.trim().equals("")) {
            columnName = columnName + "  as " + asName;
        }

        selectColumn.append(columnName + ",\n");
        return  this;

    }

    public <T1 extends Entity, R1> ResultMap put(
            TypeFunction<T1, R1> source,
            String asName){
        return put(null,source,asName);
    }

     public <T1 extends Entity, R1, T2, R2> ResultMap put(
            TypeFunction<T1, R1> source,
            TypeFunction<T2, R2> target) {

        return put(null,source,Sql2oUtils.getDtoLambdaColumnName(target));
    }

    public <T1 extends Entity, R1, T2, R2> ResultMap put(
            String fromAsTable,
            TypeFunction<T1, R1> source,
            TypeFunction<T2, R2> target) {

        return put(fromAsTable,source,Sql2oUtils.getDtoLambdaColumnName(target));
    }

    public <T1 extends Entity, R1> ResultMap put(
            TypeFunction<T1, R1> source){
        return put(null,source,"");
    }


    public <T1 extends Entity, R1> ResultMap put(
            String fromAsTable,
            TypeFunction<T1, R1> source){
        return put(fromAsTable,source,"");
    }

}
