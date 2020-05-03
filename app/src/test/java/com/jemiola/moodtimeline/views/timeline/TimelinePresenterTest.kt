package com.jemiola.moodtimeline.views.timeline

import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.DefaultTime
import io.mockk.*
import org.junit.jupiter.api.Test
import org.threeten.bp.*

class TimelinePresenterTest {

    private val view: TimelineContract.View = mockk()
    private val repository: TimelineRepository = mockk()
    private val presenter = TimelinePresenter(view, repository)

    @Test
    fun `refreshTimelineMoods invokes getTimetableMoods on repository`() {
        every { repository.getTimetableMoods(any()) } returns Unit
        presenter.setupTimetableMoods()
        verify(exactly = 1) { repository.getTimetableMoods(any()) }
    }

    @Test
    fun `refreshTimelineMoods invokes setTimelineMoods in onSuccessAction`() {
        mockkObject(DefaultTime)
        val testClock = Clock.fixed(LocalDateTime.MAX.toInstant(ZoneOffset.MAX), ZoneId.of("UTC"))
        every { DefaultTime.getClock() } returns testClock
        every { view.setTimelineMoods(any()) } returns Unit
        val testMoodBO = TimelineMoodBO(
            date = LocalDate.now(DefaultTime.getClock()),
            note = "",
            circleMood = CircleMoodBO.NONE,
            circleState = CircleStateBO.ADD,
            picturePath = ""
        )
        val result: List<TimelineMoodBO> = listOf(testMoodBO)
        every { repository.getTimetableMoods(any()) } answers {
            (firstArg() as OnRepositoryCallback<List<TimelineMoodBO>>).onSuccess(result)
        }
        presenter.setupTimetableMoods()
        verify(exactly = 1) { view.setTimelineMoods(result) }
        unmockkAll()
    }

}