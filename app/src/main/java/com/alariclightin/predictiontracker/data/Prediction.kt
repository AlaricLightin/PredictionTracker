@file:UseSerializers(KOffsetDateTimeSerializer::class)
package com.alariclightin.predictiontracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alariclightin.predictiontracker.utils.serializers.KOffsetDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.OffsetDateTime

@Serializable
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