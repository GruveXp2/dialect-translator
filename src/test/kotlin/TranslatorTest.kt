package dev.gruve

import kotlin.test.Test
import kotlin.test.assertEquals

class TranslatorTest {

    private val emptyOrdliste = Ordliste(ORDLISTE_FORMAT_VERSION)

    private val bokmål = Ordliste(
        ORDLISTE_FORMAT_VERSION,
        pronomen = Pronomen(
            mapOf(
                "eg" to mapOf(Kasus.NOMINATIV to "jeg", Kasus.AKKUSATIV to "meg"),
                "du" to mapOf(Kasus.NOMINATIV to "du", Kasus.AKKUSATIV to "deg"),
            ),
        ),
    )

    private val krsand = Ordliste(
        ORDLISTE_FORMAT_VERSION,
        mapOf(Ordklasse.ADVERB to mapOf("også" to entry("au"))),
        Pronomen(
            mapOf(
                "eg" to mapOf(Kasus.NOMINATIV to "æ", Kasus.AKKUSATIV to "mæ"),
                "du" to mapOf(Kasus.AKKUSATIV to "dæ"),
            ),
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
            assertEquals(setning, translate(setning, emptyOrdliste, emptyOrdliste))
        }
    }

    @Test
    fun `known words are replaced`() {
        assertEquals("æ ser dæ", translate("jeg ser deg", bokmål, krsand))
    }

    @Test
    fun `a pronomen without a krsand form is unchanged`() {
        assertEquals("du ser mæ", translate("du ser meg", bokmål, krsand))
    }

    @Test
    fun `unknown words, spaces and symbols are unchanged`() {
        assertEquals("æ  ser:  dæ au!", translate("jeg  ser:  deg også!", bokmål, krsand))
    }

    @Test
    fun `capital first letter is kept`() {
        assertEquals("Æ ser dæ. Søren au!", translate("Jeg ser deg. Søren også!", bokmål, krsand))
    }
}
