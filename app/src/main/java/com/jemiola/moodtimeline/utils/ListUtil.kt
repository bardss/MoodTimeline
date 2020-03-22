package com.jemiola.moodtimeline.utils

fun <T> List<T>.pushToFront(item: T): List<T> {
    return listOf(item) + this
}