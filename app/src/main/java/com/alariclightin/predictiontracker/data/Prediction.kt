package com.alariclightin.predictiontracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "predictions")
data class Prediction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val probability: Int,
    val predictionDate: OffsetDateTime,
    val resolveDate: OffsetDateTime,
    val result: Boolean? = null
)