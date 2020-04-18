package com.github.shynixn.workbench.bukkit.async

import com.github.shynixn.workbench.bukkit.common.workbenchResource
import kotlinx.coroutines.*

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
    AsyncWorkbenchResource()

/**
 * Registers the resources if not already registered.
 */
private fun registerIfNotRegistered() {
    if (!resource.registered) {
        workbenchResource.registerSubResource(resource)
    }
}

/**
 * Launches a new coroutine scope within the game thread.
 */
fun <T> launch(f: suspend () -> T): Job {
    registerIfNotRegistered()
    return GlobalScope.launch(resource.syncDispatcher!!) {
        f.invoke()
    }
}

/**
 * Launches a new blocking coroutine scope within the game thread.
 */
fun <T> launchBlocking(f: suspend () -> T) {
    registerIfNotRegistered()
    runBlocking(resource.syncDispatcher!!) {
        f.invoke()
    }
}

/**
 * Launches a new coroutine scope within any async thread.
 */
fun <T> launchAsync(f: suspend () -> T): Job {
    registerIfNotRegistered()
    return GlobalScope.launch(resource.asyncDispatcher!!) {
        f.invoke()
    }
}

/**
 * Schedules an async operation on the current coroutine context.
 */
suspend fun <T> async(f: suspend () -> T): T {
    registerIfNotRegistered()
    return withContext(resource.asyncDispatcher!!) {
        f.invoke()
    }
}
