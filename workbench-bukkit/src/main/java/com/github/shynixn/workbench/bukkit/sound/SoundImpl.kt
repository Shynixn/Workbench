package com.github.shynixn.workbench.bukkit.sound

import com.github.shynixn.workbench.bukkit.common.log
import org.bukkit.Location
import org.bukkit.entity.Player

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
class SoundImpl : Sound {
    /**
     * Gets or sets the sound name. Supports cross platform compatibility by
     * executing multiple sounds split by ','.
     */
    override var name: String = ""
    /**
     * Pitch of the sound between 0.0 and 2.0.
     */
    override var pitch: Double = 1.0
    /**
     * Range of blocks where the sound can be heard.
     */
    override var volume: Double = 10.0

    /**
     * Plays the sound.
     */
    override fun play(location: Location, vararg players: Player) {
        this.play(location, players.asList())
    }

    /**
     * Plays the sound.
     */
    override fun play(location: Location, players: Collection<Player>) {
        var sound = findSoundByName(this.name)

        if (sound == null) {
            sound = this.name.split(",").asSequence().map { e -> findSoundByName(e) }.filter { e -> e != null }
                .firstOrNull()
        }

        if (sound == null) {
            log {
                warning { "Failed to play sound '$name'." }
            }
            return
        }

        for (player in players) {
            player.playSound(location, sound, volume.toFloat(), pitch.toFloat())
        }
    }

    /**
     * Tries to locate the corresponding sound.
     */
    private fun findSoundByName(name: String): org.bukkit.Sound? {
        return try {
            org.bukkit.Sound.valueOf(name)
        } catch (e: Exception) {
            null
        }
    }
}