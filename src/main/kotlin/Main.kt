package dev.gruve

import java.io.File
import java.time.LocalDate

private val ORDLISTE_FILE = File("ordlister/krsand.json")
private val INHERITED_FILE = File("ordlister/krsand-inherited.json")

fun main() {
    var ordliste = loadOrdliste(ORDLISTE_FILE)
    var inherited = loadInheritedWords(INHERITED_FILE)

    // lokal funksjon som kan endre på ordliste/inherited
    fun askAndRegister(word: String) {
        while (true) {
            print("'$word', oversett: ")
            val answer = readln().trim().lowercase()
            if (answer == "-") return // hopp over ord
            val parts = answer.split(":", limit = 2)
            val form = parts[0]
            val ordklasse = if (parts.size == 2) ordklasseFromAbbreviation(parts[1]) else askOrdklasse()
            if (ordklasse == null) {
                println("Ukjent ordklasse '${parts[1]}', prøv igjen")
                continue
            }
            val (newOrdliste, newInherited) = register(ordliste, inherited, ordklasse, word, form, LocalDate.now().toString())
            ordliste = newOrdliste
            inherited = newInherited
            saveOrdliste(ordliste, ORDLISTE_FILE)
            saveInheritedWords(inherited, INHERITED_FILE)
            return
        }
    }

    println("[krsandmål|registrer|test_tokens|test_ordklasse]")
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

                    // future: add so you can type /context to get the words or sentence around,
                    // so you know if the word "helt" was used as whole or hero
                    val unknownWords = unknownWords(input, ordliste, inherited)
                    if (unknownWords.isNotEmpty()) {
                        println("Ukjente ord trenger oversettelse. Enter -> bruk default, '-' -> hopp over, 'form:ordklasse' -> slepp menyen (f.eks. ska:verb)")
                    }
                    for (word in unknownWords) askAndRegister(word)

                    println("Oversatt t krsandmål: ${translate(input, ordliste)}\n")
                }
            }
            "registrer" -> { // for å manuellt legge til eller rette en feil man har gjort
                print("Bokmålsord: ")
                val word = readln().trim().lowercase()
                if (word.isNotEmpty()) askAndRegister(word)
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
