package com.jemiola.moodtimeline.utils

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
import androidx.room.Room
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.*
import com.jemiola.moodtimeline.customviews.ComfortaBoldTextView
import com.jemiola.moodtimeline.customviews.MoodCircle
import com.jemiola.moodtimeline.customviews.RalewayRegularTextView
import com.jemiola.moodtimeline.databinding.FragmentTimelineBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.model.localdatabase.LocalSQLDatabase
import com.jemiola.moodtimeline.utils.rangepickers.RangePickersUtil
import com.jemiola.moodtimeline.views.calendar.CalendarFragment
import com.jemiola.moodtimeline.views.detailstimelinemood.DetailsTimelineMoodFragment
import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodFragment
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*



// KLASA PAULINY ;))))))))))))



const val licznik = 500
const val drugi_licznik = 100

// To jest tylko taki testowy widok

class moj_widok : Fragmenciak(),
    TimelineContract.View {


    override val a: TimelinePresenter by inject { parametersOf(this) }


            public lateinit var binding: FragmentTimelineBinding
                     public var b = false
    public var czy_kalendarz_jest_open = false
                  public var moj_licznik_minus_jeden = -1
    public val rangePickersUtil = RangePickersUtil()




//  WYSTARCZY ODKOMENTOWAC I NA BANK DZIAŁA!


//    override val a:  TimelineAdapterPresenter inject { parametersOf(this) }
//    public lateinit var binding: FragmentTimelineBinding
//    public var b = false
//    public var czy_kalendarz_jest_open = false
//    public var moj_licznik_minus_jeden = 0
//    public val rangePickersUtil = RangePickersUtil()



    override fun onCreateView(
                              inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
                             if (!this::binding.isInitialized) {
            binding = FragmentTimelineBinding.inflate(inflater, container, false)
            with(binding.timelineRecyclerView) {
                adapter =
                    ogarniaczListy(this@moj_widok)
                layoutManager = LinearLayoutManager(context)
            }


            val from_Date_String = a.getDefaultFromDate()
            val to_date_String = a.getDefaultToDate()
            binding.fromEditText.setText(from_Date_String)
            binding.toEditText.setText(to_date_String)



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

            setupSearchCalendars()
            binding.timelineTopLayout.post {
                initialSearchTopLayoutMoveOutOfScreen()
                binding.searchImageView.setOnClickListener { onSearchClick() }
            }
            val calendarFragment = CalendarFragment()
            childFragmentManager
                .beginTransaction()
                .add(R.id.calendarFragmentLayout, calendarFragment)
                .commit()
            binding.timelineTopLayout.post {
                initialCalendarTopLayoutMoveOutOfScreen()
                binding.calendarImageView.setOnClickListener { onCalendarClick() }
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        moj_licznik_minus_jeden = 0
        a.setupTimetableMoods()
    }

    override fun setTimelineMoods(moods: List<TimelineMoodBOv2>) {
        val adapter = binding.timelineRecyclerView.adapter
        val animationId =
            if (adapter?.itemCount == 0) R.anim.layout_animation_fade_in
            else R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(context, animationId)
        (adapter as ogarniaczListy).setTimelineMoods(moods)
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
        detailsTimelineMoodFragment.arguments = createBundleDetailsTimelineMood(mood)
        pushFragment(detailsTimelineMoodFragment)
    }

    public fun createBundleEditTimelineMood(
        mood: TimelineMoodBOv2,
        isAddingFirstMood: Boolean
    ): Bundle {
        return Bundle().apply {
            putSerializable(ExtraKeys.TIMELINE_MOOD, mood)
            putBoolean(ExtraKeys.IS_ADD_MOOD_ONBOARDING, isAddingFirstMood)
        }
    }

    public fun createBundleDetailsTimelineMood(mood: TimelineMoodBOv2): Bundle {
        return Bundle().apply {
            putSerializable(ExtraKeys.TIMELINE_MOOD, mood)
        }
    }

    public fun setupSearchCalendars() {
        context?.let {
            val fromEditText = binding.fromEditText
            val toEditText = binding.toEditText
            rangePickersUtil.setupRangeCalendars(
                it,
                fromEditText,
                toEditText
            ) { a.searchTimelineMoods() }
        }
    }

    public fun onSearchClick() {
        val distance = binding.timelineTopLayout.width
        val searchIconWidth = binding.searchImageView.width
        val timelineLayoutPadding = binding.timelineLayout.paddingStart
        if (!b) {
            b = true
            animateIconChangeTo(
                binding.searchImageView,
                ResUtil.getDrawable(context, R.drawable.ic_close)
            )
            val hideDistance = -distance + searchIconWidth + timelineLayoutPadding
            AnimUtils.animateMove(licznik, hideDistance, binding.timelineTopLayout)
            AnimUtils.animateMove(licznik, 0, binding.searchTopLayout)
        } else {
            b = false
            animateIconChangeTo(
                binding.searchImageView,
                ResUtil.getDrawable(context, R.drawable.ic_search)
            )
            AnimUtils.animateMove(licznik, 0, binding.timelineTopLayout)
            AnimUtils.animateMove(licznik, distance, binding.searchTopLayout) {
                setupSearchDefaultValues()
            }
        }
    }

    public fun setupSearchDefaultValues() {
        val fromDate = a.getDefaultFromDate()
        val toDate = a.getDefaultToDate()
        binding.fromEditText.setText(fromDate)
        binding.toEditText.setText(toDate)
    }

    public fun onCalendarClick() {
        val distance = binding.timelineTopLayout.width
        val calendarIconWidth = binding.calendarImageView.width
        val timelineLayoutPadding = binding.timelineLayout.paddingStart
        if (!czy_kalendarz_jest_open) {
            czy_kalendarz_jest_open = true
            animateIconChangeTo(
                binding.calendarImageView,
                ResUtil.getDrawable(context, R.drawable.ic_close)
            )
            val hideDistance = distance - calendarIconWidth - timelineLayoutPadding
            AnimUtils.animateMove(licznik, hideDistance, binding.timelineTopLayout)
            AnimUtils.animateMove(licznik, distance, binding.timelineRecyclerView)
            AnimUtils.animateMove(licznik, distance, binding.comeBackLaterLayout)
            AnimUtils.animateMove(licznik, 0, binding.calendarTopLayout)
            AnimUtils.animateMove(licznik, 0, binding.calendarFragmentLayout)
        } else {
            czy_kalendarz_jest_open = false
            animateIconChangeTo(
                binding.calendarImageView,
                ResUtil.getDrawable(context, R.drawable.ic_calendar)
            )
            AnimUtils.animateMove(licznik, 0, binding.timelineTopLayout)
            AnimUtils.animateMove(licznik, 0, binding.timelineRecyclerView)
            AnimUtils.animateMove(licznik, 0, binding.comeBackLaterLayout)
            AnimUtils.animateMove(licznik, -distance, binding.calendarTopLayout)
            AnimUtils.animateMove(licznik, -distance, binding.calendarFragmentLayout)
        }
    }

    public fun animateIconChangeTo(iconImageView: ImageView, drawable: Drawable?) {
        AnimUtils.fadeOut(100, {
            iconImageView.setImageDrawable(drawable)
            AnimUtils.fadeIn(100, iconImageView)
        }, iconImageView)
    }


    public fun initialSearchTopLayoutMoveOutOfScreen() {
        val distance = binding.timelineTopLayout.width
        AnimUtils.animateMove(licznik, distance, binding.searchTopLayout) {
            binding.searchTopLayout.visibility = View.VISIBLE
        }
    }

    public fun initialCalendarTopLayoutMoveOutOfScreen() {
        val distance = binding.timelineTopLayout.width
        AnimUtils.animateMove(licznik, -distance, binding.calendarTopLayout) {
            binding.calendarTopLayout.visibility = View.VISIBLE
        }
        AnimUtils.animateMove(licznik, -distance, binding.calendarFragmentLayout) {
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

    public fun showViewWhenHidden(view: View) {
        if (view.visibility == View.GONE || view.visibility == View.INVISIBLE) {
            AnimUtils.fadeIn(drugi_licznik, { view.visibility = View.VISIBLE }, view)
        }
    }

    public fun hideViewWhenVisible(view: View) {
        if (view.visibility != View.GONE) {
            AnimUtils.fadeOut(drugi_licznik, { view.visibility = View.GONE }, view)
        }
    }

    override fun showAddEmptyView() {
        setupAddEmptyViewMoodCircle()
        setupAddEmptyViewVisibility()
        setupAddEmptyViewOnClick()
    }

    override fun setupComeBackLaterView() {
        binding.timelineRecyclerView.post {
            val timelineContentLayoutHeight = binding.timelineContentLayout.height
            val timelineListHeight = binding.timelineRecyclerView.height
            val comeBackLaterViewHeight = binding.comeBackLaterLayout.height
            if (timelineListHeight < 100 && moj_licznik_minus_jeden < 5) {
                moj_licznik_minus_jeden += 1
                Handler().postDelayed({ setupComeBackLaterView() }, 1000)
            } else {
                val contentSum = timelineListHeight + comeBackLaterViewHeight
                if (timelineContentLayoutHeight > contentSum) {
                    AnimUtils.fadeIn(drugi_licznik, binding.comeBackLaterLayout)
                } else if (binding.comeBackLaterLayout.visibility != View.INVISIBLE) {
                    AnimUtils.fadeOut(drugi_licznik, binding.comeBackLaterLayout)
                }
            }
        }
    }

    public fun setupAddEmptyViewOnClick() {
        binding.addEmptyViewLayout.setOnClickListener {
            openEditTimelineMoodActivity(a.createAddTimelineMood(), true)
        }
    }

    public fun setupAddEmptyViewVisibility() {
        hideBottomMenu()
        binding.calendarImageView.visibility = View.INVISIBLE
        binding.searchImageView.visibility = View.INVISIBLE
        AnimUtils.fadeIn(drugi_licznik, binding.timelineRecyclerView)
        AnimUtils.fadeIn(drugi_licznik, binding.addEmptyViewLayout)
    }

    public fun setupAddEmptyViewMoodCircle() {
        binding.addEmptyViewCircle.mood = CircleMoodBO.VERY_GOOD
        binding.addEmptyViewCircle.state = CircleStateBO.ADD
    }
}

class TimelinePresenter(
    public val view: TimelineContract.View,
    override val repository: TimelineRepository
) : BasePresenter(repository),
    TimelineContract.Presenter {

    override fun setupTimetableMoods() {
        val callback = createRepositoryCallback<Int>(
            onSuccessAction = { onGetTimetableMoodsCountSuccess(it) },
            onErrorAction = {}
        )
        repository.getTimetableMoodsCount(callback)
    }

    public fun onGetTimetableMoodsCountSuccess(moodsCount: Int) {
        if (moodsCount == 0) view.showAddEmptyView()
        else {
            view.showBottomMenu()
            requestTimetableMoods()
        }
    }

    public fun requestTimetableMoods() {
        val fromDate = getFromDateFromView()
        val toDate = getToDateFromView()
        val callback = createRepositoryCallback<List<TimelineMoodBOv2>>(
            onSuccessAction = { onGetTimetableMoodsSuccess(it) },
            onErrorAction = {}
        )
        repository.getTimetableMoods(fromDate, toDate, callback)
    }

    fun searchTimelineMoods() {
        val fromDate = getFromDateFromView()
        val toDate = getToDateFromView()
        val callback = createRepositoryCallback<List<TimelineMoodBOv2>>(
            onSuccessAction = { onSearchTimelineMoodsSuccess(it) },
            onErrorAction = {}
        )
        repository.getTimetableMoods(fromDate, toDate, callback)
    }

    public fun getFromDateFromView(): LocalDate {
        val fromDateText = view.getFromDate()
        val formatter = getDefaultSearchDateFormatter()
        return LocalDate.parse(fromDateText, formatter)
    }

    public fun getToDateFromView(): LocalDate {
        val fromDateText = view.getToDate()
        val formatter = getDefaultSearchDateFormatter()
        return LocalDate.parse(fromDateText, formatter)
    }

    public fun onGetTimetableMoodsSuccess(result: List<TimelineMoodBOv2>) {
        val moods = addSpecialMoodsIfNeeded(result)
        view.setTimelineMoods(moods)
        if (moodsWithoutAddMoodState(moods)) {
            view.setupComeBackLaterView()
        }
    }

    public fun moodsWithoutAddMoodState(moods: List<TimelineMoodBOv2>): Boolean {
        return moods.none { it.circleState == CircleStateBO.ADD}
    }

    public fun addSpecialMoodsIfNeeded(moodsFromRepository: List<TimelineMoodBOv2>): List<TimelineMoodBOv2> {
        val editableMoods = moodsFromRepository.toMutableList()
        return when {
            shouldAddMoodBeVisible(moodsFromRepository) -> {
                val addTimelineItem = createAddTimelineMood()
                editableMoods.pushToFront(addTimelineItem)
            }
            shouldEditMoodBeVisible(moodsFromRepository) -> {
                createMoodsWithFirstEditableMood(editableMoods)
            }
            else -> moodsFromRepository
        }
    }

    public fun onSearchTimelineMoodsSuccess(moodsFromRepository: List<TimelineMoodBOv2>) {
        if (moodsFromRepository.isEmpty()) view.showSearchEmptyView()
        else {
            val moods = addSpecialMoodsIfNeeded(moodsFromRepository)
            view.showTimelineRecyclerView()
            view.setTimelineMoods(moods)
        }

    }

    override fun createAddTimelineMood(): TimelineMoodBOv2 {
        return TimelineMoodBOv2(
            id = null,
            date = LocalDate.now(DefaultTime.getClock()),
            note = "",
            circleMood = CircleMoodBO.NONE,
            circleState = CircleStateBO.ADD,
            picturesPaths = listOf()
        )
    }

    public fun createMoodsWithFirstEditableMood(
        moodsFromRepository: MutableList<TimelineMoodBOv2>
    ): List<TimelineMoodBOv2> {
        moodsFromRepository[0].circleState = CircleStateBO.EDIT
        return moodsFromRepository
    }

    public fun shouldAddMoodBeVisible(moods: List<TimelineMoodBOv2>): Boolean {
        val searchFromDate = getFromDateFromView()
        val searchToDate = getToDateFromView().plusDays(1)
        val dateNow = LocalDate.now(DefaultTime.getClock())
        return moods.none { it.date == dateNow } &&
                dateNow.isAfter(searchFromDate) &&
                dateNow.isBefore(searchToDate)
    }

    public fun shouldEditMoodBeVisible(moods: List<TimelineMoodBOv2>): Boolean {
        return moods.any { it.date == LocalDate.now(DefaultTime.getClock()) }
    }

    override fun getDefaultFromDate(): String {
        val defaultFromDate = repository.defaultSearchFromDate
        val formatter = getDefaultSearchDateFormatter()
        return defaultFromDate.format(formatter)
    }

    override fun getDefaultToDate(): String {
        val defaultToDate = repository.defaultSearchToDate
        val formatter = getDefaultSearchDateFormatter()
        return defaultToDate.format(formatter)
    }

    public fun getDefaultSearchDateFormatter() =
        DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(Locale.ENGLISH)

    override fun createDateTextFrom(dayOfMonth: Int, monthOfYear: Int, year: Int): String {
        val selectedDate = LocalDate.of(year, monthOfYear, dayOfMonth)
        val formatter = getDefaultSearchDateFormatter()
        return selectedDate.format(formatter)
    }

    override fun getSearchFromDateLong(): Long {
        val fromDate = getFromDateFromView()
        return getMilisFromDate(fromDate)
    }

    override fun getSearchToDateLong(): Long {
        val toDate = getToDateFromView()
        return getMilisFromDate(toDate)
    }

    public fun getMilisFromDate(date: LocalDate): Long {
        return LocalDateTime
            .of(date, LocalTime.NOON)
            .atZone(DefaultTime.getZone())
            .toInstant()
            .toEpochMilli()
    }
}

class TimelineRepository : BaseRepository() {

    val defaultSearchFromDate: LocalDate = LocalDate.now(DefaultTime.getClock()).minusDays(14)
    val defaultSearchToDate: LocalDate = LocalDate.now(DefaultTime.getClock())

    public val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalSQLDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    fun getTimetableMoods(
        from: LocalDate,
        to: LocalDate,
        callback: OnRepositoryCallback<List<TimelineMoodBOv2>>
    ) {
        launchCallbackRequest(
            request = {
                database.timelineMoodDaoV2().getMoodsFromTo(from, to)
            },
            onSuccess = {
                val timelineMoodBOs = convertTimelineMoodDOtoBO(it)
                val moodsInCorrectOrder = timelineMoodBOs.reversed()
                callback.onSuccess(moodsInCorrectOrder)
            },
            onError = { callback.onError() }
        )
    }

    fun getTimetableMoodsCount(
        callback: OnRepositoryCallback<Int>
    ) {
        launchCallbackRequest(
            request = { database.timelineMoodDaoV2().getMoodsCount() },
            onSuccess = { callback.onSuccess(it) },
            onError = { callback.onError() }
        )
    }

    public fun convertTimelineMoodDOtoBO(timetableMoodDOs: List<TimelineMoodDOv2>): List<TimelineMoodBOv2> {
        return timetableMoodDOs.map {
            TimelineMoodBOv2(
                id = it.id,
                date = it.date,
                note = it.note,
                circleMood = CircleMoodBO.from(it.mood),
                picturesPaths = it.picturesPaths
            )
        }
    }
}

interface TimelineContract {
    interface Presenter {
        fun setupTimetableMoods()
        fun getDefaultFromDate(): String
        fun getDefaultToDate(): String
        fun createDateTextFrom(dayOfMonth: Int, monthOfYear: Int, year: Int): String
        fun getSearchFromDateLong(): Long
        fun getSearchToDateLong(): Long
        fun createAddTimelineMood(): TimelineMoodBOv2
    }

    interface View {
        fun openEditTimelineMoodActivity(mood: TimelineMoodBOv2, isAddingFirstMood: Boolean = false)
        fun openTimelineMoodDetails(mood: TimelineMoodBOv2)
        fun setTimelineMoods(moods: List<TimelineMoodBOv2>)
        fun getFromDate(): String
        fun getToDate(): String
        fun showSearchEmptyView()
        fun showTimelineRecyclerView()
        fun showAddEmptyView()
        fun setupComeBackLaterView()
        fun showBottomMenu()
    }
}

class TimelineAdapterPresenter {

    fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d").withLocale(Locale.ENGLISH)
        return date.format(formatter).capitalize()
    }

    fun onItemClick(mood: TimelineMoodBOv2, view: TimelineContract.View) {
        when (mood.circleState) {
            CircleStateBO.ADD, CircleStateBO.EDIT -> view.openEditTimelineMoodActivity(mood)
            CircleStateBO.DEFAULT -> view.openTimelineMoodDetails(mood)
        }
    }
}

class ogarniaczListy(
    public val view: TimelineContract.View
) : RecyclerView.Adapter<ogarniaczListy.ViewHolder>(), KoinComponent {

    val adapterPresenter: TimelineAdapterPresenter by inject()
    var moodsLista: List<TimelineMoodBOv2> = listOf()

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

    override fun getItemCount(): Int = moodsLista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setupTimelineItem(position, holder)
        addFirstItemPadding(position, holder)
    }

    public fun addFirstItemPadding(position: Int, holder: ViewHolder) {
        if (position == 0) {
            holder.timelineItemLayout.setPadding(
                0, SizeUtils.dp2px(16f), 0, 0
            )
        }
    }

    public fun setupTimelineItem(
        position: Int,
        holder: ViewHolder
    ) {
        val mood = moodsLista[position]
        setupMoodCircle(holder, mood)
        setupText(holder, mood)
        setupOnClicks(holder, mood)
    }

    public fun setupOnClicks(holder: ViewHolder, mood: TimelineMoodBOv2) {
        holder.timelineItemLayout.setOnClickListener {
            adapterPresenter.onItemClick(mood, view)
        }
    }

    public fun setupText(
        holder: ViewHolder,
        mood: TimelineMoodBOv2
    ) {
        holder.dateTextView.text = adapterPresenter.getFormattedDate(mood.date)
        holder.noteTextView.text = mood.note
    }

    public fun setupMoodCircle(
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
        this.moodsLista = moods
        notifyDataSetChanged()
    }

    public fun setPathAsSelectedPicture(
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