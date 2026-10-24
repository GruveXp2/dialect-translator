package dev.gruve

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

// file format version
const val ORDLISTE_FORMAT_VERSION = 1

@Serializable
data class Ordliste(
    val version: Int,
    val words: Map<String, String>, // FellesNorsk(=radikalt bokmål) -> dialektform
)

fun parseOrdliste(json: String): Ordliste {
    val ordliste = Json.decodeFromString<Ordliste>(json)
    require(ordliste.version == ORDLISTE_FORMAT_VERSION) {
        "Ordlista har versjon ${ordliste.version}, men programmet støtter bare versjon $ORDLISTE_FORMAT_VERSION"
    }
    return ordliste
}

fun loadOrdliste(file: File): Ordliste = parseOrdliste(file.readText())
