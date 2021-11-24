package com.jemiola.moodtimeline.views.moods.timeline

import androidx.recyclerview.widget.DiffUtil
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2

class TimelineAdapterCallback(
    private val newRows: List<TimelineMoodBOv2>,
    private val oldRows: List<TimelineMoodBOv2>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldRow = oldRows[oldItemPosition]
        val newRow = newRows[newItemPosition]
        return oldRow.id == newRow.id
    }

    override fun getOldListSize(): Int = oldRows.size

    override fun getNewListSize(): Int = newRows.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldRow = oldRows[oldItemPosition]
        val newRow = newRows[newItemPosition]
        return oldRow == newRow
    }
}