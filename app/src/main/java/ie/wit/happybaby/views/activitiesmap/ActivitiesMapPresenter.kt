/*package ie.wit.happybaby.views.activitiesmap

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.happybaby.main.MainApp

class ActivitiesMapPresenter (val view: ActivitiesMapView) {

    var app: MainApp

    init{
        app = view.application as MainApp
    }

    suspend fun doPopulateMap(map: GoogleMap) {
        map.setOnMarkerClickListener(view)
        map.uiSettings.setZoomControlsEnabled(true)
        //map.mapType = GoogleMap.MAP_TYPE_HYBRID
        // var customMarker = BitmapDescriptorFactory.fromResource(R.drawable....)
        var colourMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        app.activities.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc).icon(colourMarker)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
        val lastActivity = app.activities.findAll().lastOrNull()
        if (lastActivity != null) {
            view.showActivity(lastActivity)
        }
    }

    fun doUpdateMapType(map: GoogleMap, type: String) {
        when (type) {
            "Satellite" -> {
                map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
            "Normal" -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            "Terrain" -> {
                map.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
            else -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }
    }

    suspend fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
        val activity = app.activities.findActivityById(tag)
        if (activity != null) view.showActivity(activity)
    }

    fun doHome() {
        view.finish()
    }
}

 */