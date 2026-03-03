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

fun String.capitalizeFirstChar(): String {
    return this.split("\\s+").joinToString { word ->
        word.replaceFirstChar { it.uppercase() }
    }
}