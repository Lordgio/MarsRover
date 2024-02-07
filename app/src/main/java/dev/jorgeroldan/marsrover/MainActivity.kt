package dev.jorgeroldan.marsrover

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.jorgeroldan.marsrover.ui.di.UiModule
import dev.jorgeroldan.marsrover.ui.features.app.App
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        startDi(this)
        setContent {
            App()
        }
    }

    private fun startDi(context: Context) {
        // In config changes the activity is recreated, so we need to check first if Koin has been initialised
        if (GlobalContext.getKoinApplicationOrNull() == null) {
            startKoin {
                androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
                androidContext(context)
                modules(UiModule.module)
            }
        }
    }
}
