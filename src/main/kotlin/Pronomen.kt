package dev.gruve

import kotlinx.serialization.Serializable

// formane til ett personlig pronomen, f.eks. "han": { "nom": "han", "akk": "ham" }
typealias PersonligPronomen = Map<Kasus, String>

// en del av ordlista som holder styr på pronomen
@Serializable
data class Pronomen(
    val personlig: Map<String, PersonligPronomen> = emptyMap(), // kommer fler typer pronomen ettervert, som refleksiv, spørrande, osv
) {
    // alle cellane forma står i: "meg" -> [(eg, akk)], "det" -> [(det, nom), (det, akk)]
    // will use fex bokmål-analyse in the future, right now just searching until it finds. in the future, it will be a simple lookup, and will also give eventual "conflicts" fex if 2 words are spelled the same
    fun readings(form: String): List<Pair<String, Kasus>> =
        personlig.flatMap { (id, cells) -> cells.filterValues { it == form }.keys.map { id to it } }

    fun form(id: String, kasus: Kasus): String? = personlig[id]?.get(kasus)
}

// meg -> (eg, akk) i bokmålstabellen -> samme celle i krsand-tabellen -> mæ
// har forma flere oversettinger, vinner den første som har ei form (disambiguering kommer seinere)
fun translatePronomen(word: String, from: Pronomen, to: Pronomen): String? =
    from.readings(word).firstNotNullOfOrNull { (id, kasus) -> to.form(id, kasus) }
