package com.android.kotlin.architecture

fun String.toPascalCase(): String {
    return this
        .trim()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }
        .joinToString("") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
}