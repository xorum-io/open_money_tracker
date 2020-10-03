package com.blogspot.e_kanivets.moneytracker.controller.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import java.util.List;

/**
 * This is just a wrapper for {@link IRepo} interface.
 * Don't use Repo classes outside of controllers.
 * Created on 2/17/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseController<T> implements IRepo<T> {
    protected IRepo<T> repo;

    public BaseController(IRepo<T> repo) {
        this.repo = repo;
    }

    @Nullable
    @Override
    public T create(T instance) {
        return repo.create(instance);
    }

    @Nullable
    @Override
    public T read(long id) {
        return repo.read(id);
    }

    @Nullable
    @Override
    public T update(T instance) {
        return repo.update(instance);
    }

    @Override
    public boolean delete(T instance) {
        return repo.delete(instance);
    }

    @NonNull
    @Override
    public List<T> readAll() {
        return repo.readAll();
    }

    @NonNull
    @Override
    public List<T> readWithCondition(String condition, String[] args) {
        return repo.readWithCondition(condition, args);
    }
}
