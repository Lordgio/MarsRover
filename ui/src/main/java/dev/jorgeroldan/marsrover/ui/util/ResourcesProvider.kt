package dev.jorgeroldan.marsrover.ui.util

import android.content.Context
import androidx.annotation.StringRes

interface ResourcesProvider {
    fun getString(@StringRes resourceId: Int): String
}

class ResourcesProviderImpl(private val context: Context) : ResourcesProvider {

    override fun getString(@StringRes resourceId: Int): String {
        return context.getString(resourceId)
    }
}