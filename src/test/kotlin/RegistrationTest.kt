package dev.gruve

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RegistrationTest {

    private val ordliste = Ordliste(
        ORDLISTE_FORMAT_VERSION,
        mapOf(Ordklasse.PRONOMEN to mapOf("jeg" to WordEntry("æ", "2026-10-25"))),
    )

    private val bokmål = Ordliste(
        ORDLISTE_FORMAT_VERSION,
        pronomen = Pronomen(mapOf("du" to mapOf(Kasus.NOMINATIV to "du"))),
    )

    private val inherited = InheritedWords(
        INHERITED_FORMAT_VERSION,
        mapOf(Ordklasse.SUBSTANTIV to mapOf("hest" to "2026-10-25")),
    )

    @Test
    fun `words in ordliste or inherited list are not unknown`() {
        assertEquals(listOf("ser"), unknownWords("Jeg ser hest", bokmål, ordliste, inherited))
    }

    @Test
    fun `personlige pronomen in the bokmål table are not unknown`() {
        assertEquals(listOf("ser"), unknownWords("Du ser hest", bokmål, ordliste, inherited))
    }

    @Test
    fun `unknown words are lowercase and without duplicates`() {
        assertEquals(listOf("tur", "og"), unknownWords("Tur, tur og TUR", bokmål, ordliste, inherited))
    }

    @Test
    fun `numbers and symbols are not unknown words`() {
        assertEquals(emptyList(), unknownWords("jeg 12 hest!", bokmål, ordliste, inherited))
    }

    private val date = "2026-10-26"

    @Test
    fun `a new form goes to ordliste`() {
        val (newOrdliste, newInherited) = register(ordliste, inherited, Ordklasse.VERB, "skal", "ska", date)
        assertEquals("ska", newOrdliste.lookup("skal"))
        assertEquals(inherited, newInherited)
    }

    @Test
    fun `an old form is overwritten`() {
        val (newOrdliste, _) = register(ordliste, inherited, Ordklasse.PRONOMEN, "jeg", "eg", date)
        assertEquals("eg", newOrdliste.lookup("jeg"))
    }

    @Test
    fun `empty form or the same form goes to the inherited list`() {
        for (form in listOf("", "bil")) {
            val (newOrdliste, newInherited) = register(ordliste, inherited, Ordklasse.SUBSTANTIV, "bil", form, date)
            assertEquals(ordliste, newOrdliste)
            assertTrue("bil" in newInherited)
        }
    }

    @Test
    fun `a word with a new form is moved out of the inherited list`() {
        val (newOrdliste, newInherited) = register(ordliste, inherited, Ordklasse.SUBSTANTIV, "hest", "hæst", date)
        assertEquals("hæst", newOrdliste.lookup("hest"))
        assertFalse("hest" in newInherited)
    }

    @Test
    fun `an inherited word is moved out of ordliste`() {
        val (newOrdliste, newInherited) = register(ordliste, inherited, Ordklasse.PRONOMEN, "jeg", "", date)
        assertNull(newOrdliste.lookup("jeg"))
        assertTrue("jeg" in newInherited)
    }

    @Test
    fun `other ordklasser are not touched`() {
        // helt (adj) -> heilt, helt (subst) er arva
        val inherited = inherited.withWord(Ordklasse.SUBSTANTIV, "helt", date)
        val (newOrdliste, newInherited) = register(ordliste, inherited, Ordklasse.ADJEKTIV, "helt", "heilt", date)
        assertEquals("heilt", newOrdliste.lookup("helt"))
        assertTrue("helt" in newInherited)
    }

    @Test
    fun `an ordklasse that becomes empty is removed`() {
        val (newOrdliste, _) = register(ordliste, inherited, Ordklasse.PRONOMEN, "jeg", "", date)
        assertFalse(Ordklasse.PRONOMEN in newOrdliste.words)
    }
}
