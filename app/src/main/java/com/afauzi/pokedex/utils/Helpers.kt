package com.afauzi.pokedex.utils

import java.util.*

object Helpers {
    fun capitalizeChar(text: String): String {
        return text.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}