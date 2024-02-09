package dev.jorgeroldan.marsrover.ui.util

import android.content.Context
import androidx.annotation.StringRes

class ResourcesProvider(private val context: Context) {

    fun getString(@StringRes resourceId: Int): String {
        return context.getString(resourceId)
    }
}