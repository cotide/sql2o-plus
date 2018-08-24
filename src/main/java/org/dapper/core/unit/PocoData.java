package org.dapper.core.unit;

import org.dapper.core.provider.mapper.IMapper;
import org.dapper.core.provider.mapper.Mappers;
import org.dapper.core.unit.info.PocoColumn;
import org.dapper.core.unit.info.TableInfo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 表映射数据
 */
public class PocoData {


    private static Cache<Type, PocoData> _pocoDatas = new Cache<Type, PocoData>();


    private Map<String,PocoColumn> _column  =   new HashMap<String, PocoColumn>();

    public Map<String, PocoColumn> getColumns(){
        return _column;
    }

    /**
     * 表信息
     */
    private TableInfo tableInfo;

    /**
     * 获取表信息
     * @return
     */
    public  TableInfo getTableInfo(){
        return  tableInfo;
    }

    /**
     * 当前映射实体类型
     */
    private Type type;


    public Type getType(){
        return  type;
    }

    public void setType(Type type){
        this.type = type;
    }

    /**
     * 字段信息
     */
    private Map<String,PocoColumn> map = new HashMap<String,PocoColumn>();

    public PocoData(Class<?> type, IMapper defaultMapper){
        this.type = type;
        IMapper mapper = Mappers.getMapper(type, defaultMapper);
        tableInfo = mapper.GetTableInfo(type);
        _column = new HashMap<String, PocoColumn>();
    }

    /**
     * 清除当前对象缓存数据
     */
    public static void flushCaches()
    {
        _pocoDatas.flush();
    }

}
