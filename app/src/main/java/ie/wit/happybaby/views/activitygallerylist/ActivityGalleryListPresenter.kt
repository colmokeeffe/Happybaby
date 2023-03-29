package ie.wit.happybaby.views.activitygallerylist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.wit.happybaby.R
import ie.wit.happybaby.databinding.ActivityGalleryListBinding
import ie.wit.happybaby.main.MainApp
import ie.wit.happybaby.models.ActivityFireStore
import ie.wit.happybaby.models.ActivityGalleryModel
import ie.wit.happybaby.views.activitygallery.ActivityGalleryView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActivityGalleryListPresenter (private val view: ActivityGalleryListView) {

    var app: MainApp = view.application as MainApp
    var gallery = ActivityGalleryModel()
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>
    var fireStore: ActivityFireStore? = null
    private lateinit var binding: ActivityGalleryListBinding
    private lateinit var activityGalleryList: List<ActivityGalleryModel>
    private lateinit var activityIntentLauncher : ActivityResultLauncher<Intent>


    init {

        app = view.application as MainApp
        registerRefreshCallback()
        registerLoginCallback()
        registerActivityCallback()

        if (app.galleries is ActivityFireStore) {
            fireStore = app.galleries as ActivityFireStore
        }
        GlobalScope.launch(Dispatchers.Main) {

            activityGalleryList = getGalleries()
            fireStore!!.fetchGalleries { loadGalleries() }

        }


    }


    fun doAddImageActivity() {
        val launcherIntent = Intent(view, ActivityGalleryView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }



    fun doEditActivity(gallery: ActivityGalleryModel) {
        val launcherIntent = Intent(view, ActivityGalleryView::class.java)
        launcherIntent.putExtra("gallery_edit", gallery)
        refreshIntentLauncher.launch(launcherIntent)
    }


    fun doCancel() {
        view.finish()
    }

    fun doHome() {
        view.finish()
    }

    suspend fun doSearch(query: String, checkedChip: Int) {
        var chipCategory = when (checkedChip) {
            R.id.chip_option_newborn -> "Newborn"
            R.id.chip_option_threeToSix -> "3-6m"
            R.id.chip_option_sixToNine -> "6-9m"
            R.id.chip_option_nineToTwelve -> "9-12m"
            else -> "All"
        }
        val filteredGallerylist: MutableList<ActivityGalleryModel> = mutableListOf()
        val galleries = app.galleries.findActivitiesGalleryByCategory(chipCategory)

        for (item in galleries) {
            if (item.imagedescription.lowercase().contains(query.lowercase())) {
                filteredGallerylist.add(item)
                // i("item added to list")
            }
        }
        view.showGalleries(filteredGallerylist)
    }

    suspend fun doChipChange(selectedChip: Int) {
        var chipCategory = when (selectedChip) {
            R.id.chip_option_newborn -> "Newborn"
            R.id.chip_option_threeToSix -> "3-6m"
            R.id.chip_option_sixToNine -> "6-9m"
            R.id.chip_option_nineToTwelve -> "9-12m"
            else -> "All"
        }
        var filteredGallerylist: MutableList<ActivityGalleryModel>
        var galleries = app.galleries.findAllGalleries().toMutableList()

        when (chipCategory) {
            "All" -> {
                filteredGallerylist = galleries
            }
            "Newborn" -> {
                filteredGallerylist =
                    galleries.filter { it.imagecategory == "Newborn" } as MutableList<ActivityGalleryModel>
            }

            "3-6m" -> {
                filteredGallerylist =
                    galleries.filter { it.imagecategory == "3-6m" } as MutableList<ActivityGalleryModel>
            }

            "6-9m" -> {
                filteredGallerylist =
                    galleries.filter { it.imagecategory == "6-9m" } as MutableList<ActivityGalleryModel>
            }

            "9-12m" -> {
                filteredGallerylist =
                    galleries.filter { it.imagecategory == "9-12m" } as MutableList<ActivityGalleryModel>
            }

            else -> {
                filteredGallerylist = galleries
            }
        }
        view.showGalleries(filteredGallerylist)
    }
    
    suspend fun getGalleries() = app.galleries.findAllGalleries()

    fun loadGalleries() {
        GlobalScope.launch(Dispatchers.Main) {
            //fireStore!!.fetchGalleries {  }
            view.showGalleries(getGalleries() as MutableList<ActivityGalleryModel>)
        }
    }


    suspend fun doDelete(gallery: ActivityGalleryModel) {
        app.galleries.deleteGallery(gallery)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main){
                    getGalleries()
                }
            }
    }

    private fun registerActivityCallback() {
        activityIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }


    private fun registerLoginCallback() {
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }

}

   