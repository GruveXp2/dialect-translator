package dev.gruve

import java.io.File

fun main() {
    val ordliste = loadOrdliste(File("ordlister/krsand.json"))
    println("[krsandmål|test_tokens|test_ordklasse]")
    while (true) {
        print("Skriv inn hva du vil gjør: ")
        when (val oper = readln()) {
            "krsandmål" -> {
                while (true) {
                    println("Skriv 'exit' for å avslutte")
                    // blir først kristiansandsk ettersom andre på min alder og meg har søgnedialekta nesten dødd ut,
                    // og importert ei blanding av krsandsk og østlandsk istedet
                    print("Skriv inn ei setning som skal oversettes t krsandmål: ")
                    val input = readln()

                    if (input in listOf("exit", "stop", "stopp", "avslutt")) break

                    println("Oversatt t krsandmål: ${translate(input, ordliste)}\n")
                }
            }
            "test_tokens" -> {
                print("Skriv inn ei setning: ")
                val rawSetning = readln()
                val setning = tokenize(rawSetning)
                println("$setning".replace("), Token", "),\nToken"))
            }
            "test_ordklasse" -> {
                val ordklasse = askOrdklasse()
                println("Du valgte: $ordklasse\n")
            }
            else -> println("Ugyldig valg '$oper'")
        }
    }
}

// ANSI codes for underlined text in the terminal
private const val UNDERLINE = "\u001B[4m"
private const val NO_UNDERLINE = "\u001B[24m"

fun askOrdklasse(): Ordklasse {
    for (ordklasse in Ordklasse.entries) {
        val name = ordklasse.name.lowercase()
        val abbreviation = ordklasse.abbreviation
        println("$UNDERLINE$abbreviation$NO_UNDERLINE${name.removePrefix(abbreviation)}")
    }
    while (true) {
        print("Skriv ordklasse (forkortelse): ")
        val ordklasse = ordklasseFromAbbreviation(readln())
        if (ordklasse != null) return ordklasse
        println("Ugyldig valg, prøv igjen")
    }
}
