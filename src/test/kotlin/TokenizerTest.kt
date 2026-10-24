package gruvexp

import kotlin.test.Test
import kotlin.test.assertEquals

class TokenizerTest {

    @Test
    fun `setning blir delt i ord, mellomrom og tegn`() {
        val tokens = tokenize("Javel, se det!")
        assertEquals(
            listOf(
                Token("Javel", TokenType.ORD),
                Token(",", TokenType.TEGN),
                Token(" ", TokenType.MELLOMROM),
                Token("se", TokenType.ORD),
                Token(" ", TokenType.MELLOMROM),
                Token("det", TokenType.ORD),
                Token("!", TokenType.TEGN),
            ),
            tokens,
        )
    }

    @Test
    fun `bindestrek inni ord og tall`() {
        val tokens = tokenize("e-post kl 12")
        assertEquals(listOf("e-post", " ", "kl", " ", "12"), tokens.map { it.text })
        assertEquals(TokenType.TALL, tokens.last().type)
    }

    @Test
    fun `sette sammen igjen gir nøyaktig samme tekst`() {
        val setninger = listOf(
            "Hei, kommer du?",
            "Æ og dæ  ska på   tur.\tI mårra!",
            "Linje én\nLinje to",
            "Kjør kano, i De Høye Hastigheder, det forurenser ikkje! kvikkhets-kano: 33,5 kr :D",
            "",
        )
        for (setning in setninger) {
            assertEquals(setning, combine(tokenize(setning)))
        }
    }
}
