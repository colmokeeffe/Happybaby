package ie.wit.happybaby.views.reminder

import ie.wit.happybaby.main.MainApp
import ie.wit.happybaby.models.ActivityModel

class ReminderPresenter (private val view: ReminderView) {

    var activity = ActivityModel()
    var edit = false
    var app: MainApp = view.application as MainApp

    init {
    }


    fun doCancel() {
        view.finish()
    }

    fun doHome() {
        view.finish()
    }
}