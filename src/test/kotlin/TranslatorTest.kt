package dev.gruve

import kotlin.test.Test
import kotlin.test.assertEquals

class TranslatorTest {

    @Test
    fun `without ordliste the text is unchanged`() {
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
