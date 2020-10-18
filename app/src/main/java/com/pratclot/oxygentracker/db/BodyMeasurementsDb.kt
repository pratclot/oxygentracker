package com.pratclot.oxygentracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pratclot.oxygentracker.data.BodyMeasurement

@Database(entities = [BodyMeasurement.OxygenMeasurement::class], version = 2, exportSchema = false)
abstract class BodyMeasurementsDb: RoomDatabase() {
    abstract val bodyMeasurementsDao: BodyMeasurementsDao
}