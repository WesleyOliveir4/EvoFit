package com.guaracode.evofit.core.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}

expect class NetworkConnectivityObserver : ConnectivityObserver {
    override fun observe(): Flow<ConnectivityObserver.Status>
}
