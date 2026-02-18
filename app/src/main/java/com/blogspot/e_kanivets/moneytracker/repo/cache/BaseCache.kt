package com.blogspot.e_kanivets.moneytracker.repo.cache

import androidx.collection.LruCache
import com.blogspot.e_kanivets.moneytracker.entity.base.IEntity
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo

/**
 * Cache for [IRepo] instances. Use Decorator pattern.
 * Created on 4/19/16.
 *
 * @author Evgenii Kanivets
 */
class BaseCache<T : IEntity>(private val repo: IRepo<T>) : IRepo<T> {

    private val lruCache = LruCache<Long, T>(CACHE_SIZE)

    override fun create(instance: T?): T? {
        val createdT = repo.create(instance)
        if (createdT != null) lruCache.put(createdT.id, createdT)
        return createdT
    }

    override fun read(id: Long): T? {
        val cachedT = lruCache.get(id)
        return if (cachedT == null) {
            val readT = repo.read(id)
            if (readT != null) lruCache.put(readT.id, readT)
            readT
        } else cachedT
    }

    override fun update(instance: T?): T? {
        val updatedT = repo.update(instance)
        if (updatedT != null) lruCache.put(updatedT.id, updatedT)
        return updatedT
    }

    override fun delete(instance: T?): Boolean {
        val deleted = repo.delete(instance)
        if (instance != null && deleted) lruCache.remove(instance.id)
        return deleted
    }

    override fun readAll(): List<T> = repo.readAll()

    override fun readWithCondition(condition: String?, args: Array<String?>?): List<T> =
        repo.readWithCondition(condition, args)

    companion object {
        private const val CACHE_SIZE = 1024 * 1024 // 1MiB
    }
}
