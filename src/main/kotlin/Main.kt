package gruvexp

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    while (true) {
        println("Skriv 'exit' for å avslutte")
        print("Skriv inn ei setning som skal oversettes t søgnemål: ")
        val input = readln()

        if (input in listOf("exit", "stop", "stopp", "avslutt")) break

        println("Oversatt t søgnemål: $input\n")
    }
}