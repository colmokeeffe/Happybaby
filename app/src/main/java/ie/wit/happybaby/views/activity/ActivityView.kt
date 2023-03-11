package ie.wit.happybaby.views.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TimePicker.OnTimeChangedListener
import androidx.appcompat.app.AppCompatActivity
import ie.wit.happybaby.R
import ie.wit.happybaby.databinding.ActivityActivityBinding
import ie.wit.happybaby.models.ActivityModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber.i
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener


class ActivityView : AppCompatActivity() {

    private lateinit var binding: ActivityActivityBinding
    lateinit var presenter: ActivityPresenter
    var activity = ActivityModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        presenter = ActivityPresenter(this)





        i("Activity Activity started..")

        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute -> // Convert the selected time to a formatted string
            activity.selectedTime = String.format("%02d:%02d", hourOfDay, minute)

            // Update the textView to display the selected time
            binding.timeTextView.setText(activity.selectedTime)
        }

        binding.datePicker.setOnDateChangedListener { _, year, month, day -> // Get the selected time value from the datepicker
            val month = month + 1
            activity.selectedDate = String.format("%02d/%02d/%02d", day, month, year)
            // Update the textView with the selected time value
            binding.dateTextView.setText(activity.selectedDate)
        }

        binding.btnAdd.setOnClickListener() {
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doAddorUpdate(
                        binding.dateTextView.text.toString(),
                        binding.timeTextView.text.toString(),
                        binding.activityDescription.text.toString(),
                        binding.activityRating.rating,
                        binding.activityCategory.checkedRadioButtonId,
                        binding.checkboxPriority.isChecked
                    )
                }
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
        binding.dateTextView.setText(activity.selectedDate)
        binding.timeTextView.setText(activity.selectedTime)
        binding.activityDescription.setText(activity.description)
        binding.activityRating.rating = activity.rating
        binding.checkboxPriority.setChecked(activity.priority)
        var categoryId = when(activity.category) {
            "Poo" -> R.id.option_poo
            "Feed" -> R.id.option_feed
            "Medication" -> R.id.option_medication
            "Exercise" -> R.id.option_exercise
            else -> R.id.option_pee
        }

        binding.activityCategory.check(categoryId)
    }


    fun showEditView() {
        binding.btnAdd.setText(R.string.menu_saveActivity)
        binding.addActivity.setText("Update Activity")
    }

}