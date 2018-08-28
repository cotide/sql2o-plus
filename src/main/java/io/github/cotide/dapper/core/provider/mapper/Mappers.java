package io.github.cotide.dapper.core.provider.mapper;

import io.github.cotide.dapper.core.unit.PocoData;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public  class Mappers {

    /**
     * 映射缓存
     */
    private static Map<Object, IMapper> _mappers = new HashMap<Object, IMapper>();

    /**
     * 读写锁
     */
    private static ReadWriteLock _lock = new ReentrantReadWriteLock();


    public static IMapper getMapper(Class<?> entityType, IMapper defaultMapper)
    {
        _lock.readLock().lock();
        try
        {
           return  _mappers.getOrDefault(entityType,defaultMapper);
        }
        finally
        {
            _lock.readLock().unlock();
        }
    }


    // region

    private static void registerInternal(Object typeOrAssembly, IMapper mapper)
    {
        _lock.writeLock().lock();
        try
        {
            _mappers.put(typeOrAssembly, mapper);
        }
        finally
        {
            _lock.writeLock().unlock();
            flushCaches();
        }
    }


    private static void flushCaches()
    {
        PocoData.flushCaches();
    }

    //endregion

}
