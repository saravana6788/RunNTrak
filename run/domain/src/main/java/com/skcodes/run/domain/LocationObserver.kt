package com.skcodes.run.domain

import com.skcodes.core.domain.LocationWithAltitude
import kotlinx.coroutines.flow.Flow

interface LocationObserver {
    fun observeLocation(interval:Long): Flow<LocationWithAltitude>
}