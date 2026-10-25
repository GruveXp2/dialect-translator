package dev.gruve

import kotlinx.serialization.SerializationException
import java.io.File
import kotlin.io.path.createTempDirectory
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

    private fun entry(form: String) = WordEntry(form, "2026-10-25")

    private val ordliste = Ordliste(
        ORDLISTE_FORMAT_VERSION,
        mapOf(
            Ordklasse.PRONOMEN to mapOf("jeg" to entry("æ"), "deg" to entry("dæ")),
            Ordklasse.VERB to mapOf("skal" to entry("ska")),
        ),
    )

    private fun tempFile() = File(createTempDirectory().toFile(), "ordliste.json")

    @Test
    fun `saved ordliste can be loaded again`() {
        val file = tempFile()
        saveOrdliste(ordliste, file)
        assertEquals(ordliste, loadOrdliste(file))
    }

    @Test
    fun `saved file is sorted and has one word per line`() {
        val file = tempFile()
        saveOrdliste(ordliste, file)
        assertEquals(
            """
            {
                "version": 1,
                "words": {
                    "verb": {
                        "skal": { "form": "ska", "registered": "2026-10-25" }
                    },
                    "pronomen": {
                        "deg": { "form": "dæ", "registered": "2026-10-25" },
                        "jeg": { "form": "æ", "registered": "2026-10-25" }
                    }
                }
            }

            """.trimIndent(),
            file.readText(),
        )
    }

    @Test
    fun `words are sorted in norwegian alphabetical order`() {
        val words = listOf("år", "øy", "ære", "bil").associateWith { entry(it) }
        val sorted = Ordliste(ORDLISTE_FORMAT_VERSION, mapOf(Ordklasse.SUBSTANTIV to words)).sorted()
        assertEquals(listOf("bil", "ære", "øy", "år"), sorted.words.getValue(Ordklasse.SUBSTANTIV).keys.toList())
    }

    @Test
    fun `no temp file is left after saving`() {
        val file = tempFile()
        saveOrdliste(ordliste, file)
        assertEquals(listOf("ordliste.json"), file.parentFile.list()!!.toList())
    }
}
