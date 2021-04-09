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

