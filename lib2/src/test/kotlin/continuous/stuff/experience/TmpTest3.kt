package continuous.stuff.experience

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class TmpTest3 {
	@Test
	fun blabla31() {
		assertTrue(!test2().isNotEmpty())
	}
	@Test
	fun blabla32() {
		assertTrue(test2().isNotEmpty())
	}
}