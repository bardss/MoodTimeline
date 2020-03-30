package com.jemiola.moodtimeline.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ListUtilTest {

    @Test
    fun `pushToFront adds item to the front of the list case 1`(){
        val itemToPush = 5
        val firstItem = 1
        val secondItem = 2
        val items = listOf(firstItem, secondItem)
        val updatedList = items.pushToFront(itemToPush)
        assertEquals(updatedList[0], itemToPush)
        assertEquals(updatedList[1], firstItem)
        assertEquals(updatedList[2], secondItem)
        assertEquals(updatedList.size, 3)
    }

    @Test
    fun `pushToFront adds item to the front of the list case 2`(){
        val itemToPush = 5
        val items = listOf<Int>()
        val updatedList = items.pushToFront(itemToPush)
        assertEquals(updatedList[0], itemToPush)
        assertEquals(updatedList.size, 1)
    }
}