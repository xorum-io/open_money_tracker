package com.blogspot.e_kanivets.moneytracker.repo.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

import com.blogspot.e_kanivets.moneytracker.entity.base.IEntity;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import java.util.List;

/**
 * Cache for {@link IRepo} instances. Use Decorator pattern.
 * Created on 4/19/16.
 *
 * @author Evgenii Kanivets
 */
public class BaseCache<T extends IEntity> implements IRepo<T> {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseCache";

    private static final int CACHE_SIZE = 1024 * 1024; // 1MiB

    private LruCache<Long, T> lruCache;

    @NonNull
    private IRepo<T> repo;

    public BaseCache(@NonNull IRepo<T> repo) {
        this.repo = repo;
        lruCache = new LruCache<>(CACHE_SIZE);
    }

    @Nullable
    @Override
    public T create(@Nullable T instance) {
        T createdT = repo.create(instance);
        if (createdT != null) lruCache.put(createdT.getId(), createdT);
        return createdT;
    }

    @Nullable
    @Override
    public T read(long id) {
        T cachedT = lruCache.get(id);
        if (cachedT == null) {
            T readT = repo.read(id);
            if (readT != null) lruCache.put(readT.getId(), readT);
            return readT;
        } else return cachedT;
    }

    @Nullable
    @Override
    public T update(@Nullable T instance) {
        T updatedT = repo.update(instance);
        if (updatedT != null) lruCache.put(updatedT.getId(), updatedT);
        return updatedT;
    }

    @Override
    public boolean delete(@Nullable T instance) {
        boolean deleted = repo.delete(instance);
        if (instance != null && deleted) lruCache.remove(instance.getId());
        return deleted;
    }

    @NonNull
    @Override
    public List<T> readAll() {
        return repo.readAll();
    }

    @NonNull
    @Override
    public List<T> readWithCondition(@Nullable String condition, @Nullable String[] args) {
        return repo.readWithCondition(condition, args);
    }
}
