/*package ie.wit.happybaby.views.activitiesmap

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.squareup.picasso.Picasso
import ie.wit.happybaby.R
import ie.wit.happybaby.databinding.ActivityActivityMapsBinding
import ie.wit.happybaby.databinding.ContentActivityMapsBinding
import ie.wit.happybaby.main.MainApp
import ie.wit.happybaby.models.ActivityModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActivitiesMapView : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityActivityMapsBinding
    private lateinit var contentBinding: ContentActivityMapsBinding
    lateinit var presenter: ActivitiesMapPresenter
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        presenter = ActivitiesMapPresenter(this)

        contentBinding = ContentActivityMapsBinding.bind(binding.root)

        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync {
            GlobalScope.launch(Dispatchers.Main) {
                presenter.doPopulateMap(it)
            }
        }

        val spinner: Spinner = findViewById(R.id.map_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.maps_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem == "Satellite") {
                    contentBinding.mapView.getMapAsync {
                        GlobalScope.launch(Dispatchers.Main) {
                            presenter.doUpdateMapType(it, "Satellite")
                        }
                    }
                }
                if (selectedItem == "Normal") {
                    contentBinding.mapView.getMapAsync {
                        GlobalScope.launch(Dispatchers.Main) {
                            presenter.doUpdateMapType(it, "Normal")
                        }
                    }
                }
                if (selectedItem == "Terrain") {
                    contentBinding.mapView.getMapAsync {
                        GlobalScope.launch(Dispatchers.Main) {
                            presenter.doUpdateMapType(it, "Terrain")
                        }
                    }
                }
            }
        }

    }

    fun showActivity(activity: ActivityModel) {
        contentBinding.currentTitle.text = activity?.title
        contentBinding.currentDescription.text = activity?.description
        contentBinding.currentCategory.text = activity?.category
        if (activity.image != "") {
            Picasso.get().load(activity?.image).resize(250, 250).into(contentBinding.currentImage)
        }
        contentBinding.mapView.getMapAsync {
            val loc = LatLng(activity.lat, activity.lng)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10f))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        GlobalScope.launch(Dispatchers.Main) {
            presenter.doMarkerSelected(marker)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                presenter.doHome()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

 */