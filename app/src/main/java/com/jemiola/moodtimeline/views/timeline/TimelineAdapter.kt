package com.jemiola.moodtimeline.views.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.customviews.ComfortaBoldTextView
import com.jemiola.moodtimeline.customviews.MoodCircle
import com.jemiola.moodtimeline.customviews.RalewayRegularTextView
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.ImageUtils
import com.jemiola.moodtimeline.utils.PermissionsUtil
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.utils.SizeUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class TimelineAdapter(
    private val view: TimelineContract.View
) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>(), KoinComponent {

    private val adapterPresenter: TimelineAdapterPresenter by inject()
    private var moods: List<TimelineMoodBOv2> = listOf()

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
        addFirstItemPadding(position, holder)
    }

    private fun addFirstItemPadding(position: Int, holder: ViewHolder) {
        if (position == 0) {
            holder.timelineItemLayout.setPadding(
                0, SizeUtils.dp2px(16f), 0, 0
            )
        }
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

    private fun setupOnClicks(holder: ViewHolder, mood: TimelineMoodBOv2) {
        holder.timelineItemLayout.setOnClickListener {
            adapterPresenter.onItemClick(mood, view)
        }
    }

    private fun setupText(
        holder: ViewHolder,
        mood: TimelineMoodBOv2
    ) {
        holder.dateTextView.text = adapterPresenter.getFormattedDate(mood.date)
        holder.noteTextView.text = mood.note
    }

    private fun setupMoodCircle(
        holder: ViewHolder,
        mood: TimelineMoodBOv2
    ) {
        holder.moodCircle.state = mood.circleState
        holder.moodCircle.mood = mood.circleMood
        if (mood.circleState == CircleStateBO.ADD) {
            holder.lineView.visibility = View.GONE
            holder.noteTextView.visibility = View.GONE
            holder.pictureLayout.visibility = View.GONE
        } else {
            holder.lineView.setBackgroundColor(
                ResUtil.getColor(holder.lineView.context, mood.circleMood.colorId)
            )
            holder.lineView.visibility = View.VISIBLE
            holder.noteTextView.visibility = View.VISIBLE
            //TODO: Do przekimny wyświetlanie
            setPathAsSelectedPicture(
                mood.picturesPaths[0],
                holder.pictureImageView,
                holder.pictureLayout
            )
        }
    }

    fun setTimelineMoods(moods: List<TimelineMoodBOv2>) {
        this.moods = moods
        notifyDataSetChanged()
    }

    private fun setPathAsSelectedPicture(
        path: String?,
        pictureImageView: ImageView,
        pictureLayout: ViewGroup
    ) {
        if (PermissionsUtil.isStoragePermissionGranted()) {
            val pictureBitmap = ImageUtils.getBitmapDrawableFromPath(path)
            if (pictureBitmap != null) {
                pictureLayout.visibility = View.VISIBLE
                pictureImageView.setImageDrawable(pictureBitmap)
            } else pictureLayout.visibility = View.GONE
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timelineItemLayout: ViewGroup = view.findViewById(R.id.timelineItemLayout)
        val moodCircle: MoodCircle = view.findViewById(R.id.moodCircle)
        val dateTextView: ComfortaBoldTextView = view.findViewById(R.id.dateTextView)
        val noteTextView: RalewayRegularTextView = view.findViewById(R.id.noteTextView)
        val lineView: View = view.findViewById(R.id.lineView)
        val pictureImageView: ImageView = view.findViewById(R.id.pictureImageView)
        val pictureLayout: ViewGroup = view.findViewById(R.id.pictureLayout)
    }
}