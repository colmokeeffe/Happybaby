package ie.wit.happybaby.views.activitylist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import ie.wit.happybaby.R
import ie.wit.happybaby.helpers.exportToCSV

import ie.wit.happybaby.main.MainApp
import ie.wit.happybaby.models.ActivityFireStore
import ie.wit.happybaby.models.ActivityModel
import ie.wit.happybaby.views.activity.ActivityView
import ie.wit.happybaby.views.activitygallery.ActivityGalleryView
import ie.wit.happybaby.views.activitygallerylist.ActivityGalleryListView
import ie.wit.happybaby.views.login.LoginView
import ie.wit.happybaby.views.reminder.ReminderView

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActivityListPresenter(private val view: ActivityListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>
    var fireStore: ActivityFireStore? = null
    private lateinit var activityIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var registerIntentLauncher : ActivityResultLauncher<Intent>


    init {
        app = view.application as MainApp
        registerRefreshCallback()
        registerLoginCallback()
        registerActivityCallback()

        if (app.activities is ActivityFireStore) {
            fireStore = app.activities as ActivityFireStore
        }

    }

    fun doAddActivity() {
        val launcherIntent = Intent(view, ActivityView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doListActivity() {
        val launcherIntent = Intent(view, ActivityListView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doAddImageActivity() {
        val launcherIntent = Intent(view, ActivityGalleryListView::class.java)
        activityIntentLauncher.launch(launcherIntent)
            }

    fun doAddReminderActivity() {
        val launcherIntent = Intent(view, ReminderView::class.java)
        activityIntentLauncher.launch(launcherIntent)
    }



    fun doAddImageGalleryActivity() {
        val launcherIntent = Intent(view, ActivityGalleryView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }


    fun doExport(){
        GlobalScope.launch(Dispatchers.Main){
            val activities = fireStore!!.activities
            if (activities != null){
            val fileOut = exportToCSV(
                "Happy_Baby.csv",
                view.getExternalFilesDir(null),
                "Date,Time,Category,Description,Mood(1-5)\n", activities
            )
            val uri = FileProvider.getUriForFile(
                view,
                "ie.wit.happybaby.file_provider",
                fileOut
            )
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
            sendIntent.flags =
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            sendIntent.type = "text/csv"
            view.startActivity(Intent.createChooser(sendIntent, "SHARE"))
        }
    }}


    fun doEditActivity(activity: ActivityModel) {
        val launcherIntent = Intent(view, ActivityView::class.java)
        launcherIntent.putExtra("activity_edit", activity)
        refreshIntentLauncher.launch(launcherIntent)
    }

    suspend fun doSearch(query: String, checkedChip: Int) {
        var chipCategory = when (checkedChip) {
            R.id.chip_option_pee -> "Pee"
            R.id.chip_option_poo -> "Poop"
            R.id.chip_option_feed -> "Feed"
            R.id.chip_option_exercise -> "Exercise"
            R.id.chip_option_medication -> "Medication"
            R.id.chip_option_priority -> "High Priority"
            else -> "All"
        }
        val filteredlist: MutableList<ActivityModel> = mutableListOf()
        val activities = app.activities.findActivitiesByCategory(chipCategory)

        for (item in activities) {
            if (item.description.lowercase().contains(query.lowercase())) {
                filteredlist.add(item)
            }
        }
        view.showActivities(filteredlist)
    }

    suspend fun doChipChange(selectedChip: Int) {
        var chipCategory = when (selectedChip) {
            R.id.chip_option_pee -> "Pee"
            R.id.chip_option_poo -> "Poop"
            R.id.chip_option_feed -> "Feed"
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
    suspend fun getGalleries() = app.galleries.findAllGalleries()

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


    private fun registerLoginCallback() {
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }

    }

    private fun registerActivityCallback() {
        activityIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }


}