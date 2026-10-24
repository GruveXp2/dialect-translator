package gruvexp

import kotlin.test.Test
import kotlin.test.assertEquals

class TranslatorTest {

    @Test
    fun `uten ordliste blir teksten uendra`() {
        val setninger = listOf(
            "Jeg skal på tur i morgen.",
            "Hei, kommer du?",
            "",
        )
        for (setning in setninger) {
            assertEquals(setning, translate(setning))
        }
    }
}
