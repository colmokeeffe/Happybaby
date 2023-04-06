package ie.wit.happybaby.views.activitygallerylist

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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import ie.wit.happybaby.R
import ie.wit.happybaby.adapters.ActivityGalleryAdapter
import ie.wit.happybaby.adapters.ImageListener
import ie.wit.happybaby.databinding.ActivityGalleryListBinding
import ie.wit.happybaby.helpers.SwipeToDeleteCallback
import ie.wit.happybaby.main.MainApp
import ie.wit.happybaby.models.ActivityFireStore
import ie.wit.happybaby.models.ActivityGalleryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber.i


class ActivityGalleryListView: AppCompatActivity(), ImageListener {

    lateinit var app: MainApp
    lateinit var presenter: ActivityGalleryListPresenter
    private lateinit var binding: ActivityGalleryListBinding
    private lateinit var activityGalleryList: List<ActivityGalleryModel>
    var gallery = ActivityGalleryModel()
    private var fireStore: ActivityFireStore? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGalleryListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.toolbarAdd.title = title
        binding.toolbarAdd.setTitleTextAppearance(this, R.style.toolbarFont)
        setSupportActionBar(binding.toolbarAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        presenter = ActivityGalleryListPresenter(this)
        app = application as MainApp
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewGallery.layoutManager = layoutManager

        val adapter = binding.recyclerViewGallery.adapter as? ActivityGalleryAdapter

        binding.recyclerViewGallery.adapter?.notifyDataSetChanged()
        binding.imageadder.setOnClickListener{
            presenter.doAddImageActivity()
        }




        GlobalScope.launch(Dispatchers.Main) {
            activityGalleryList = presenter.getGalleries()

        }

        presenter.loadGalleries()






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
                val adapter = binding.recyclerViewGallery.adapter as ActivityGalleryAdapter
                var gallery = activityGalleryList[viewHolder.adapterPosition]
                adapter.removeAt(viewHolder.adapterPosition)
                i("Deleting activity: $gallery")
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doDelete(gallery)
                }
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(binding.recyclerViewGallery)
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


        val searchItem: MenuItem = menu.findItem(R.id.search)

        val searchView: SearchView = searchItem.getActionView() as SearchView
        searchView.setQueryHint(getString(R.string.search_hint))


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

    override fun onActivityGalleryClick(gallery: ActivityGalleryModel) {
        presenter.doEditActivity(gallery)
    }





    fun showGalleries (galleries: MutableList<ActivityGalleryModel>) {

        binding.recyclerViewGallery.adapter = ActivityGalleryAdapter(galleries, this)
        binding.recyclerViewGallery.adapter?.notifyDataSetChanged()
        if (activityGalleryList.isEmpty()) {
            binding.recyclerViewGallery.visibility = View.GONE
            binding.noActivitiesFound.visibility = View.GONE
        } else {
            binding.recyclerViewGallery.visibility = View.VISIBLE
            binding.noActivitiesFound.visibility = View.GONE
        }


        activityGalleryList = galleries
    }

    override fun onResume() {

        presenter.loadGalleries()
        binding.recyclerViewGallery.adapter?.notifyDataSetChanged()
        i("recyclerViewGallery onResume")
        binding.chipOptions.check(R.id.chip_option_all)
        super.onResume()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_delete -> {
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doDelete(gallery)
                }
            }
            android.R.id.home -> {
                presenter.doHome()
            }
        }
        return super.onOptionsItemSelected(item)
    }



}
