package ie.wit.happybaby.views.activitylist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import ie.wit.happybaby.R

import ie.wit.happybaby.main.MainApp
import ie.wit.happybaby.models.ActivityModel
import ie.wit.happybaby.views.activity.ActivityView
import ie.wit.happybaby.views.login.LoginView

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActivityListPresenter (private val view: ActivityListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>

    init {
        app = view.application as MainApp
        registerRefreshCallback()
        registerMapCallback()
        registerLoginCallback()
    }

    fun doAddActivity() {
        val launcherIntent = Intent(view, ActivityView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }



    fun doEditActivity(activity: ActivityModel) {
        val launcherIntent = Intent(view, ActivityView::class.java)
        launcherIntent.putExtra("activity_edit", activity)
        refreshIntentLauncher.launch(launcherIntent)
    }

    suspend fun doSearch(query: String, checkedChip: Int) {
        var chipCategory = when (checkedChip) {
            R.id.chip_option_see -> "Pee"
            R.id.chip_option_do -> "Poop"
            R.id.chip_option_eat -> "Feed"
            R.id.chip_option_exercise -> "Exercise"
            R.id.chip_option_medication -> "Medication"
            else -> "All"
        }
        val filteredlist: MutableList<ActivityModel> = mutableListOf()
        val activities = app.activities.findActivitiesByCategory(chipCategory)

        for (item in activities) {
            if (item.description.lowercase().contains(query.lowercase())) {
                filteredlist.add(item)
                // i("item added to list")
            }
        }
        view.showActivities(filteredlist)
    }

    suspend fun doChipChange(selectedChip: Int) {
        var chipCategory = when (selectedChip) {
            R.id.chip_option_see -> "Pee"
            R.id.chip_option_do -> "Poop"
            R.id.chip_option_eat -> "Feed"
            R.id.chip_option_exercise -> "Exercise"
            R.id.chip_option_medication -> "Medication"
            R.id.chip_option_priority -> "High Priority"
            else -> "All"
        }
        var filteredlist: MutableList<ActivityModel>
        var activities = app.activities.findAll().toMutableList()

        when (chipCategory) {
            "All" -> {
                filteredlist = activities
            }
            "Poop" -> {
                filteredlist =
                    activities.filter { it.category == "Poop" } as MutableList<ActivityModel>
            }

            "Pee" -> {
                filteredlist =
                    activities.filter { it.category == "Pee" } as MutableList<ActivityModel>
            }

            "Feed" -> {
                filteredlist =
                    activities.filter { it.category == "Feed" } as MutableList<ActivityModel>
            }

            "Exercise" -> {
                filteredlist =
                    activities.filter { it.category == "Exercise" } as MutableList<ActivityModel>
            }

            "Medication" -> {
                filteredlist =
                    activities.filter { it.category == "Medication" } as MutableList<ActivityModel>
            }
            "High Priority" -> {
                filteredlist =
                    activities.filter { it.priority } as MutableList<ActivityModel>
            }
            else -> {
                filteredlist = activities
            }
        }
        view.showActivities(filteredlist)
    }
    
    suspend fun getActivities() = app.activities.findAll()

    suspend fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.activities.clear()
        val launcherIntent = Intent(view, LoginView::class.java)
        loginIntentLauncher.launch(launcherIntent)
    }

    suspend fun doDelete(activity: ActivityModel) {
        app.activities.delete(activity)
    }


    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main){
                    getActivities()
                }
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }

    private fun registerLoginCallback() {
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }

    }
}