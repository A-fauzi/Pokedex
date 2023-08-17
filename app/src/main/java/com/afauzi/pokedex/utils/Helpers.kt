package com.afauzi.pokedex.utils

import java.util.*

object Helpers {
    /**
     * Fungsi untuk mengubah karakter pertama dari string menjadi huruf kapital (title case).
     *
     * @param text String yang akan diubah karakter pertamanya menjadi huruf kapital.
     * @return String dengan karakter pertama diubah menjadi huruf kapital.
     */
    fun capitalizeChar(text: String): String {
        // Menggunakan fungsi replaceFirstChar untuk mengganti karakter pertama dengan versi huruf kapital jika awalnya huruf kecil.
        // Jika karakter pertama adalah huruf kapital atau tanda baca, maka tidak akan diubah.
        return text.replaceFirstChar {
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault()) // Mengubah huruf kecil menjadi huruf kapital.
            else
                it.toString() // Mempertahankan huruf kapital atau tanda baca.
        }
    }


    /**
     * Fungsi untuk memformat nomor ID Pokemon menjadi format yang lebih terstruktur.
     *
     * @param pokeId Nomor ID Pokemon yang akan diformat.
     * @return String dengan format ID Pokemon yang sudah diformat.
     */
    fun formatIdPoke(pokeId: String): String {
        // Menggunakan try-catch untuk mengatasi jika pokeId tidak bisa diubah menjadi angka.
        val formattedId = try {
            val idNumber = pokeId.toInt()
            String.format("#%04d", idNumber) // Memformat ID menjadi format #000X.
        } catch (e: NumberFormatException) {
            pokeId // Jika tidak bisa diubah menjadi angka, kembalikan aslinya.
        }

        return formattedId // Mengembalikan hasil format ID.
    }

}