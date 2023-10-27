package com.abogomazov.merchant.guide.storage.inmemory

class KeyValueInMemoryStorage<K, V> {
    private val registry = mutableMapOf<K, V>()

    fun get(key: K) = registry[key]
    fun getByValue(value: V) = registry.entries.singleOrNull { it.value == value }?.key
    fun set(key: K, value: V) {
        synchronized(this) { registry[key] = value }
    }
    fun delete(key: K) = synchronized(this) {
        registry.remove(key)
    }
}
