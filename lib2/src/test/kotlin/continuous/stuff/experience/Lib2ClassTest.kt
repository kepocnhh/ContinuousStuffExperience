package continuous.stuff.experience

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class Lib2ClassTest {
    @Test
    fun sumOfTest() {
        val int1 = 2
        val int2 = 3
        val result = Lib2Class().sumOf(int1, int2)
        assertEquals(result, int1 + int2)
    }

    @Test
    fun m2Test() {
        val result = Lib2Class().m2()
        assertEquals(result, "string from lib2: Lib2Class: m2")
    }

    @Test
    fun nextOrNullTest() {
        val list = listOf("one", "two")
        val result = Lib2Class().nextOrNull(list)
        assertTrue(list.contains(result))
    }

    @Test
    fun nextOrNullTestEmpty() {
        val list = emptyList<String>()
        assertNull(Lib2Class().nextOrNull(list))
    }
}
