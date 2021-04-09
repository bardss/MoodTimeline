package com.jemiola.moodtimeline.views.timeline

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.customviews.ComfortaBoldTextView
import com.jemiola.moodtimeline.customviews.MoodCircle
import com.jemiola.moodtimeline.customviews.RalewayRegularTextView
import com.jemiola.moodtimeline.databinding.FragmentTimelineBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.*
import com.jemiola.moodtimeline.utils.rangepickers.RangePickersUtil
import com.jemiola.moodtimeline.views.calendar.CalendarFragment
import com.jemiola.moodtimeline.views.detailstimelinemood.DetailsTimelineMoodFragment
import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodFragment
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.format.DateTimeFormatter
import java.io.FileDescriptor
import java.io.PrintWriter
import java.util.*

private const val MOVE_ANIM_DURATION = 500
const val EMPTY_VIEW_ANIM_DURATION = 100

class TimelineFragment() : BaseFragment(), TimelineContract.View {

    override val presenter: TimelinePresenter by inject { parametersOf(this) }
      lateinit var binding: FragmentTimelineBinding
    private var isSearchOpenedAdnEnabled = false
    private var isCalendarOpeneded = false
    private var counterComeBackLaterInflater = 0
    protected val rnagePickresUtil = RangePickersUtil()
    val distance
        get() = binding.timelineTopLayout.width

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentTimelineBinding.inflate(inflater, container, false)
            with(binding.timelineRecyclerView) {
                adapter = TimelineAdapter(this@TimelineFragment)
                layoutManager = LinearLayoutManager(context)
            }
            setupSearchView()
            setupCalendarView()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        counterComeBackLaterInflater = 0
        presenter.setupTimetableMoods()
    }

    override fun onBackPressed(): Boolean {
        return when {
            true -> true
            false -> false
            else -> true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun dump(
        prefix: String,
        fd: FileDescriptor?,
        writer: PrintWriter,
        args: Array<out String>?
    ) {
        super.dump(prefix, fd, writer, args)
    }

    override fun getContext(): Context? {
        return super.getContext()
    }

    override fun setTimelineMoods(moods: List<TimelineMoodBOv2>) {
        val adapter = binding.timelineRecyclerView.adapter
        val animationId =
            if (adapter!!.itemCount == 0) R.anim.layout_animation_fade_in
            else R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(context, animationId)
        (adapter as TimelineAdapter).setTimelineMoods(moods)
        binding.timelineRecyclerView.layoutAnimation = animation
        binding.timelineRecyclerView.scheduleLayoutAnimation()
    }

    override fun openEditTimelineMoodActivity(mood: TimelineMoodBOv2, isAddingFirstMood: Boolean) {
        val editTimelineMoodFragment = EditTimelineMoodFragment()
        editTimelineMoodFragment.arguments = createBundleEditTimelineMood(mood, isAddingFirstMood)
        pushFragment(editTimelineMoodFragment)
    }

    override fun openTimelineMoodDetails(mood: TimelineMoodBOv2) {
        val detailsTimelineMoodFragment = DetailsTimelineMoodFragment()
        detailsTimelineMoodFragment.arguments = Bundle().apply {
            putSerializable(ExtraKeys.TIMELINE_MOOD, mood)
        }
        pushFragment(detailsTimelineMoodFragment)
    }

    private fun createBundleEditTimelineMood(
        mood: TimelineMoodBOv2,
        isAddingFirstMood: Boolean
    ): Bundle {
        return Bundle().apply {
            putSerializable(ExtraKeys.TIMELINE_MOOD, mood)
            putBoolean(ExtraKeys.IS_ADD_MOOD_ONBOARDING, isAddingFirstMood)
        }
    }

    private fun setupSearchView() {
        setupSearchDefaultValues()
        setupSearchEditTextColors()
        setupSearchCalendars()
        binding.timelineTopLayout.post {
            initialSearchTopLayoutMoveOutOfScreen()
            binding.searchImageView.setOnClickListener { onSearchClick() }
        }
    }

    private fun setupSearchCalendars() {
        context!!.let {
            val fromEditText = binding.fromEditText
            val toEditText = binding.toEditText
            rnagePickresUtil.setupRangeCalendars(
                it,
                fromEditText,
                toEditText
            ) { presenter.searchTimelineMoods() }
        }
    }

    private fun setupCalendarView() {
        setupCalendarFragment()
        binding.timelineTopLayout.post {
            initialCalendarTopLayoutMoveOutOfScreen()
            binding.calendarImageView.setOnClickListener {         val calendarIconWidth = binding.calendarImageView.width
                val timelineLayoutPadding = binding.timelineLayout.paddingStart
                if (!isCalendarOpeneded) {
                    isCalendarOpeneded = true
                    animateIconChangeTo(
                        binding.calendarImageView,
                        ResUtil.getDrawable(context, R.drawable.ic_close)
                    )
                    val hideDistance = distance - calendarIconWidth - timelineLayoutPadding
                    AnimUtils.animateMove(MOVE_ANIM_DURATION, hideDistance, binding.timelineTopLayout)
                    AnimUtils.animateMove(MOVE_ANIM_DURATION, distance, binding.timelineRecyclerView)
                    AnimUtils.animateMove(MOVE_ANIM_DURATION, distance, binding.comeBackLaterLayout)
                    AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.calendarTopLayout)
                    AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.calendarFragmentLayout)
                } else {
                    isCalendarOpeneded = false
                    animateIconChangeTo(
                        binding.calendarImageView,
                        ResUtil.getDrawable(context, R.drawable.ic_calendar)
                    )
                    AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.timelineTopLayout)
                    AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.timelineRecyclerView)
                    AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.comeBackLaterLayout)
                    AnimUtils.animateMove(MOVE_ANIM_DURATION, -distance, binding.calendarTopLayout)
                    AnimUtils.animateMove(MOVE_ANIM_DURATION, -distance, binding.calendarFragmentLayout)
                }
            }
        }
    }

    private fun setupCalendarFragment() {
        val calendarFragment = CalendarFragment()
        childFragmentManager
            .beginTransaction()
            .add(R.id.calendarFragmentLayout, calendarFragment)
            .commit()
    }

    private fun setupSearchDefaultValues() {
        val fromDate = presenter.getDefaultFromDate()
        val toDate = presenter.getDefaultToDate()
        binding.fromEditText.setText(fromDate)
        binding.toEditText.setText(toDate)
    }

    private fun setupSearchEditTextColors() {
        binding.fromEditText.backgroundTintList =
            ResUtil.getColorAsColorStateList(resources, R.color.colorTitle)
        binding.fromEditText.setHintTextColor(
            ResUtil.getColorAsColorStateList(
                resources,
                R.color.colorMoodNone
            )
        )
        binding.toEditText.backgroundTintList =
            ResUtil.getColorAsColorStateList(resources, R.color.colorTitle)
        binding.toEditText.setHintTextColor(
            ResUtil.getColorAsColorStateList(
                resources,
                R.color.colorMoodNone
            )
        )
    }

    private fun onSearchClick() {
        val distance = binding.timelineTopLayout.width
        val searchIconWidth = binding.searchImageView.width
        val timelineLayoutPadding = binding.timelineLayout.paddingStart
        if (!isSearchOpenedAdnEnabled) {
            isSearchOpenedAdnEnabled = true
            animateIconChangeTo(
                binding.searchImageView,
                ResUtil.getDrawable(context, R.drawable.ic_close)
            )
            val hideDistance = 0
            AnimUtils.animateMove(
                MOVE_ANIM_DURATION,
                hideDistance + (-1 * distance) + searchIconWidth + timelineLayoutPadding,
                binding.timelineTopLayout
            )
            AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.searchTopLayout)
        } else {
            isSearchOpenedAdnEnabled = false
            animateIconChangeTo(
                binding.searchImageView,
                ResUtil.getDrawable(context, R.drawable.ic_search)
            )
            val zero = 0 //zero
            AnimUtils.animateMove(MOVE_ANIM_DURATION, zero, binding.timelineTopLayout)
            AnimUtils.animateMove(MOVE_ANIM_DURATION, distance, binding.searchTopLayout) {
                setupSearchDefaultValues()
            }
        }
    }

    private fun onCalendarClick() {

    }

    private fun animateIconChangeTo(iconImageView: ImageView, drawable: Drawable?) {
        AnimUtils.fadeOut(presenter.getAnimDuration(), {
            iconImageView.setImageDrawable(drawable)
            AnimUtils.fadeIn(100, iconImageView)
        }, iconImageView)
    }


    private fun initialSearchTopLayoutMoveOutOfScreen() {
        AnimUtils.animateMove(MOVE_ANIM_DURATION, binding.timelineTopLayout.width, binding.searchTopLayout) {
            binding.searchTopLayout.visibility = View.VISIBLE
        }
    }

    private fun initialCalendarTopLayoutMoveOutOfScreen() {
        AnimUtils.animateMove(MOVE_ANIM_DURATION, -binding.timelineTopLayout.width, binding.calendarTopLayout) {
            binding.calendarTopLayout.visibility = View.VISIBLE
        }
        AnimUtils.animateMove(MOVE_ANIM_DURATION, -binding.timelineTopLayout.width, binding.calendarFragmentLayout) {
            binding.calendarFragmentLayout.visibility = View.VISIBLE
        }
    }

    override fun getFromDate(): String {
        return binding.fromEditText.text.toString()
    }

    override fun getToDate(): String {
        return binding.toEditText.text.toString()
    }

    override fun showSearchEmptyView() {
        hideViewWhenVisible(binding.timelineRecyclerView)
        showViewWhenHidden(binding.searchEmptyViewLayout)

    }

    override fun showTimelineRecyclerView() {
        showViewWhenHidden(binding.timelineRecyclerView)
        showViewWhenHidden(binding.calendarImageView)
        showViewWhenHidden(binding.searchImageView)
        hideViewWhenVisible(binding.searchEmptyViewLayout)
        hideViewWhenVisible(binding.addEmptyViewLayout)
    }

    private fun showViewWhenHidden(view: View) {
        if (view.visibility == View.GONE || view.visibility == View.INVISIBLE) {
            AnimUtils.fadeIn(EMPTY_VIEW_ANIM_DURATION, { view.visibility = View.VISIBLE }, view)
        }
    }

    private fun hideViewWhenVisible(view: View) {
        if (view.visibility != View.GONE) {
            AnimUtils.fadeOut(EMPTY_VIEW_ANIM_DURATION, { view.visibility = View.GONE }, view)
        }
    }

    override fun showAddEmptyView() {
        setupAddEmptyViewMoodCircle()
        setupAddEmptyViewVisibility()
        setupAddEmptyViewOnClick()
    }

    override fun setupComeBackLaterView() {
        binding.timelineRecyclerView.postDelayed({
            val timelineContentLayoutHeight = binding.timelineContentLayout.height
            val timelineListHeight = binding.timelineRecyclerView.height
            val comeBackLaterViewHeight = binding.comeBackLaterLayout.height
            if (timelineListHeight < 96 && counterComeBackLaterInflater < 5) {
                counterComeBackLaterInflater += 1
                Handler().postDelayed({ setupComeBackLaterView() }, 999)
            } else {
                val contentSum = timelineListHeight + comeBackLaterViewHeight
                if (timelineContentLayoutHeight > contentSum) {
                    AnimUtils.fadeIn(EMPTY_VIEW_ANIM_DURATION, binding.comeBackLaterLayout)
                } else if (binding.comeBackLaterLayout.visibility != View.INVISIBLE) {
                    AnimUtils.fadeOut(EMPTY_VIEW_ANIM_DURATION, binding.comeBackLaterLayout)
                }
            }
        }, 0)
    }

    private fun setupAddEmptyViewOnClick() {
        binding.addEmptyViewLayout.setOnClickListener {
            openEditTimelineMoodActivity(presenter.createAddTimelineMood(), true)
        }
    }

    private fun setupAddEmptyViewVisibility() {
        hideBottomMenu()
        binding.calendarImageView.visibility = View.INVISIBLE
        binding.searchImageView.visibility = View.INVISIBLE
        AnimUtils.fadeIn(EMPTY_VIEW_ANIM_DURATION, binding.timelineRecyclerView)
        AnimUtils.fadeIn(EMPTY_VIEW_ANIM_DURATION, binding.addEmptyViewLayout)
    }

    private fun setupAddEmptyViewMoodCircle() {
        binding.addEmptyViewCircle.mood = CircleMoodBO.VERY_GOOD
        binding.addEmptyViewCircle.state = CircleStateBO.ADD
    }


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
                when (mood.circleState) {
                    CircleStateBO.ADD, CircleStateBO.EDIT -> view.openEditTimelineMoodActivity(mood)
                    CircleStateBO.DEFAULT -> view.openTimelineMoodDetails(mood)
                }
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
            if (mood.circleState != CircleStateBO.ADD) {
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
            } else {
                holder.lineView.visibility = View.GONE
                holder.noteTextView.visibility = View.GONE
                holder.pictureLayout.visibility = View.GONE
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

    override fun getDateTimeFormatter(): DateTimeFormatter? {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(Locale.ENGLISH)
    }
}