package com.one.fruitmanseller.ui.maps

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import android.widget.ImageView
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.one.fruitmanseller.R
import com.one.fruitmanseller.utils.extensions.visible
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity() {

    private var markerViewManager : MarkerViewManager? = null
    private var marker : MarkerView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        instanceMapbox()
        setContentView(R.layout.activity_maps)
        setUpMaps(savedInstanceState)
    }

    private fun setUpMaps(savedInstanceState: Bundle?){
        map_view.onCreate(savedInstanceState)
        map_view.getMapAsync{ mapboxMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-6.879704, 109.125595), 8.5))
            locationPicker(mapboxMap)
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {}
        }
    }

    private fun locationPicker(mapboxMap: MapboxMap) {
        markerViewManager = MarkerViewManager(map_view, mapboxMap)
        mapboxMap.addOnMapClickListener { point ->
            marker?.let { markerViewManager?.removeMarker(it) }
            marker = MarkerView(point, iconLocation())
            markerViewManager?.addMarker(marker!!)
            goBackProductActivity(LatLng(point.latitude, point.longitude))
            true
        }
    }

    private fun goBackProductActivity(latLng: LatLng) {
        btn_oke.visible()
        btn_oke.setOnClickListener {
            val intent = Intent()
            intent.putExtra("RESULT_COORDINATE", Coordinate(latLng.latitude.toString(), latLng.longitude.toString()))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun iconLocation() : ImageView {
        return ImageView(this@MapsActivity).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setImageBitmap(BitmapFactory.decodeResource(this@MapsActivity.resources, R.drawable.mapbox_marker_icon_default))
        }
    }

    private fun instanceMapbox() = Mapbox.getInstance(this@MapsActivity, getString(R.string.mapbox_access_token))

    override fun onStart() {
        super.onStart()
        map_view?.onStart()
    }

    override fun onResume() {
        super.onResume()
        map_view?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view?.onPause()
    }

    override fun onStop() {
        super.onStop()
        map_view?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view?.onDestroy()
    }
}

@Parcelize
data class Coordinate(val lat : String? = null, val lng : String?) : Parcelable
