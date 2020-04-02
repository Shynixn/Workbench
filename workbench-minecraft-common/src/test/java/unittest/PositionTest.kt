package unittest

import com.github.shynixn.workbench.minecraft.common.dsl.Position.Companion.blockX
import com.github.shynixn.workbench.minecraft.common.dsl.Position.Companion.blockY
import com.github.shynixn.workbench.minecraft.common.dsl.Position.Companion.blockZ
import com.github.shynixn.workbench.minecraft.common.dsl.Position.Companion.pitch
import com.github.shynixn.workbench.minecraft.common.dsl.Position.Companion.worldName
import com.github.shynixn.workbench.minecraft.common.dsl.Position.Companion.x
import com.github.shynixn.workbench.minecraft.common.dsl.Position.Companion.y
import com.github.shynixn.workbench.minecraft.common.dsl.Position.Companion.yaw
import com.github.shynixn.workbench.minecraft.common.dsl.Position.Companion.z
import com.github.shynixn.workbench.minecraft.common.dsl.matches
import com.github.shynixn.workbench.minecraft.common.dsl.matchesExcept
import com.github.shynixn.workbench.minecraft.common.dsl.position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

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
class PositionTest {
    /**
     * Given
     *      a new position
     * When
     *      add is called
     * Then
     *     data should be added to the position.
     */
    @Test
    fun add_NewPosition_ShouldCalculateCorrectly() {
        // Arrange
        val classUnderTest = position {
            x = 4.0
            y = 592.0
            z = 30.0
            yaw = 3.0
            pitch = 2.0
        }
        val addPosition = position {
            x = 4.0
            y = 40.0
            z = 30.0
            yaw = 4.3
            pitch = 9.5
        }

        // Act
        classUnderTest.add(addPosition)

        // Assert
        assertEquals(8.0, classUnderTest.x)
        assertEquals(632.0, classUnderTest.y)
        assertEquals(60.0, classUnderTest.z)
        assertEquals(7.3, classUnderTest.yaw)
        assertEquals(11.5, classUnderTest.pitch)
    }

    /**
     * Given
     *      a new position
     * When
     *      multiply is called
     * Then
     *     x, y, z should be multiplied. Yaw and pitch stay the same.
     */
    @Test
    fun multiply_NewPosition_ShouldCalculateCorrectly() {
        // Arrange
        val classUnderTest = position {
            x = 4.0
            y = 592.0
            z = 30.0
            yaw = 3.0
            pitch = 2.0
        }

        // Act
        classUnderTest.multiply(3.5)

        // Assert
        assertEquals(14.0, classUnderTest.x)
        assertEquals(2072.0, classUnderTest.y)
        assertEquals(105.0, classUnderTest.z)
        assertEquals(3.0, classUnderTest.yaw)
        assertEquals(2.0, classUnderTest.pitch)
    }

    /**
     * Given
     *      two positions with the same value
     * When
     *      equal is called
     * Then
     *     true should be returned.
     */
    @Test
    fun equal_NewPositions_ShouldMatch() {
        // Arrange
        val classUnderTest = position {
            x = 4.0
            y = 592.0
            z = 30.0
            yaw = 3.0
            pitch = 2.0
            worldName = "world"
        }

        // Act
        val otherPosition = position {
            x = 4.0
            y = 592.0
            z = 30.0
            yaw = 3.0
            pitch = 2.0
            worldName = "world"
        }
        val positionString = classUnderTest.toString()
        val clonedPosition = classUnderTest.clone()
        val clonedPositionString = clonedPosition.toString()

        // Assert
        assertEquals(otherPosition, classUnderTest)
        assertEquals(clonedPosition, classUnderTest)
        assertEquals(clonedPositionString, positionString)
    }

    /**
     * Given
     *      two positions in different worlds.
     * When
     *      distance is called
     * Then
     *     DOUBLE.MAX should be returned.
     */
    @Test
    fun distance_NewPositionsInDifferentWorlds_ShouldBeCalculatedCorrectly() {
        // Arrange
        val classUnderTest = position {
            x = 4.0
            y = 592.0
            z = 30.0
            yaw = 3.0
            pitch = 2.0
            worldName = "world"
        }
        val otherPosition = position {
            x = -4.0
            y = 25.0
            z = 303.5
            yaw = 3.0
            pitch = 2.0
            worldName = "world_nether"
        }
        val expectedDistance = Double.MAX_VALUE

        // Act
        val distance = classUnderTest.distance(otherPosition)

        // Assert
        assertEquals(expectedDistance, distance)
    }

    /**
     * Given
     *      two positions
     * When
     *      distance is called
     * Then
     *     the correct distance should be returned.
     */
    @Test
    fun distance_NewPositions_ShouldBeCalculatedCorrectly() {
        // Arrange
        val classUnderTest = position {
            x = 4.0
            y = 592.0
            z = 30.0
            yaw = 3.0
            pitch = 2.0
            worldName = "world"
        }
        val otherPosition = position {
            x = -4.0
            y = 25.0
            z = 303.5
            yaw = 3.0
            pitch = 2.0
            worldName = "world"
        }
        val expectedDistance = 629.5675102798746

        // Act
        val distance = classUnderTest.distance(otherPosition)

        // Assert
        assertEquals(expectedDistance, distance)
    }

    /**
     * Given
     *      two positions
     * When
     *      match is called
     * Then
     *     a match should be determined
     */
    @Test
    fun match_NewPositions_ShouldBeCalculatedCorrectly() {
        // Arrange
        val classUnderTest = position {
            x = 4.0
            y = 592.0
            z = 30.0
            yaw = 3.0
            pitch = 2.0
            worldName = "world"
        }
        val otherPosition = position {
            x = -4.0
            y = 25.0
            z = 303.5
            yaw = 3.0
            pitch = 2.0
            worldName = "world"
        }

        // Assert
        assertNotEquals(classUnderTest, otherPosition)

        assertTrue(classUnderTest.matches(otherPosition))
        assertTrue(classUnderTest.matches(otherPosition, yaw, pitch))
        assertFalse(classUnderTest.matches(otherPosition, x, y, z))
        assertTrue(classUnderTest.matches(otherPosition, worldName))

        assertFalse(classUnderTest.matchesExcept(otherPosition))
        assertTrue(classUnderTest.matchesExcept(otherPosition, x, y, z, blockX, blockY, blockZ))
        assertFalse(classUnderTest.matchesExcept(otherPosition, x, y))
    }
}