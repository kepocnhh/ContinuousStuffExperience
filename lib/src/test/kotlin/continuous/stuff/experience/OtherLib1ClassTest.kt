package continuous.stuff.experience

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class OtherLib1ClassTest {
	@Test
	fun m1Test() {
		val result = OtherLib1Class().m1()
		assertEquals(result, "string from lib: OtherLib1Class: m1")
	}

	@Test
	fun m2Test() {
		val result = OtherLib1Class().m2()
		assertEquals(result, "string from lib: OtherLib1Class: m2")
	}
}