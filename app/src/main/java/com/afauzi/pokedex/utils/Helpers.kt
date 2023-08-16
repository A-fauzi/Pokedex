package com.afauzi.pokedex.utils

import java.util.*

object Helpers {
    fun capitalizeChar(text: String): String {
        return text.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    fun formatIdPoke(pokeId: String): String {
        val formattedId = try {
            val idNumber = pokeId.toInt()
            String.format("#%04d", idNumber)
        } catch (e: NumberFormatException) {
            pokeId
        }

        return formattedId
    }
}