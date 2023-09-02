package com.alariclightin.predictiontracker.data.settings

interface DefaultResolveDateRepository {
    fun getDefaultResolveDate(): String
    fun getDefaultResolveTime(): String
}