package ie.wit.happybaby.main

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ie.wit.happybaby.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var activities: ActivityStore
    // val users = UserMemStore()
    // lateinit var users: UserStore

    lateinit var galleries: ActivityStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("HappyBaby started")
        // activities = ActivityJSONStore(applicationContext)
        // users = UserJSONStore(applicationContext)
        //activities = ActivityMemStore()
        // activities = ActivityStoreRoom(applicationContext)
        activities = ActivityFireStore(applicationContext)
        galleries = ActivityFireStore(applicationContext)
        val sharedPreferences = getSharedPreferences("save", MODE_PRIVATE)
        if (sharedPreferences.getBoolean("value", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}