package com.abogomazov.merchant.guide.application.inmemory

import java.util.concurrent.ConcurrentHashMap

class KeyValueInMemoryStorage<K, V> {
    private val registry = ConcurrentHashMap<K, V>()

    fun get(key: K) = registry[key]
    fun getByValue(value: V) = registry.entries.singleOrNull { it.value == value }?.key
    fun set(key: K, value: V) {
        registry[key] = value
    }
    fun delete(key: K) = registry.remove(key)
}
