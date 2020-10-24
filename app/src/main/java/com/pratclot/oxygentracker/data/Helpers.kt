package com.pratclot.oxygentracker.data

import com.github.mikephil.charting.data.Entry
import org.threeten.bp.ZonedDateTime

fun String.toEpochSecond() = ZonedDateTime.parse(this).toInstant().epochSecond

fun entryFromMeasurement(oxygenMeasurement: BodyMeasurement.OxygenMeasurement) = Entry(
    oxygenMeasurement.timestamp.toEpochSecond().toFloat(),
    oxygenMeasurement.oxygenLevel.toFloat()
)