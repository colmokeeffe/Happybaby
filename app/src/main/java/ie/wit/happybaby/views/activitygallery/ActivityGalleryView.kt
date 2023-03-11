/*package ie.wit.happybaby.views.activitygallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.happybaby.R
import ie.wit.happybaby.databinding.ActivityActivityBinding
import ie.wit.happybaby.models.ActivityModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber.i

class ActivityGalleryView : AppCompatActivity() {

    private lateinit var binding: ActivityActivityBinding
    lateinit var presenter: ActivityGalleryPresenter
    var activity = ActivityModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        presenter = ActivityGalleryPresenter(this)

        i("Activity Activity started..")

        binding.btnAdd.setOnClickListener() {
            if (binding.activityLocation.text.toString().isEmpty()) {
                binding.activityLocation.setError("Mandatory field")
            }
            if (binding.activityTitle.text.toString().isEmpty()) {
                binding.activityTitle.setError("Mandatory field")
            }
            if (binding.activityTitle.text.toString().isEmpty() || binding.activityLocation.text.toString().isEmpty()) {
                Snackbar
                    .make(it, R.string.warning_enterTitle, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doAddorUpdate(
                        binding.activityLocation.text.toString(),
                        binding.activityTitle.text.toString(),
                        binding.activityDescription.text.toString(),
                        binding.activityRating.rating,
                        binding.activityCategory.checkedRadioButtonId,
                        binding.checkboxPriority.isChecked
                    )
                }
            }
        }

        binding.chooseImage.setOnClickListener {
            presenter.cacheActivity(
                binding.activityLocation.text.toString(),
                binding.activityTitle.text.toString(),
                binding.activityDescription.text.toString(),
                binding.activityRating.rating,
                binding.activityCategory.checkedRadioButtonId,
                binding.checkboxPriority.isChecked
            )
            presenter.doSelectImage()
        }

        binding.setActivityLocation.setOnClickListener {
            presenter.cacheActivity(
                binding.activityLocation.text.toString(),
                binding.activityTitle.text.toString(),
                binding.activityDescription.text.toString(),
                binding.activityRating.rating,
                binding.activityCategory.checkedRadioButtonId,
                binding.checkboxPriority.isChecked
            )
            presenter.doSetLocation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit){
            deleteMenu.setVisible(true)
        }
        else{
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_delete -> {
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doDelete()
                }
            }
            android.R.id.home -> {
                presenter.doHome()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showActivity(activity: ActivityModel) {
        binding.activityTitle.setText(activity.title)
        binding.activityDescription.setText(activity.description)
        binding.activityLocation.setText(activity.location)
        binding.activityRating.rating = activity.rating
        binding.checkboxPriority.setChecked(activity.priority)
        var categoryId = when(activity.category) {
            "Poo" -> R.id.option_poo
            "Feed" -> R.id.option_feed
            else -> R.id.option_pee
        }
        binding.activityCategory.check(categoryId)
        if (activity.image != "") {
        Picasso.get()
            .load(activity.image)
            .into(binding.activityImage)
            binding.chooseImage.setText(R.string.button_changeImage)
        }
    }

    fun showEditView() {
        binding.btnAdd.setText(R.string.menu_saveActivity)
        binding.addPlacemarkHeader.setText("Update Activity")
    }

    fun updateImage(image: String){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.activityImage)
        binding.chooseImage.setText(R.string.button_changeImage)
        }

    }

 */