package com.pratclot.oxygentracker.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pratclot.oxygentracker.data.BodyMeasurement
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyMeasurementsDao {
    @Insert
    suspend fun insert(oxygenMeasurement: BodyMeasurement.OxygenMeasurement): Long

    @Query("SELECT * FROM OxygenMeasurement ORDER BY id DESC")
    fun getAll(): Flow<List<BodyMeasurement.OxygenMeasurement>>
}