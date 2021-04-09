package com.jemiola.moodtimeline.views.navigation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseActivity
import com.jemiola.moodtimeline.base.Fragmenciak
import com.jemiola.moodtimeline.databinding.ActivityNavigationBinding
import com.jemiola.moodtimeline.utils.LoadingHandler
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.views.settings.SettingsFragment
import com.jemiola.moodtimeline.utils.moj_widok
import com.ncapdevi.fragnav.FragNavController
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

public const val TAB_STATISTICS = FragNavController.TAB1
public const val TAB_TIMELINE = FragNavController.TAB2
public const val TAB_SETTINGS = FragNavController.TAB3

class NavigationActivity : BaseActivity(),
    NavigationContract.View,
    FragNavController.RootFragmentListener {

    override val presenter: NavigationPresenter by inject { parametersOf(this) }
    public lateinit var binding: ActivityNavigationBinding
    public lateinit var navigation: FragNavController
    public val loadingHandler = LoadingHandler(this)
    public val actionsToDoOnResume: MutableList<() -> Any?> = mutableListOf()

    override val numberOfRootFragments: Int = 3
    override fun getRootFragment(index: Int): Fragment =
        when (index) {
            TAB_STATISTICS -> Fragment()
            TAB_TIMELINE -> moj_widok()
            TAB_SETTINGS -> SettingsFragment()
            else -> Fragment()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigation = createFragNavController()
        navigation.initialize(TAB_TIMELINE, savedInstanceState)
        unhighlightAllNavigationOptions()
        highlightTimeline()
        setupBottomNavigationClicks()
    }

    override fun onResume() {
        super.onResume()
        actionsToDoOnResume.forEach { it.invoke() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navigation.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        val topFragment = navigation.currentStack?.lastElement() as? Fragmenciak
        if (topFragment?.onBackPressed() == false) {
            if (navigation.currentStack?.size == 1) super.onBackPressed()
            else navigation.popFragment()
        }
    }

    public fun createFragNavController(): FragNavController {
        return FragNavController(
            supportFragmentManager,
            R.id.fragmentContainerLayout
        ).apply {
            rootFragmentListener = this@NavigationActivity
        }
    }

    public fun setupBottomNavigationClicks() {
        with(binding.menuBottomView) {
//            statisticsLayout.setOnClickListener { highlightStatistics() }
            timelineLayout.setOnClickListener {
                highlightTimeline()
            }
            settingsLayout.setOnClickListener {
                highlightSettings()
            }
        }
    }

//    public fun highlightStatistics() {
//        navigation.switchTab(TAB_STATISTICS)
//        highlightNavigationOption(
//            binding.menuBottomView.statisticsImageView,
//            binding.menuBottomView.statisticsTextView
//        )
//    }

    public fun highlightTimeline() {
        navigation.switchTab(TAB_TIMELINE)
        highlightNavigationOption(
            binding.menuBottomView.timelineImageView,
            binding.menuBottomView.timelineTextView
        )
    }

    public fun highlightSettings() {
        navigation.switchTab(TAB_SETTINGS)
        highlightNavigationOption(
            binding.menuBottomView.settingsImageView,
            binding.menuBottomView.settingsTextView
        )
    }

    public fun highlightNavigationOption(imageView: ImageView, textView: TextView) {
        unhighlightAllNavigationOptions()
        val highlightColor = ResUtil.getColor(this, R.color.colorBottomMenuActive)
        imageView.setColorFilter(highlightColor)
        textView.setTextColor(highlightColor)
    }

    public fun unhighlightAllNavigationOptions() {
        val inactiveColor = ResUtil.getColor(this, R.color.colorBottomMenuInactive)
        with(binding.menuBottomView) {
//            statisticsImageView.setColorFilter(inactiveColor)
            timelineImageView.setColorFilter(inactiveColor)
            settingsImageView.setColorFilter(inactiveColor)
//            statisticsTextView.setTextColor(inactiveColor)
            timelineTextView.setTextColor(inactiveColor)
            settingsTextView.setTextColor(inactiveColor)
        }
    }

    override fun popFragment() {
        performNavControllerAction {
            navigation.popFragment()
        }
    }

    override fun pushFragment(fragment: Fragment) {
        performNavControllerAction {
            navigation.pushFragment(fragment)
        }
    }

    public fun performNavControllerAction(action: () -> Any?) {
        if (navigation.isStateSaved) {
            actionsToDoOnResume.add(action)
        } else {
            action.invoke()
        }
    }

    override fun hideBottomMenu() {
        if (binding.menuBottomView.bottomMenuLayout.visibility != View.GONE) {
            binding.menuBottomView.bottomMenuLayout.visibility = View.GONE
        }
    }

    override fun showBottomMenu() {
        if (binding.menuBottomView.bottomMenuLayout.visibility != View.VISIBLE) {
            binding.menuBottomView.bottomMenuLayout.visibility = View.VISIBLE
        }
    }

    override fun startLoading() {
        loadingHandler.showLoading(false)
    }

    override fun stopLoading() {
        loadingHandler.stopLoading()
    }
}