package com.blogspot.e_kanivets.moneytracker.repo.base

/**
 * Interface that represents a contract of access to abstract storage with CRUD operations.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
interface IRepo<T> {
    fun create(instance: T?): T?
    fun read(id: Long): T?
    fun update(instance: T?): T?
    fun delete(instance: T?): Boolean
    fun readAll(): List<T>
    fun readWithCondition(condition: String?, args: Array<String?>?): List<T>
}
