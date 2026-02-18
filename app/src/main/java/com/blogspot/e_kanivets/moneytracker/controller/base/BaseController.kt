package com.blogspot.e_kanivets.moneytracker.controller.base

import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo

/**
 * This is just a wrapper for [IRepo] interface.
 * Don't use Repo classes outside of controllers.
 * Created on 2/17/16.
 *
 * @author Evgenii Kanivets
 */
abstract class BaseController<T>(repo: IRepo<T>) : IRepo<T> {

    @JvmField
    protected val repo: IRepo<T> = repo


    override fun create(instance: T?): T? = repo.create(instance)

    override fun read(id: Long): T? = repo.read(id)

    override fun update(instance: T?): T? = repo.update(instance)

    override fun delete(instance: T?): Boolean = repo.delete(instance)

    override fun readAll(): List<T> = repo.readAll()

    override fun readWithCondition(condition: String?, args: Array<String?>?): List<T> =
        repo.readWithCondition(condition, args)
}
