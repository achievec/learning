package org.aloha;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 显示锁 和 独占锁，显示锁可用于一些可定时 可轮询 可中断的锁获取操作中
 * 
 * @author aloha
 *
 * @param <K>
 * @param <V>
 */
public class ReadWriteMap<K, V> extends AbstractMap<K, V> {

    private Map<K, V> innerMap;

    /**
     * 默认非公平锁
     */
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock readLock = readWriteLock.readLock();

    private Lock writeLock = readWriteLock.writeLock();

    public ReadWriteMap(Map<K, V> map) {
        innerMap = map;
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return innerMap.entrySet();
    }

    @Override
    public V get(Object key) {
        readLock.lock();
        try {
            return innerMap.get(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        writeLock.lock();
        try {
            return innerMap.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

}
