package com.one.fruitmanseller.ui.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.one.fruitmanseller.utils.SingleLiveEvent

class MapsViewModel () : ViewModel(){
    private val state : SingleLiveEvent<MapsState> = SingleLiveEvent()
    private val markerViewManager = MutableLiveData<MarkerViewManager>()
    private val markerView = MutableLiveData<MarkerViewManager>()

    private fun isLoading(b : Boolean){ state.value = MapsState.Loading(b) }
    private fun toast(message: String) { state.value = MapsState.ShowToast(message)}

    fun setMarkerViewManager(marker : MarkerView){
        markerViewManager.value?.addMarker(marker)
    }


    fun listenToMarkerViewManager() = markerViewManager
    fun listenToMarkerView() = markerView

}

sealed class MapsState{
    data class Loading(var state : Boolean = false) : MapsState()
    data class ShowToast(var message : String) : MapsState()
}