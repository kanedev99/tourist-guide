package vinova.kane.touristguide

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import vinova.kane.touristguide.di.repoModule
import vinova.kane.touristguide.di.retrofitModule
import vinova.kane.touristguide.di.viewModelModule


class TouristGuideApplication: Application() {

    private var instance: TouristGuideApplication? = null

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        instance = this

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@TouristGuideApplication)
            modules(listOf(retrofitModule, viewModelModule, repoModule))
        }
    }

    fun getInstance(): TouristGuideApplication? {
        return instance
    }
}