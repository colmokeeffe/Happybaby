package ie.wit.happybaby.views.reminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import ie.wit.happybaby.R
import ie.wit.happybaby.databinding.ActivityMainBinding
import ie.wit.happybaby.helpers.ReminderWorker
import timber.log.Timber

import java.util.*
import java.util.concurrent.TimeUnit


private lateinit var binding: ActivityMainBinding
lateinit var presenter: ReminderPresenter

class ReminderView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        binding.toolbarAdd.setTitleTextAppearance(this, R.style.toolbarFont)
        setSupportActionBar(binding.toolbarAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        presenter = ReminderPresenter(this)

        Timber.i("Activity Reminder started..")



        //Create Variables to hold user's selection
        var chosenYear = 0
        var chosenMonth = 0
        var chosenDay = 0
        var chosenHour = 0
        var chosenMin = 0

        //Access View Components using their Id
        val descriptionText = findViewById<EditText>(R.id.editText)
        val button = findViewById<Button>(R.id.setBtn)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val timeReminder = findViewById<TextView>(R.id.timeReminder)
        val dateReminder = findViewById<TextView>(R.id.dateReminder)
        val today = Calendar.getInstance()

        //Initialize of datePicker using the current day as starting parameters and then
        //pass the userSelected to the variables created
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            chosenYear = year
            chosenMonth = month
            chosenDay = day

            val userSelectedDateString = String.format("%02d/%02d/%02d", chosenDay, chosenMonth, chosenYear)
            dateReminder.setText(userSelectedDateString)
        }



        //Add the Listener to gain access to user selection in the TimePicker and
        //then assign the selected values to the variables created above
        timePicker.setOnTimeChangedListener { _, hour, minute ->
            chosenHour = hour
            chosenMin  = minute

            val userSelectedTimeString = String.format("%02d:%02d", chosenHour, chosenMin)
            timeReminder.setText(userSelectedTimeString)
        }


        //Add the Listener to listen to click events and execute the code to setNotification
        button.setOnClickListener {

            //Get the DateTime the user selected
            val userSelectedDateTime =Calendar.getInstance()
            userSelectedDateTime.set(chosenYear, chosenMonth, chosenDay, chosenHour , chosenMin)



            //Next get DateTime for today
            val todayDateTime = Calendar.getInstance()

            //
            val delayInSeconds = (userSelectedDateTime.timeInMillis/1000L) - (todayDateTime.timeInMillis/1000L)

            //
            createWorkRequest(descriptionText.text.toString(), delayInSeconds)

            //
            Toast.makeText(this, "Reminder set", Toast.LENGTH_SHORT).show()
        }

    }



    // Private Function to create the OneTimeWorkRequest
    private fun createWorkRequest(message: String,timeDelayInSeconds: Long  ) {
        val myWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
            .setInputData(workDataOf(
                "title" to "Happy Baby Reminder",
                "message" to message,
            )
            )
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)
        finish()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }

            android.R.id.home -> {
                presenter.doHome()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}