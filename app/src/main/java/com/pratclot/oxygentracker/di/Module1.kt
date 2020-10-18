package com.pratclot.oxygentracker.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pratclot.oxygentracker.db.BodyMeasurementsDao
import com.pratclot.oxygentracker.db.BodyMeasurementsDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class Module1 {
    @Singleton
    @Provides
    fun provideDao(bodyMeasurementsDb: BodyMeasurementsDb): BodyMeasurementsDao {
        return bodyMeasurementsDb.bodyMeasurementsDao
    }

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): BodyMeasurementsDb {
        return Room.databaseBuilder(
            context,
            BodyMeasurementsDb::class.java,
            "bodyMeasurementsDb"
        )
            .addMigrations(*provideMigrations())
            .build()
    }

    private fun provideMigrations(): Array<out Migration> {
        return arrayOf(
            object : Migration(1, 2) {
                override fun migrate(p0: SupportSQLiteDatabase) {
                    p0.execSQL("""
                        ALTER TABLE OxygenMeasurement ADD COLUMN timestamp TEXT NOT NULL DEFAULT `timestamp`
                    """.trimIndent())
                }
            }
        )
    }
}