/*package ie.wit.happybaby.views.activitygallery

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import ie.wit.happybaby.R
import ie.wit.happybaby.views.editlocation.EditLocationView
import ie.wit.happybaby.databinding.ActivityActivityBinding
import ie.wit.happybaby.helpers.showImagePicker
import ie.wit.happybaby.main.MainApp
import ie.wit.happybaby.models.Location
import ie.wit.happybaby.models.ActivityModel
import ie.wit.happybaby.views.editlocation.checkLocationPermissions
import timber.log.Timber

class ActivityGalleryPresenter (private val view: ActivityGalleryView) {

    var activity = ActivityModel()
    var binding: ActivityActivityBinding = ActivityActivityBinding.inflate(view.layoutInflater)
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    val location = Location(52.245696, -7.139102, 15f)

    var edit = false
    var app: MainApp = view.application as MainApp

    init {

        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()

        if (view.intent.hasExtra("activity_edit")) {
            edit = true
            activity = view.intent.extras?.getParcelable("activity_edit")!!
            view.showActivity(activity)
            view.showEditView()
        }
        else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            activity.lat = location.lat
            activity.lng = location.lng
        }
        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()
    }

    suspend fun doAddorUpdate(location: String, title: String, description: String, rating: Float, category: Int, priority: Boolean  ) {
        activity.location = location
        activity.title = title
        activity.description = description
        activity.rating = rating
        activity.priority = priority
        activity.category = when (category) {
            R.id.option_pee -> "Pee"
            R.id.option_poo -> "Poo"
            R.id.option_feed -> "Feed"
            else -> "N/A"
        }
        if (edit) {
            app.activities.update(activity.copy())
        } else {
            app.activities.create(activity.copy())
        }
        view.finish()
    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {
        if (activity.zoom != 0f) {
            location.lat = activity.lat
            location.lng = activity.lng
            location.zoom = activity.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun cacheActivity (location: String, title: String, description: String, rating: Float, category: Int, priority: Boolean) {
        activity.location = location
        activity.title = title
        activity.description = description
        activity.rating = rating
        activity.priority = priority
        activity.category = when (category) {
            R.id.option_pee -> "Pee"
            R.id.option_poo -> "Poo"
            R.id.option_feed -> "Feed"
            else -> "N/A"
        }
    }

    suspend fun doDelete() {
        app.activities.delete(activity)
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    fun doHome() {
        view.finish()
    }

    fun locationUpdate(lat: Double, lng: Double) {
        activity.lat = lat
        activity.lng = lng
        activity.zoom = 15f
        // view.showActivity(activity)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        Timber.i("setting location from doSetLocation")
        locationService.lastLocation.addOnSuccessListener {
            if (it != null) {
                locationUpdate(it.latitude, it.longitude)
            } else {
                locationUpdate(-8.69163, 115.15783)
            }
        }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            activity.image = result.data!!.data!!.toString()
                            view.updateImage(activity.image)
                            Picasso.get()
                                .load(activity.image)
                                .into(binding.activityImage)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            activity.lat = location.lat
                            activity.lng = location.lng
                            activity.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun doPermissionLauncher() {
        Timber.i("permission check called")
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }
}

 */