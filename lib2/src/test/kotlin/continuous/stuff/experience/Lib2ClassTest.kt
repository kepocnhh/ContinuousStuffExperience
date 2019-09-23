package continuous.stuff.experience

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Lib2ClassTest {
	@Test
	fun m1Test() {
		val result = Lib2Class().m1()
		assertEquals(result, "string from lib2: Lib2Class: m1")
	}

	@Test
	fun m2Test() {
		val result = Lib2Class().m2()
		assertEquals(result, "string from lib2: Lib2Class: m2")
	}
}