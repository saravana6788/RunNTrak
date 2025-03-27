package com.skcodes.run.network

import com.skcodes.core.data.networking.constructRoute
import com.skcodes.core.data.networking.delete
import com.skcodes.core.data.networking.get
import com.skcodes.core.data.networking.safeCall
import com.skcodes.core.domain.run.RemoteRunDataSource
import com.skcodes.core.domain.run.Run
import com.skcodes.core.domain.util.DataError
import com.skcodes.core.domain.util.EmptyResult
import com.skcodes.core.domain.util.Result
import com.skcodes.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class KtorRunDataSource(
    private val httpClient:HttpClient
):RemoteRunDataSource {
    override suspend fun getRuns(): Result<List<Run>, DataError.NetworkError> {
        return httpClient.get<List<RunDto>>(
            route = "/runs",
        ).map { runDtos ->
            runDtos.map {runDto ->
                runDto.toRun()
            }
        }
    }

    override suspend fun postRun(
        run: Run,
        mapPicture: ByteArray
    ): Result<Run, DataError.NetworkError> {
        val createRunRequestJson = Json.encodeToString(run.toCreateRunRequest())
        return safeCall<RunDto> {
            httpClient.submitFormWithBinaryData(
                url = constructRoute("/run"),
                formData = formData {
                    append("MAP_PICTURE",mapPicture, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=mappicture.jpg")
                    })

                    append("RUN_DATA",createRunRequestJson,Headers.build {
                        append(HttpHeaders.ContentType, "text/plain")
                        append(HttpHeaders.ContentDisposition, "form-data; name=\"RUN_DATA\"")
                    })
                }
            ){
               method = HttpMethod.Post
            }
        }.map {
            it.toRun()
        }
    }

    override suspend fun deleteRun(id: String): EmptyResult<DataError.NetworkError> {
        return httpClient.delete(
            route = "/run",
            queryParameters = mapOf(
                "id" to id
            )
        )
    }


}