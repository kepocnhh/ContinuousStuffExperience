package continuous.stuff.experience

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class Lib1ClassTest {
    @Test
    fun m1Test() {
        val result = Lib1Class().m1()
        assertEquals(result, "string from lib: Lib1Class: m11")
    }

    /**
     * {Lib1Class.paramType} always return {Type.TYPE_1} so we check this
     * @see Lib1Class.paramType
     * @see Lib1Class.Type.TYPE_1
     */
    @Test
    fun paramTypeTest() {
        assertEquals(Lib1Class().paramType, Lib1Class.Type.TYPE_1)
    }

    @Test
    fun paramZeroTest() {
        assertEquals(Lib1Class().paramZero, 0)
    }

    @Test
    fun paramTrueTest() {
        assertEquals(Lib1Class().paramTrue, true)
    }

    @Test
    fun paramDateTest() {
        assertNotNull(Lib1Class().paramDate)
    }

    @Test
    fun dataClassTest() {
        val paramString = "test string"
        val dataClass = Lib1DataClass(paramString = paramString)
        assertEquals(paramString, dataClass.paramString)
    }
}
