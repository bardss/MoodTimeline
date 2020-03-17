package com.jemiola.moodtimeline.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.customviews.ComfortaRegularTextView
import com.jemiola.moodtimeline.customviews.MoodCircle
import com.jemiola.moodtimeline.customviews.RalewayRegularTextView
import com.jemiola.moodtimeline.data.CircleState
import com.jemiola.moodtimeline.data.TimelineItem
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.java.KoinJavaComponent.inject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class TimelineAdapter(
    private val view: TimelineContract.View
) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>(), KoinComponent {

    private val adapterPresenter: TimelineAdapterPresenter by inject()
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
        setupMoodCircle(holder, item)
        setupText(holder, item)
        setupOnClicks(holder, item)
    }

    private fun setupOnClicks(holder: ViewHolder, item: TimelineItem) {
        holder.timelineItemLayout.setOnClickListener {
            adapterPresenter.onItemClick(item, view)
        }
    }

    private fun setupText(
        holder: ViewHolder,
        item: TimelineItem
    ) {
        holder.dateTextView.text = adapterPresenter.getFormattedDate(item.date)
        holder.noteTextView.text = item.note
    }

    private fun setupMoodCircle(
        holder: ViewHolder,
        item: TimelineItem
    ) {
        holder.moodCircle.state = item.state
        holder.moodCircle.mood = item.mood
        if (item.state == CircleState.ADD) {
            holder.lineView.visibility = View.GONE
            holder.noteTextView.visibility = View.GONE
        } else {
            holder.lineView.setBackgroundColor(item.mood.color)
            holder.lineView.visibility = View.VISIBLE
            holder.noteTextView.visibility = View.VISIBLE
        }
    }

    fun setItems(items: List<TimelineItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timelineItemLayout: ViewGroup = view.findViewById(R.id.timelineItemLayout)
        val moodCircle: MoodCircle = view.findViewById(R.id.moodCircle)
        val dateTextView: ComfortaRegularTextView = view.findViewById(R.id.dateTextView)
        val noteTextView: RalewayRegularTextView = view.findViewById(R.id.noteTextView)
        val lineView: View = view.findViewById(R.id.lineView)
    }
}