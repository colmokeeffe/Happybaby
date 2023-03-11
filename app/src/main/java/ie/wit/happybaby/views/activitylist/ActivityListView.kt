package ie.wit.happybaby.views.activitylist

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import ie.wit.happybaby.R
import ie.wit.happybaby.adapters.ActivityAdapter
import ie.wit.happybaby.adapters.ActivityListener
import ie.wit.happybaby.databinding.ActivityActivityListBinding
import ie.wit.happybaby.helpers.SwipeToDeleteCallback
import ie.wit.happybaby.main.MainApp
import ie.wit.happybaby.models.ActivityModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber.i


class ActivityListView : AppCompatActivity(), ActivityListener {

    lateinit var app: MainApp
    lateinit var presenter: ActivityListPresenter
    lateinit var bottomNav : BottomNavigationView
    private lateinit var binding: ActivityActivityListBinding
    private lateinit var activityList: List<ActivityModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.toolbar.title = "${user.email}"
        }
        setSupportActionBar(binding.toolbar)
        //setSwipeRefresh()
        presenter = ActivityListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.adder.setOnClickListener{
            presenter.doAddActivity()
        }




        GlobalScope.launch(Dispatchers.Main) {
            activityList = presenter.getActivities()
        }
        loadActivities()


        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    true
                }
                R.id.item_add -> {
                    presenter.doAddActivity()
                    true
                }
                R.id.item_map -> {
                    presenter.doAddActivity()
                    true
                }
                R.id.item_logout -> {
                    GlobalScope.launch(Dispatchers.IO) {
                        presenter.doLogout()
                    }
                    true
                }
                else -> false
            }
        }

        binding.chipOptions.check(R.id.chip_option_all)
        binding.chipOptions.setOnCheckedChangeListener(object: ChipGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(chipGroup: ChipGroup, id: Int) {
                var selectedChip = chipGroup.findViewById(id) as Chip
                GlobalScope.launch(Dispatchers.Main) {
                    presenter.doChipChange(selectedChip.id)
                }
            }
        })


             val swipeDeleteHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerView.adapter as ActivityAdapter
                var activity = activityList[viewHolder.adapterPosition]
                adapter.removeAt(viewHolder.adapterPosition)
                i("Deleting activity: $activity")
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doDelete(activity)
                }
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(binding.recyclerView)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val item = menu.findItem(R.id.switchTheme) as MenuItem
        item.setActionView(R.layout.switch_layout)
        val switchTheme: SwitchCompat =  item.actionView!!.findViewById(R.id.switchForActionBar)
        switchTheme.isChecked = false

        val sharedPreferences = getSharedPreferences("save", MODE_PRIVATE)
        switchTheme.setChecked(sharedPreferences.getBoolean("value", true))

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value", true)
                editor.apply()
                switchTheme.setChecked(true)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value", false)
                editor.apply()
                switchTheme.setChecked(false)
            }
        }

        // get menu item
        val searchItem: MenuItem = menu.findItem(R.id.search)
        // getting search view of menu item and set query hint
        val searchView: SearchView = searchItem.getActionView() as SearchView
        searchView.setQueryHint(getString(R.string.search_hint))

        // call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val checkedChip = binding.chipOptions.checkedChipId
                GlobalScope.launch(Dispatchers.Main) {
                    presenter.doSearch(query, checkedChip)
                }
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                val checkedChip = binding.chipOptions.checkedChipId
                GlobalScope.launch(Dispatchers.Main) {
                    presenter.doSearch(msg, checkedChip)
                }
                return false
            }
        })
        return true
    }

    override fun onActivityClick(activity: ActivityModel) {
        presenter.doEditActivity(activity)
    }

    private fun loadActivities() {
        GlobalScope.launch(Dispatchers.Main) {
            showActivities(presenter.getActivities() as MutableList<ActivityModel>)
        }
    }

    fun showActivities (activities: MutableList<ActivityModel>) {
        binding.recyclerView.adapter = ActivityAdapter(activities, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
        if (activityList.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.noActivitiesFound.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.noActivitiesFound.visibility = View.GONE
        }
        //update activity list here to avoid bug in swipe to delete when using filtered data
        activityList = activities
        //checkSwipeRefresh()
    }


    override fun onResume() {
        loadActivities()
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("recyclerView onResume")
        bottomNav.setSelectedItemId(R.id.item_home)
        binding.chipOptions.check(R.id.chip_option_all)
        super.onResume()
    }


}
