package com.guaracode.evofit.core.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class NetworkConnectivityObserver : ConnectivityObserver {
    actual override fun observe(): Flow<ConnectivityObserver.Status> {
        return flowOf(ConnectivityObserver.Status.Available)
    }
}
