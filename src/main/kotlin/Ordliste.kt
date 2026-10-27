package dev.gruve

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File

// file format version
const val ORDLISTE_FORMAT_VERSION = 2

@Serializable
data class WordEntry(
    val form: String, // hva det blir oversatt til
    val registered: String, // dato
)

@Serializable
data class Ordliste(
    val version: Int,
    val words: Map<Ordklasse, Map<String, WordEntry>> = emptyMap(), // ordklasse -> (input ord -> oversatt ord)
    val pronomen: Pronomen = Pronomen(),
) {
    // basically words[word], but for using nested maps.
    fun lookup(word: String): String? = words.values.firstNotNullOfOrNull { it[word] }?.form

    fun withWord(ordklasse: Ordklasse, word: String, entry: WordEntry) = copy(words = words.plusWord(ordklasse, word, entry))

    fun withoutWord(ordklasse: Ordklasse, word: String) = copy(words = words.minusWord(ordklasse, word))

    fun sorted() = copy(words = words.sorted())
}

fun parseOrdliste(json: String): Ordliste {
    val ordliste = Json.decodeFromString<Ordliste>(json)
    require(ordliste.version == ORDLISTE_FORMAT_VERSION) { // migration will prob be added in the future when needed
        "Ordlista har versjon ${ordliste.version}, men programmet støtter bare versjon $ORDLISTE_FORMAT_VERSION"
    }
    return ordliste
}

fun loadOrdliste(file: File): Ordliste = parseOrdliste(file.readText())

fun saveOrdliste(ordliste: Ordliste, file: File) = writeJsonFile(Json.encodeToJsonElement(ordliste.sorted()), file)
