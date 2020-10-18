package com.pratclot.oxygentracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

sealed class BodyMeasurement {
    @Entity
    data class OxygenMeasurement(
        val oxygenLevel: Int,
        val pulse: Int,
        val timestamp: String
    ) : BodyMeasurement() {
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0

        companion object {
            fun from(text: String): OxygenMeasurement {
                return OxygenMeasurement(
                    text.toInt(),
                    0,
                    ZonedDateTime.now()
                        .truncatedTo(ChronoUnit.SECONDS)
                        .toString()
                )
            }

            fun empty() = OxygenMeasurement.from("0")
        }
    }
}
