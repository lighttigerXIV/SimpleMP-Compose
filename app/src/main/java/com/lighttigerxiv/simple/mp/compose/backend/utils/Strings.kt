package com.lighttigerxiv.simple.mp.compose.backend.utils

import java.text.Normalizer

fun CharSequence.unAccent(): String {
    val normalizer = Normalizer.normalize(this, Normalizer.Form.NFD)
    return "\\p{InCombiningDiacriticalMarks}+".toRegex().replace(normalizer, "")
}

fun matchesSearch(original: String, search: String): Boolean {
    return original.trim().unAccent().lowercase().contains(search.trim().unAccent().lowercase())
}