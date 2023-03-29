package ie.wit.happybaby.views.activitygallery

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.happybaby.R
import ie.wit.happybaby.adapters.ImageListener
import ie.wit.happybaby.databinding.ActivityGalleryHappybabyBinding
import ie.wit.happybaby.models.ActivityGalleryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber.i

class ActivityGalleryView : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryHappybabyBinding
    lateinit var presenter: ActivityGalleryPresenter
    var gallery = ActivityGalleryModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryHappybabyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        binding.toolbarAdd.setTitleTextAppearance(this, R.style.toolbarFont)
        setSupportActionBar(binding.toolbarAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        presenter = ActivityGalleryPresenter(this)

        i("Activity Activity started..")

        binding.btnAddImage.setOnClickListener() {
            if (binding.imageTitle.text.toString().isEmpty()) {
                binding.imageTitle.setError("Mandatory field")
            }
            if (binding.imageTitle.text.toString().isEmpty()) {
                Snackbar
                    .make(it, R.string.warning_enterTitle, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doAddorUpdate(
                        binding.imageTitle.text.toString(),
                        binding.imageDescription.text.toString(),
                        binding.imageCategory.checkedRadioButtonId,
                        binding.checkboxFavourite.isChecked
                    )
                }
            }
        }

        binding.chooseImage.setOnClickListener {
            presenter.cacheActivity(
                binding.imageTitle.text.toString(),
                binding.imageDescription.text.toString(),
                binding.imageCategory.checkedRadioButtonId,
                binding.checkboxFavourite.isChecked
            )
            presenter.doSelectImage()
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_delete -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Are you sure?")
                builder.setMessage("Do you want to delete this activity?")
                builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                    GlobalScope.launch(Dispatchers.IO) {
                        presenter.doDelete()
                    }
                }
                builder.setNegativeButton("No", { dialogInterface: DialogInterface, i: Int -> })
                builder.show()
            }
            android.R.id.home -> {
                presenter.doHome()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        val shareMenu: MenuItem = menu.findItem(R.id.item_share)
        if (presenter.edit){
            deleteMenu.setVisible(true)
            shareMenu.setVisible(false)
        }
        else{
            deleteMenu.setVisible(false)
            shareMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun showActivity(gallery: ActivityGalleryModel) {
        binding.imageTitle.setText(gallery.imagetitle)
        binding.imageDescription.setText(gallery.imagedescription)
        binding.checkboxFavourite.setChecked(gallery.favourite)
        var categoryId = when(gallery.imagecategory) {
            "newborn" -> R.id.option_newborn
            "3-6m" -> R.id.option_threeToSix
            "6-9m" -> R.id.option_sixToNine
            else -> R.id.option_nineToTwelve
        }
        binding.imageCategory.check(categoryId)
        if (gallery.image != "") {
        Picasso.get()
            .load(gallery.image)
            .into(binding.activityImage)
            binding.chooseImage.setText(R.string.button_changeImage)
        }
    }

    fun showEditView() {
        binding.btnAddImage.setText(R.string.menu_saveImage)
        binding.addActivityImage.setText(R.string.menu_updateImage)
    }

    fun updateImage(image: String){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.activityImage)
        binding.chooseImage.setText(R.string.button_changeImage)
        }

    }

