package com.github.shynixn.workbench.bukkit.common

import com.github.shynixn.workbench.bukkit.common.Logger
import java.util.logging.Level

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
internal class LoggerImpl : Logger {
    var infoMessage: String? = null
    var warningMessage: String? = null
    var errorMessage: String? = null
    var throwable: Throwable? = null

    /**
     * Log message.
     */
    fun log(logger: java.util.logging.Logger) {
        val logPair = when {
            errorMessage != null -> {
                Pair(Level.SEVERE, errorMessage!!)
            }
            infoMessage != null -> {
                Pair(Level.INFO, infoMessage!!)
            }
            else -> {
                Pair(Level.WARNING, warningMessage!!)
            }
        }

        if (throwable != null) {
            logger.log(logPair.first, logPair.second, throwable!!)
        } else {
            logger.log(logPair.first, logPair.second)
        }
    }

    /**
     * Logs an information message.
     */
    override fun info(f: () -> String) {
        infoMessage = f.invoke()
    }

    /**
     * Logs a warning message.
     */
    override fun warning(f: () -> String) {
        warningMessage = f.invoke()
    }

    /**
     * Logs an error message.
     */
    override fun error(f: () -> String) {
        errorMessage = f.invoke()
    }

    /**
     * Logs an throwable.
     */
    override fun throwable(f: () -> Throwable) {
        throwable = f.invoke()
    }
}