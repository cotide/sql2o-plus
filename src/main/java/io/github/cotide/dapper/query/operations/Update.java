package io.github.cotide.dapper.query.operations;

import com.mysql.cj.util.StringUtils;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.functions.TypeFunction;
import io.github.cotide.dapper.core.unit.Sql2oUtils;
import io.github.cotide.dapper.exceptions.SqlBuildException;

import java.util.HashMap;
import java.util.Map;

public class Update<TModel extends Entity> {

    private Map<String,Object> ops = new HashMap<>();

    private final Class<TModel> clazz;


    public Update(Class<TModel> type) {
        this.clazz = type;
    }

    public <R> Update<TModel> set(TypeFunction<TModel,R> function,Object value)
    {
        if(value==null)
        {
            throw new SqlBuildException("Value cannot be null");
        }
        set(Sql2oUtils.getLambdaColumnName(function),value);
        return this;
    }


    public  Update<TModel> set(String columnName,Object value)
    {
        if(StringUtils.isNullOrEmpty(columnName))
        {
            throw new SqlBuildException("ColumnName cannot be null");
        }
        ops.put(columnName,value);
        return this;
    }



    public Map<String,Object> getOps(){
        return ops;
    }


    public  boolean isHaveOps(){
      return ops!=null  && ops.size()>0;
    }
}
