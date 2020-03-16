package com.jemiola.moodtimeline.utils

fun <T> List<T>.addToFront(item: T): List<T> {
    return listOf(item) + this
}