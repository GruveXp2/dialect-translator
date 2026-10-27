package dev.gruve

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// vil ha dat og gen i framtida
@Serializable
enum class Kasus {
    @SerialName("nom") NOMINATIV,
    @SerialName("akk") AKKUSATIV,
}
