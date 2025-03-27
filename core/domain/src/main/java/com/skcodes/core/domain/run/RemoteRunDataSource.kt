package com.skcodes.core.domain.run

import com.skcodes.core.domain.util.DataError
import com.skcodes.core.domain.util.EmptyResult
import com.skcodes.core.domain.util.Result

interface RemoteRunDataSource {

    suspend fun getRuns():Result<List<Run>,DataError.NetworkError>

    suspend fun postRun(run:Run, mapPicture:ByteArray):Result<Run,DataError.NetworkError>

    suspend fun deleteRun(id:String):EmptyResult<DataError.NetworkError>



}