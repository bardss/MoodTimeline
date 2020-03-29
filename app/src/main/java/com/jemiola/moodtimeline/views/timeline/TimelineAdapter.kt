package com.jemiola.moodtimeline.views.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.customviews.ComfortaBoldTextView
import com.jemiola.moodtimeline.customviews.ComfortaRegularTextView
import com.jemiola.moodtimeline.customviews.MoodCircle
import com.jemiola.moodtimeline.customviews.RalewayRegularTextView
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import org.koin.core.KoinComponent
import org.koin.core.inject

class TimelineAdapter(
    private val view: TimelineContract.View
) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>(), KoinComponent {

    private val adapterPresenter: TimelineAdapterPresenter by inject()
    private var moods: List<TimelineMoodBO> = listOf()

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

    override fun getItemCount(): Int = moods.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setupTimelineItem(position, holder)
    }

    private fun setupTimelineItem(
        position: Int,
        holder: ViewHolder
    ) {
        val mood = moods[position]
        setupMoodCircle(holder, mood)
        setupText(holder, mood)
        setupOnClicks(holder, mood)
    }

    private fun setupOnClicks(holder: ViewHolder, mood: TimelineMoodBO) {
        holder.timelineItemLayout.setOnClickListener {
            adapterPresenter.onItemClick(mood, view)
        }
    }

    private fun setupText(
        holder: ViewHolder,
        mood: TimelineMoodBO
    ) {
        holder.dateTextView.text = adapterPresenter.getFormattedDate(mood.date)
        holder.noteTextView.text = mood.note
    }

    private fun setupMoodCircle(
        holder: ViewHolder,
        mood: TimelineMoodBO
    ) {
        holder.moodCircle.state = mood.state
        holder.moodCircle.mood = mood.mood
        if (mood.state == CircleStateBO.ADD) {
            holder.lineView.visibility = View.GONE
            holder.noteTextView.visibility = View.GONE
        } else {
            holder.lineView.setBackgroundColor(mood.mood.color)
            holder.lineView.visibility = View.VISIBLE
            holder.noteTextView.visibility = View.VISIBLE
        }
    }

    fun setTimelineMoods(moods: List<TimelineMoodBO>) {
        this.moods = moods
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timelineItemLayout: ViewGroup = view.findViewById(R.id.timelineItemLayout)
        val moodCircle: MoodCircle = view.findViewById(R.id.moodCircle)
        val dateTextView: ComfortaBoldTextView = view.findViewById(R.id.dateTextView)
        val noteTextView: RalewayRegularTextView = view.findViewById(R.id.noteTextView)
        val lineView: View = view.findViewById(R.id.lineView)
    }
}