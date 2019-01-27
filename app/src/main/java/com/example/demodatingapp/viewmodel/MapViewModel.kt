package com.example.demodatingapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.demodatingapp.util.LocationLiveData

class MapViewModel: ViewModel() {
    fun currentLocation(context: Context): LocationLiveData {
        return LocationLiveData(context)
    }
}