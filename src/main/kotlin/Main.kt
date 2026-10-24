package gruvexp

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    while (true) {
        print("Skriv inn hva du vil gjør: ")
        when (readln()) {
            "søgnemål" -> {
                while (true) {
                    println("Skriv 'exit' for å avslutte")
                    // blir først kristiansandsk ettersom andre på min alder og meg har søgnedialekta nesten dødd ut,
                    // og importert krsandsk istedet
                    print("Skriv inn ei setning som skal oversettes t krsandmål: ")
                    val input = readln()

                    if (input in listOf("exit", "stop", "stopp", "avslutt")) break

                    println("Oversatt t krsandmål: ${translate(input)}\n")
                }
            }
            "test_tokens" -> {
                print("Skriv inn ei setning: ")
                val rawSetning = readln()
                val setning = tokenize(rawSetning)
                println("$setning".replace("), Token", "),\nToken"))
            }
        }
    }
}

fun translate(text: String): String {
    val tokens = tokenize(text)
    return combine(tokens)
}
