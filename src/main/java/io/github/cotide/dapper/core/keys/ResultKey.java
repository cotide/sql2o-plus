package io.github.cotide.dapper.core.keys;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResultKey {

    private  List<Object> keys;

    public ResultKey(Object key){
        this.keys = Arrays.asList ((Object[])key);
    }

    public <T> T asResult(Class<T> modelClass)
    {
        Object key = keys.get(0);
        if(key==null)
        {
            return null;
        }
        return (T)key;
    }

    public <T> List<T> asResults(Class<T> modelClass){

        return  keys.stream().map(x->(T)x).collect(Collectors.toList());
    }

    public Integer asInt(){

        Object key = keys.get(0);
        if(key==null) {
            return null;
        }
        if(key instanceof Long)
        {
            return asLong().intValue();
        }
        if(key instanceof BigInteger)
        {
            return ((BigInteger)key).intValue();
        }
        return (Integer)key;
    }

    public Long asLong(){
        Object key = keys.get(0);
        if(key==null)
        {
           return null;
        }
        return (Long) key;
    }

    public String asString(){
        Object key = keys.get(0);
        if(key==null)
        {
            return null;
        }
        return key.toString();
    }
}
