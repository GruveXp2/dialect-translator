package dev.gruve

// sjekker etter ord som ikke er i ordlista eller er inherita
// temporary: man kanke registrere personlig pronomen enda
fun unknownWords(text: String, from: Ordliste, to: Ordliste, inherited: InheritedWords): List<String> =
    tokenize(text)
        .filter { it.type == TokenType.ORD }
        .map { it.text.lowercase() }
        .distinct()
        .filter { to.lookup(it) == null && it !in inherited && from.pronomen.readings(it).isEmpty() }

// lagrer/flytter et spesifikt ord i riktig liste og submap, og evt fjerner tomme deler
fun register(
    ordliste: Ordliste,
    inherited: InheritedWords,
    ordklasse: Ordklasse,
    word: String,
    form: String,
    date: String,
): Pair<Ordliste, InheritedWords> =
    if (form.isEmpty() || form == word) { // man trykka enter, flytt det til inherited
        ordliste.withoutWord(ordklasse, word) to inherited.withWord(ordklasse, word, date)
    } else { // man la inn en oversettelse, flytt det til ordlista
        ordliste.withWord(ordklasse, word, WordEntry(form, date)) to inherited.withoutWord(ordklasse, word)
    }
