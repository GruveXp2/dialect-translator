package dev.gruve

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
    fun `the krsand ordliste file can be loaded`() {
        val ordliste = loadOrdliste(File("ordlister/krsand.json"))
        assertEquals(ORDLISTE_FORMAT_VERSION, ordliste.version)
    }
}
