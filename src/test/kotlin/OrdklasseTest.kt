package dev.gruve

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OrdklasseTest {

    @Test
    fun `abbreviation gives the right ordklasse`() {
        assertEquals(Ordklasse.PRONOMEN, ordklasseFromAbbreviation("pron"))
        assertEquals(Ordklasse.PRONOMEN, ordklasseFromAbbreviation(" PRON "))
        assertEquals(Ordklasse.DETERMINATIV, ordklasseFromAbbreviation("determ"))
    }

    @Test
    fun `invalid input gives no ordklasse`() {
        for (text in listOf("1", "", "pronomen", "x")) {
            assertNull(ordklasseFromAbbreviation(text), "\"$text\" skulle vært ugyldig")
        }
    }

    @Test
    fun `all abbreviations are unique`() {
        val abbreviations = Ordklasse.entries.map { it.abbreviation }
        assertEquals(abbreviations.size, abbreviations.toSet().size)
    }

    @Test
    fun `abbreviation is the start of the full name`() {
        for (ordklasse in Ordklasse.entries) {
            assertTrue(ordklasse.name.lowercase().startsWith(ordklasse.abbreviation), "$ordklasse")
        }
    }
}
