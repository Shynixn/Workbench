@file:Suppress("BlockingMethodInNonBlockingContext", "UNCHECKED_CAST")

package com.github.shynixn.workbench.bukkit.yaml.dsl

import com.fasterxml.jackson.core.type.TypeReference
import com.github.shynixn.workbench.bukkit.async.dsl.async
import com.github.shynixn.workbench.bukkit.yaml.implementation.Resource
import com.github.shynixn.workbench.bukkit.yaml.implementation.YamlWorkbenchResource
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
 * Registers a cached resource from file.
 */
inline fun <reified T> registerResource(noinline onload: suspend () -> T?, noinline onNotExist: suspend () -> T) {
    registerResource(object : TypeReference<T>() {}, onload, onNotExist)
}

/**
 * Registers a cached resource from file.
 */
fun <T> registerResource(typeInference: TypeReference<*>, onload: suspend () -> T?, onNotExist: suspend () -> T) {
    val fileResource = Resource(typeInference, onload, onNotExist, null)
    resource.fileCache[fileResource.typeReference.type] = fileResource
}

/**
 * Gets the data you want from the cache and fileSystem.
 */
suspend inline fun <reified T> get(): T {
    return get(object : TypeReference<T>() {})
}

/**
 * Gets the data you want from the cache and fileSystem.
 */
suspend fun <T> get(typeInference: TypeReference<*>): T {
    if (has<T>(typeInference)) {
        return resource.fileCache[typeInference.type]!!.cache!! as T
    }

    val internalResource = resource.fileCache[typeInference.type]!!
    val loadedData = internalResource.onload.invoke()

    if (loadedData != null) {
        internalResource.cache = loadedData
        return loadedData as T
    }

    val initialData = internalResource.onNotExist.invoke() as T
    internalResource.cache = initialData
    return initialData
}

/**
 * Checks if the cache has got the data.
 */
inline fun <reified T> has(): Boolean {
    return has<T>(object : TypeReference<T>() {})
}

/**
 * Checks if the cache has got the data.
 */
fun <T> has(typeInference: TypeReference<*>): Boolean {
    if (!resource.fileCache.containsKey(typeInference.type)) {
        throw IllegalArgumentException("Resource $typeInference has to be registered!")
    }

    return resource.fileCache[typeInference.type]!!.cache != null
}

/**
 * Loads the item async from the given file.
 */
suspend inline fun <reified T> loadFromYamlFile(file: Path): T? {
    return loadFromYamlFile(file, object : TypeReference<T>() {})
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