package dev.gruve

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

// file format version
const val ORDLISTE_FORMAT_VERSION = 1

@Serializable
data class WordEntry(
    val form: String, // hva det blir oversatt til
    val registered: String, // dato
)

@Serializable
data class Ordliste(
    val version: Int,
    val words: Map<Ordklasse, Map<String, WordEntry>>, // ordklasse -> (input ord -> oversatt ord)
) {
    // Beholder forbokstav
    fun lookup(word: String): String? {
        val lowercase = word.lowercase()
        // basically words[lowercase], but for using nested maps
        val translated = words.values.firstNotNullOfOrNull { it[lowercase] }?.form ?: return null
        return if (word.first().isUpperCase()) translated.replaceFirstChar { it.uppercase() } else translated
    }
}

fun parseOrdliste(json: String): Ordliste {
    val ordliste = Json.decodeFromString<Ordliste>(json)
    require(ordliste.version == ORDLISTE_FORMAT_VERSION) { // migration will prob be added in the future when needed
        "Ordlista har versjon ${ordliste.version}, men programmet støtter bare versjon $ORDLISTE_FORMAT_VERSION"
    }
    return ordliste
}

fun loadOrdliste(file: File): Ordliste = parseOrdliste(file.readText())
