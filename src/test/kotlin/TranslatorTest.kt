package dev.gruve

import kotlin.test.Test
import kotlin.test.assertEquals

class TranslatorTest {

    private val emptyOrdliste = Ordliste(ORDLISTE_FORMAT_VERSION, emptyMap())

    private val ordliste = Ordliste(
        ORDLISTE_FORMAT_VERSION,
        mapOf(
            Ordklasse.PRONOMEN to mapOf("jeg" to entry("æ"), "deg" to entry("dæ")),
            Ordklasse.ADVERB to mapOf("også" to entry("au")),
        ),
    )

    private fun entry(form: String) = WordEntry(form, "2026-10-25")

    @Test
    fun `without ordliste the text is unchanged`() {
        val setninger = listOf(
            "Jeg skal på tur i morgen.",
            "Hei, kommer du?",
            "",
        )
        for (setning in setninger) {
            assertEquals(setning, translate(setning, emptyOrdliste))
        }
    }

    @Test
    fun `known words are replaced`() {
        assertEquals("æ ser dæ", translate("jeg ser deg", ordliste))
    }

    @Test
    fun `unknown words, spaces and symbols are unchanged`() {
        assertEquals("æ  ser:  dæ au!", translate("jeg  ser:  deg også!", ordliste))
    }

    @Test
    fun `capital first letter is kept`() {
        assertEquals("Æ ser dæ. Søren au!", translate("Jeg ser deg. Søren også!", ordliste))
    }
}
