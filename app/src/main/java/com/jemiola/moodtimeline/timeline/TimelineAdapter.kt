package com.jemiola.moodtimeline.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.customviews.ComfortaRegularTextView
import com.jemiola.moodtimeline.customviews.MoodCircle
import com.jemiola.moodtimeline.customviews.RalewayRegularTextView
import com.jemiola.moodtimeline.data.MoodCircleState
import com.jemiola.moodtimeline.data.TimelineItem
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class TimelineAdapter : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    private var items: List<TimelineItem> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_timeline,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setupTimelineItem(position, holder)
    }

    private fun setupTimelineItem(
        position: Int,
        holder: ViewHolder
    ) {
        val item = items[position]
        holder.moodCircle.state = MoodCircleState(item.mood)
        holder.dateTextView.text = getFormattedDate(item.date)
        holder.noteTextView.text = item.note
        holder.lineView.setBackgroundColor(item.mood.color)
    }

    private fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d")
        return date.format(formatter)
    }

    fun setItems(items: List<TimelineItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moodCircle: MoodCircle = view.findViewById(R.id.moodCircle)
        val dateTextView: ComfortaRegularTextView = view.findViewById(R.id.dateTextView)
        val noteTextView: RalewayRegularTextView = view.findViewById(R.id.noteTextView)
        val lineView: View = view.findViewById(R.id.lineView)
    }
}