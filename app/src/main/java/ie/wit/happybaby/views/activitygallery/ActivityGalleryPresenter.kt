package ie.wit.happybaby.views.activitygallery


import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import ie.wit.happybaby.R

import ie.wit.happybaby.databinding.ActivityGalleryHappybabyBinding

import ie.wit.happybaby.helpers.showImagePicker
import ie.wit.happybaby.main.MainApp
import ie.wit.happybaby.models.ActivityGalleryModel

import timber.log.Timber

@SuppressLint("SuspiciousIndentation")
class ActivityGalleryPresenter (private val view: ActivityGalleryView) {

    var gallery = ActivityGalleryModel()
    var binding: ActivityGalleryHappybabyBinding = ActivityGalleryHappybabyBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false
    var app: MainApp = view.application as MainApp


    init {
        registerImagePickerCallback()

        if (view.intent.hasExtra("gallery_edit")) {
            edit = true
            gallery = view.intent.extras?.getParcelable("gallery_edit")!!
            view.showActivity(gallery)
            view.showEditView()
        }
        else
        registerImagePickerCallback()
    }

    suspend fun doAddorUpdate(imagetitle: String, imagedescription: String, imagecategory: Int, favourite: Boolean  ) {
        gallery.imagetitle = imagetitle
        gallery.imagedescription = imagedescription
        gallery.favourite = favourite
        gallery.imagecategory = when (imagecategory) {
            R.id.option_sixToNine -> "6-9m"
            R.id.option_threeToSix -> "3-6m"
            R.id.option_nineToTwelve -> "9-12m"
            R.id.option_newborn -> "Newborn"
            else -> "N/A"
        }
        if (edit) {
            app.galleries.updateGallery(gallery.copy())
        } else {
            app.galleries.createGallery(gallery.copy())
        }
        view.finish()
    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }


    fun cacheActivity (imagetitle: String, imagedescription: String, imagecategory: Int, favourite: Boolean) {
        gallery.imagetitle = imagetitle
        gallery.imagedescription = imagedescription
        gallery.favourite = favourite
        gallery.imagecategory = when (imagecategory) {
            R.id.option_sixToNine -> "6-9m"
            R.id.option_threeToSix -> "3-6m"
            R.id.option_nineToTwelve -> "9-12m"
            R.id.option_newborn -> "Newborn"
            else -> "N/A"
        }
    }

    suspend fun doDelete() {
        app.galleries.deleteGallery(gallery)
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    fun doHome() {
        view.finish()
    }


    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            gallery.image = result.data!!.data!!.toString()
                            view.updateImage(gallery.image)
                            Picasso.get()
                                .load(gallery.image)
                                .into(binding.activityImage)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
