package dev.gruve

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class PronomenTest {

    @Test
    fun `json cells are read with kasus as key`() {
        val json = """{ "nom": "han", "akk": "ham" }"""
        val expected: PersonligPronomen = mapOf(Kasus.NOMINATIV to "han", Kasus.AKKUSATIV to "ham")
        assertEquals(expected, Json.decodeFromString<PersonligPronomen>(json))
    }

    @Test
    fun `unknown kasus in json fails`() {
        assertFailsWith<SerializationException> {
            Json.decodeFromString<PersonligPronomen>("""{ "dat": "honom" }""")
        }
    }

    private val bokmål = Pronomen(
        mapOf(
            "eg" to mapOf(Kasus.NOMINATIV to "jeg", Kasus.AKKUSATIV to "meg"),
            "det" to mapOf(Kasus.NOMINATIV to "det", Kasus.AKKUSATIV to "det"),
        ),
    )

    private val krsand = Pronomen(
        mapOf("eg" to mapOf(Kasus.NOMINATIV to "æ", Kasus.AKKUSATIV to "mæ")),
    )

    @Test
    fun `a form gives every cell it is in`() {
        assertEquals(listOf("eg" to Kasus.AKKUSATIV), bokmål.readings("meg"))
        assertEquals(listOf("det" to Kasus.NOMINATIV, "det" to Kasus.AKKUSATIV), bokmål.readings("det"))
        assertEquals(emptyList(), bokmål.readings("hest"))
    }

    @Test
    fun `a pronomen is translated through the same cell`() {
        assertEquals("æ", translatePronomen("jeg", bokmål, krsand))
        assertEquals("mæ", translatePronomen("meg", bokmål, krsand))
    }

    @Test
    fun `a missing cell gives no translation`() {
        assertNull(translatePronomen("det", bokmål, krsand))
        assertNull(translatePronomen("hest", bokmål, krsand))
    }
}
