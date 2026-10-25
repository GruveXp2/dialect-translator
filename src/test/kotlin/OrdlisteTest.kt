package dev.gruve

import kotlinx.serialization.SerializationException
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class OrdlisteTest {

    @Test
    fun `empty ordliste has no words`() {
        val ordliste = parseOrdliste("""{ "version": 1, "words": {} }""")
        assertEquals(1, ordliste.version)
        assertTrue(ordliste.words.isEmpty())
    }

    @Test
    fun `ordliste with another version is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            parseOrdliste("""{ "version": 2, "words": {} }""")
        }
    }

    @Test
    fun `words are grouped by ordklasse name`() {
        val ordliste = parseOrdliste(
            """
            {
              "version": 1,
              "words": {
                "pronomen": { "jeg": { "form": "æ", "registered": "2026-10-25" } },
                "adverb": { "også": { "form": "au", "registered": "2026-10-25" } }
              }
            }
            """
        )
        assertEquals("æ", ordliste.words[Ordklasse.PRONOMEN]?.get("jeg")?.form)
        assertEquals("au", ordliste.words[Ordklasse.ADVERB]?.get("også")?.form)
    }

    @Test
    fun `unknown ordklasse is rejected`() {
        assertFailsWith<SerializationException> {
            parseOrdliste("""{ "version": 1, "words": { "pron": {} } }""")
        }
    }

    @Test
    fun `the krsand ordliste file can be loaded`() {
        val ordliste = loadOrdliste(File("ordlister/krsand.json"))
        assertEquals(ORDLISTE_FORMAT_VERSION, ordliste.version)
    }
}
