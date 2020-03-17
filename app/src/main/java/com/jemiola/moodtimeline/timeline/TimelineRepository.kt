package com.jemiola.moodtimeline.timeline

import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.data.CircleMood
import com.jemiola.moodtimeline.data.TimelineItem
import org.threeten.bp.LocalDate

class TimelineRepository : BaseRepository() {

    fun getDummyTimelineItems(): List<TimelineItem> {
        return listOf(
            TimelineItem(
                LocalDate.now().minusDays(1),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                CircleMood.GOOD
            ),
            TimelineItem(
                LocalDate.now().minusDays(2),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                CircleMood.BAD
            ),
            TimelineItem(
                LocalDate.now().minusDays(3),
                "Lorem ipsum",
                CircleMood.BAD
            ),
            TimelineItem(
                LocalDate.now().minusDays(4),
                "Lorem ipsum dolor sit amet",
                CircleMood.MEDIOCRE
            ),
            TimelineItem(
                LocalDate.now().minusDays(5),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                CircleMood.GOOD
            ),
            TimelineItem(
                LocalDate.now().minusDays(2),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                CircleMood.BAD
            ),
            TimelineItem(
                LocalDate.now().minusDays(3),
                "Lorem ipsum",
                CircleMood.BAD
            ),
            TimelineItem(
                LocalDate.now().minusDays(4),
                "Lorem ipsum dolor sit amet",
                CircleMood.MEDIOCRE
            ),
            TimelineItem(
                LocalDate.now().minusDays(5),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                CircleMood.GOOD
            )
        )
    }

}