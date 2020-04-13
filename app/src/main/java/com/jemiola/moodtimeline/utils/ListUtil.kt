package com.jemiola.moodtimeline.utils

fun <T> MutableList<T>.pushToFront(item: T): List<T> {
    return listOf(item) + this
}