package continuous.stuff.experience

import org.junit.jupiter.api.Assertions.assertEquals
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
}
