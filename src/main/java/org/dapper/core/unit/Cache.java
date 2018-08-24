package org.dapper.core.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

  public  class Cache<TKey, TValue> {

    /**
     * 读写锁
     */
    private static ReadWriteLock _lock = new ReentrantReadWriteLock();

    /**
     * 缓存集合
     */
    private Map<TKey, TValue> _map = new HashMap<TKey, TValue>();


     /**
      * 获取缓存集合总数
      * @return
      */
    public int getCount()
    {
        return  _map.size();
    }


     /**
      * 获取缓存值
      * @param key 键名
      * @param factory 值
      * @return
      */
    public TValue get(TKey key,Callable<TValue> factory){

        // 检查缓存
        _lock.readLock().lock();
        TValue val;
        try{
            val = _map.getOrDefault(key, null);
            if(val!=null) { return val; }
        }finally {
            _lock.readLock().unlock();
        }


        // 初次读取,写入缓存
        _lock.writeLock().lock();
        try
        {
            // 再次检查
            val = _map.getOrDefault(key, null);
            if(val!=null) {  return val; }

            // 缓存
            try {
                val = factory.call();
            } catch (Exception e) {
                return  null;
            }
            _map.put(key, val);
            return val;
        } finally
        {
            _lock.writeLock().unlock();
        }
    }


     /**
      * 清除缓存
      */
    public void flush(){
        _lock.writeLock().lock();
        try
        {
            _map.clear();
        }
        finally
        {
            _lock.writeLock().unlock();
        }
    }
}
