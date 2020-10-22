package com.jemiola.moodtimeline.views.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentSettingsBinding
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class SettingsFragment : BaseFragment(), SettingsContract.View {

    override val presenter: SettingsPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized) {
            binding = FragmentSettingsBinding.inflate(inflater, container, false)
            setupChangeThemeButton()
        }
        return binding.root
    }

    private fun setupChangeThemeButton() {
        binding.themeButtonView.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }


}
