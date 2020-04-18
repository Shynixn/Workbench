package com.github.shynixn.workbench.bukkit.testsuite

import com.github.shynixn.workbench.bukkit.common.ChatColor
import com.github.shynixn.workbench.bukkit.common.clearAllEntities
import com.github.shynixn.workbench.bukkit.common.player
import com.github.shynixn.workbench.bukkit.common.workbenchResource
import com.github.shynixn.workbench.bukkit.testsuite.angel.*
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.util.*


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
class WorkBenchTestSuitePlugin : JavaPlugin() {
    private val prefix = ChatColor.GREEN.toString() + "[TestSuite] " + ChatColor.WHITE
    val entityRegistration = EntityRegistration114R1ServiceImpl()

    /**
     * OnEnable.
     */
    override fun onEnable() {
        workbenchResource.onEnable(this)
        val player = player { "Shynixn" }

        clearAllEntities()

        Bukkit.getServer().worlds.stream().forEach { world ->
            world.entities.forEach { e ->
                if (e !is Player) {
                    try {
                        (e as Any).javaClass.getDeclaredMethod("deleteFromWorld").invoke(e)
                    } catch (ex: Exception) {
                        e.remove()
                    }
                }
            }
        }


        entityRegistration.register(WeepingAngelZombie::class.java, EntityType.ZOMBIE)

        val weepingAngle = WeepingAngleImpl(player.location)


        /*  val server = (Bukkit.getServer() as CraftServer).getServer() as MinecraftServer
          val world= (Bukkit.getWorlds().get(0) as CraftWorld).getHandle()
          val npc = EntityPlayer(
              server,
              world,
              generateGameProfile("http://textures.minecraft.net/texture/70d806f19a7526fc6fa48f42f703b90add8142dac7f37ad1be1b4edcac5ba83", null),
              PlayerInteractManager(world)
          )

          npc.setLocation(player.location.x, player.location.y, player.location.z, player.location.yaw, player.location.pitch)

          for (p in Bukkit.getOnlinePlayers()) {
              val connection = (p as CraftPlayer).getHandle().playerConnection
              connection.sendPacket(PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc))
              connection.sendPacket(PacketPlayOutNamedEntitySpawn(npc))
          }*/

        //  video.download(  video.audioFormats().get(0), File("testdownload/audio.mp3"))

        /*  launch {
              player.sendMessage(prefix + "Loading [Particle] testsuite....")
              for(i in 0 until 10){
                  player.sendMessage("Playing [Particle] testsuite ${i+1}")
                  delay(1500)
                  val particleTestSuite = ParticleTestSuite()
                  particleTestSuite.play(player)
              }

              player.sendMessage(prefix + "Completed [Particle] testsuite.")
              player.sendMessage(prefix + "Completed testsuite.")
          }*/
    }

    private fun generateGameProfile(skin: String, name: String?): GameProfile {
        var newSkin = skin
        val newSkinProfile = GameProfile(UUID.randomUUID(), name)

        if (newSkin.contains("textures.minecraft.net")) {
            if (!newSkin.startsWith("http://")) {
                newSkin = "http://$newSkin"
            }

            newSkin = Base64Coder.encodeString("{textures:{SKIN:{url:\"$newSkin\"}}}")
        }

        newSkinProfile.properties.put("textures", Property("textures", newSkin))
        return newSkinProfile
    }

    override fun onDisable() {
        workbenchResource.onDisable()
        entityRegistration.clearResources()
    }
}