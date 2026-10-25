package dev.gruve

import kotlin.test.Test
import kotlin.test.assertEquals

class RegistrationTest {

    private val ordliste = Ordliste(
        ORDLISTE_FORMAT_VERSION,
        mapOf(Ordklasse.PRONOMEN to mapOf("jeg" to WordEntry("æ", "2026-10-25"))),
    )

    private val inherited = InheritedWords(
        INHERITED_FORMAT_VERSION,
        mapOf(Ordklasse.SUBSTANTIV to mapOf("hest" to "2026-10-25")),
    )

    @Test
    fun `words in ordliste or inherited list are not unknown`() {
        assertEquals(listOf("ser"), unknownWords("Jeg ser hest", ordliste, inherited))
    }

    @Test
    fun `unknown words are lowercase and without duplicates`() {
        assertEquals(listOf("tur", "og"), unknownWords("Tur, tur og TUR", ordliste, inherited))
    }

    @Test
    fun `numbers and symbols are not unknown words`() {
        assertEquals(emptyList(), unknownWords("jeg 12 hest!", ordliste, inherited))
    }
}
