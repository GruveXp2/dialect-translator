package dev.gruve

import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InheritedWordsTest {

    private val inherited = InheritedWords(
        INHERITED_FORMAT_VERSION,
        mapOf(
            Ordklasse.SUBSTANTIV to mapOf("hest" to "2026-10-25", "helt" to "2026-10-25"),
            Ordklasse.VERB to mapOf("gå" to "2026-10-25"),
        ),
    )

    private fun tempFile() = File(createTempDirectory().toFile(), "inherited.json")

    @Test
    fun `inherited word is found regardless of case`() {
        assertTrue("hest" in inherited)
        assertTrue("Hest" in inherited)
        assertFalse("bil" in inherited)
    }

    @Test
    fun `withWord adds the word`() {
        assertTrue("bil" in inherited.withWord(Ordklasse.SUBSTANTIV, "bil", "2026-10-25"))
    }

    @Test
    fun `saved inherited list can be loaded again`() {
        val file = tempFile()
        saveInheritedWords(inherited, file)
        assertEquals(inherited, loadInheritedWords(file))
    }

    @Test
    fun `saved file is sorted and has one word per line`() {
        val file = tempFile()
        saveInheritedWords(inherited, file)
        assertEquals(
            """
            {
                "version": 1,
                "words": {
                    "substantiv": {
                        "helt": "2026-10-25",
                        "hest": "2026-10-25"
                    },
                    "verb": {
                        "gå": "2026-10-25"
                    }
                }
            }

            """.trimIndent(),
            file.readText(),
        )
    }

    @Test
    fun `the krsand inherited file can be loaded`() {
        val inherited = loadInheritedWords(File("ordlister/krsand-inherited.json"))
        assertEquals(INHERITED_FORMAT_VERSION, inherited.version)
    }
}
