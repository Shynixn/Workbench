@file:Suppress("BlockingMethodInNonBlockingContext", "UNCHECKED_CAST")

package com.github.shynixn.workbench.bukkit.yaml

import com.fasterxml.jackson.core.type.TypeReference
import com.github.shynixn.workbench.bukkit.async.async
import kotlinx.coroutines.delay
import org.bukkit.entity.Player
import java.nio.file.Files
import java.nio.file.Path

/**
 * Created by Shynixn 2020.
 * <p>
 * Version 1.5
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2020 by Shynixn
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

private var resource =
    YamlWorkbenchResource()

/**
 * Saves the given item async to the given file.
 */
suspend fun <T> saveToYamlFile(item: T, file: Path) {
    async {
        Files.createDirectories(file.parent)
        resource.objectMapper.writeValue(file.toFile(), item)
    }
}

/**
 * Registers a cached resource.
 */
inline fun <reified T> registerResource(
    noinline onload: suspend () -> T?,
    noinline onNotExist: suspend () -> T,
    noinline onSave: suspend (T) -> Unit
) {
    registerResource(
        object : TypeReference<T>() {},
        onload,
        onNotExist,
        onSave
    )
}

/**
 * Registers a cached resource.
 */
fun <T> registerResource(
    typeInference: TypeReference<*>,
    onload: suspend () -> T?,
    onNotExist: suspend () -> T,
    onSave: suspend (T) -> Unit
) {
    val fileResource =
        Resource(
            typeInference,
            { onload.invoke() },
            { onNotExist.invoke() },
            onSave as (suspend (Any) -> Unit)
        )
    resource.commonCache[fileResource.typeReference.type] = fileResource
}

/**
 * Registers a cached resource.
 */
inline fun <reified T> registerPlayerResource(
    noinline onload: suspend (Player) -> T?,
    noinline onNotExist: suspend (Player) -> T,
    noinline onSave: suspend (Pair<Player, T>) -> Unit
) {
    registerPlayerResource(
        object : TypeReference<T>() {},
        onload,
        onNotExist,
        onSave
    )
}

/**
 * Registers a cached resource.
 */
fun <T> registerPlayerResource(
    typeInference: TypeReference<*>,
    onload: suspend (Player) -> T?,
    onNotExist: suspend (Player) -> T,
    onSave: suspend (Pair<Player, T>) -> Unit
) {
    val fileResource =
        Resource(
            typeInference,
            onload as suspend (Any) -> T?,
            onNotExist as suspend (Any) -> T?,
            onSave as (suspend (Any) -> Unit)
        )
    resource.commonCache[fileResource.typeReference.type] = fileResource
}

/**
 * Gets the data from the player.
 */
suspend inline fun <reified T> Player.get(): T {
    return get(object : TypeReference<T>() {})
}

/**
 * Gets the data from the player.
 */
suspend fun <T> Player.get(typeInference: TypeReference<*>): T {
    if (has(typeInference)) {
        val internalResource = resource.commonCache[typeInference.type]!!
        return internalResource.playerCache!![uniqueId] as T
    }

    val internalResource = resource.commonCache[typeInference.type]!!

    if (internalResource.playerCache == null) {
        internalResource.playerCache = HashMap()
    }

    if (internalResource.loadingPlayerCache == null) {
        internalResource.loadingPlayerCache = HashSet()
    }

    while (true) {
        if (!internalResource.loadingPlayerCache!!.contains(this.uniqueId)) {
            if (has(typeInference)) {
                return internalResource.playerCache!![uniqueId] as T
            }

            break
        }

        delay(500)
    }

    internalResource.loadingPlayerCache!!.add(uniqueId)
    val loadedData = internalResource.onload.invoke(this)
    internalResource.loadingPlayerCache!!.remove(uniqueId)

    if (loadedData != null) {
        internalResource.cache = loadedData
        return loadedData as T
    }

    val initialData = internalResource.onNotExist.invoke(this) as T
    internalResource.cache = initialData
    return initialData
}

/**
 * Checks if the player cache has got the data.
 */
inline fun <reified T> Player.has(): Boolean {
    return has(object : TypeReference<T>() {})
}

/**
 * Checks if the player cache has got the data.
 */
fun Player.has(typeInference: TypeReference<*>): Boolean {
    if (!resource.commonCache.containsKey(typeInference.type)) {
        throw IllegalArgumentException("Resource $typeInference has to be registered!")
    }

    val internalResource = resource.commonCache[typeInference.type]!!
    return internalResource.playerCache != null && internalResource.playerCache!!.containsKey(uniqueId) && internalResource.playerCache!![uniqueId] != null
}

/**
 * Gets the data from the cache and fileSystem.
 */
suspend inline fun <reified T> get(): T {
    return get(object : TypeReference<T>() {})
}

/**
 * Gets the data from the cache and fileSystem.
 */
suspend fun <T> get(typeInference: TypeReference<*>): T {
    if (has(typeInference)) {
        return resource.commonCache[typeInference.type]!!.cache!! as T
    }

    val internalResource = resource.commonCache[typeInference.type]!!
    val loadedData = internalResource.onload.invoke(Unit)

    if (loadedData != null) {
        internalResource.cache = loadedData
        return loadedData as T
    }

    val initialData = internalResource.onNotExist.invoke(Unit) as T
    internalResource.cache = initialData
    return initialData
}

/**
 * Checks if the cache has got the data.
 */
inline fun <reified T> has(): Boolean {
    return has(object : TypeReference<T>() {})
}

/**
 * Checks if the cache has got the data.
 */
fun has(typeInference: TypeReference<*>): Boolean {
    if (!resource.commonCache.containsKey(typeInference.type)) {
        throw IllegalArgumentException("Resource $typeInference has to be registered!")
    }

    return resource.commonCache[typeInference.type]!!.cache != null
}

/**
 * Loads the item async from the given file.
 */
suspend inline fun <reified T> loadFromYamlFile(file: Path): T? {
    return loadFromYamlFile(
        file,
        object : TypeReference<T>() {})
}

/**
 * Loads from internal yaml file.
 */
suspend fun <T> loadFromYamlFile(file: Path, typeInference: TypeReference<*>): T? {
    return async {
        Files.createDirectories(file.parent)
        if (!Files.exists(file)) {
            null
        } else {
            resource.objectMapper.readValue<T>(file.toFile(), typeInference)
        }
    }
}