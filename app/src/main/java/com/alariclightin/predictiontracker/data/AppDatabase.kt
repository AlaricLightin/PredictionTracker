package com.alariclightin.predictiontracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Prediction::class], version = 1, exportSchema = false)
@TypeConverters(OffsetDateTimeTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun predictionsDao(): PredictionsDao
}