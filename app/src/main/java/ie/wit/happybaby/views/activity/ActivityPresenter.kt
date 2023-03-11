package ie.wit.happybaby.views.activity

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import ie.wit.happybaby.R
import ie.wit.happybaby.databinding.ActivityActivityBinding

import ie.wit.happybaby.main.MainApp

import ie.wit.happybaby.models.ActivityModel

import java.util.*

class ActivityPresenter (private val view: ActivityView) {

    var activity = ActivityModel()
    private lateinit var binding: ActivityActivityBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    var edit = false
    var app: MainApp = view.application as MainApp
    init {
        if (view.intent.hasExtra("activity_edit")) {
            edit = true
            activity = view.intent.extras?.getParcelable("activity_edit")!!
            view.showActivity(activity)
            view.showEditView()
        }
    }

    suspend fun doAddorUpdate(selectedDate: String, selectedTime:String, description: String, rating: Float,category: Int, priority: Boolean) {
        activity.selectedDate = selectedDate
        activity.selectedTime = selectedTime
        activity.description = description
        activity.rating = rating
        activity.priority = priority
        activity.category = when (category) {
            R.id.option_pee -> "Pee"
            R.id.option_poo -> "Poop"
            R.id.option_feed -> "Feed"
            R.id.option_exercise -> "Exercise"
            R.id.option_medication -> "Medication"
            else -> "N/A"
        }
        if (edit) {
            app.activities.update(activity.copy())
        } else {
            app.activities.create(activity.copy())
        }
        view.finish()
    }

    fun cacheActivity (selectedDate: String, selectedTime:String, description: String, rating: Float, category: Int, priority: Boolean) {
        activity.selectedDate = selectedDate
        activity.selectedTime = selectedTime
        activity.description = description
        activity.rating = rating
        activity.priority = priority
        activity.category = when (category) {
            R.id.option_pee -> "Pee"
            R.id.option_poo -> "Poop"
            R.id.option_feed -> "Feed"
            R.id.option_exercise -> "Exercise"
            R.id.option_medication -> "Medication"
            else -> "N/A"
        }
    }


    suspend fun doDelete() {
        app.activities.delete(this.activity)
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    fun doHome() {
        view.finish()
    }
}