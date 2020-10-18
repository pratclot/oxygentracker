package com.pratclot.oxygentracker.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pratclot.oxygentracker.data.BodyMeasurement
import com.pratclot.oxygentracker.db.BodyMeasurementsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class OxygenActivityViewModel @ViewModelInject constructor(
    val dao: BodyMeasurementsDao
) : ViewModel() {
    val oxygenMeasurements: LiveData<List<BodyMeasurement.OxygenMeasurement>>
        get() = dao.getAll().asLiveData()

    suspend fun addMeasurement(value: String) = withContext(Dispatchers.IO) {
        dao.insert(BodyMeasurement.OxygenMeasurement.from(value))
    }

    suspend fun getAll() = withContext(Dispatchers.IO) { dao.getAll().distinctUntilChanged() }
}