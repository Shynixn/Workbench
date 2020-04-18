@file:Suppress("SENSELESS_COMPARISON")

package unittest.async

import com.github.shynixn.workbench.bukkit.async.launchBlocking
import com.github.shynixn.workbench.bukkit.yaml.*
import helper.MockServer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

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
class DSLTest {

    @BeforeEach
    internal fun setUp() {
        MockServer.init()
    }

    @Test
    fun saveAndLoadTest() {
        launchBlocking {
            val outPerson = Person("Christoph", "Mayr", arrayListOf(Equipment(50)))

            saveToYamlFile(
                outPerson,
                Paths.get("build/testcases/test.yml")
            )
            val inPerson = loadFromYamlFile<Person>(
                Paths.get(
                    "build/testcases/test.yml"
                )
            )!!

            assertEquals(outPerson.firstName, inPerson.firstName)
            assertEquals(outPerson.lastName, inPerson.lastName)
        }
    }

    @Test
    fun getOfExistingFile() {
        launchBlocking {
            val initialPersons = listOf(Person("Christoph", "Mayr", arrayListOf(Equipment(50))))
            saveToYamlFile(
                initialPersons,
                Paths.get("build/testcases/existingfile.yml")
            )

            registerResource(suspend {
                loadFromYamlFile<List<Person>>(Paths.get("build/testcases/existingfile.yml"))
            }, suspend {
                arrayListOf<Person>()
            }, { item ->
                suspend {
                    saveToYamlFile(
                        item,
                        Paths.get("build/testcases/existingfile.yml")
                    )
                }
            })
            val getGlobalPersonsFromFile =
                get<List<Person>>()
            val getGlobalPersonsFromCache =
                get<List<Person>>()

            assertEquals(initialPersons, getGlobalPersonsFromCache)
            assertEquals(initialPersons, getGlobalPersonsFromFile)
        }

        has<List<Person>>()
    }

    data class Person(
        var firstName: String = "",
        var lastName: String = "",
        var equipments: List<Equipment> = ArrayList()
    )

    data class Equipment(var shovel: Int = 5)
}