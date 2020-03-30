package com.jemiola.moodtimeline.views.timeline

import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate

class TimelineAdapterPresenterTest {

    @Test
    fun `onItemClick invokes openEditTimelineMoodActivity on view with mood state is ADD`() {
        val circleMoodState = CircleStateBO.ADD
        val adapterPresenter = TimelineAdapterPresenter()
        val mood = TimelineMoodBO(
            LocalDate.MAX,
            "",
            CircleMoodBO.BAD,
            circleMoodState
        )
        val view: TimelineContract.View = mockk()
        every { view.openEditTimelineMoodActivity(any()) } returns Unit
        adapterPresenter.onItemClick(mood, view)
        verify(exactly = 1) { view.openEditTimelineMoodActivity(mood) }
    }

    @Test
    fun `onItemClick invokes openEditTimelineMoodActivity on view with mood state is EDIT`() {
        val circleMoodState = CircleStateBO.EDIT
        val adapterPresenter = TimelineAdapterPresenter()
        val mood = TimelineMoodBO(
            LocalDate.MAX,
            "",
            CircleMoodBO.BAD,
            circleMoodState
        )
        val view: TimelineContract.View = mockk()
        every { view.openEditTimelineMoodActivity(any()) } returns Unit
        adapterPresenter.onItemClick(mood, view)
        verify(exactly = 1) { view.openEditTimelineMoodActivity(mood) }
    }

    @Test
    fun `onItemClick invokes openTimelineMoodDetails on view with mood state is DEFAULT`() {
        val circleMoodState = CircleStateBO.DEFAULT
        val adapterPresenter = TimelineAdapterPresenter()
        val mood = TimelineMoodBO(
            LocalDate.MAX,
            "",
            CircleMoodBO.BAD,
            circleMoodState
        )
        val view: TimelineContract.View = mockk()
        every { view.openTimelineMoodDetails(any()) } returns Unit
        adapterPresenter.onItemClick(mood, view)
        verify(exactly = 1) { view.openTimelineMoodDetails(mood) }
    }

    @Test
    fun `getFormattedDate returns date string correctly formatted case 1`() {
        val date = LocalDate.of(2000, 3, 3)
        val adapterPresenter = TimelineAdapterPresenter()
        val formattedDate = adapterPresenter.getFormattedDate(date)
        assertEquals("March 3", formattedDate)
    }

    @Test
    fun `getFormattedDate returns date string correctly formatted case 2`() {
        val date = LocalDate.of(2000, 4, 12)
        val adapterPresenter = TimelineAdapterPresenter()
        val formattedDate = adapterPresenter.getFormattedDate(date)
        assertEquals("April 12", formattedDate)
    }
}
