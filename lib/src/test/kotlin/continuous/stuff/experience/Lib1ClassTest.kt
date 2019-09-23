package continuous.stuff.experience

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Lib1ClassTest {
	@Test
	fun m1Test() {
		val result = Lib1Class().m1()
		assertEquals(result, "string from lib: Lib1Class: m1")
	}
}