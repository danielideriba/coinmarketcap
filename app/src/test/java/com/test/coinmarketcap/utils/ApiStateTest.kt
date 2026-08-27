package com.test.coinmarketcap.utils

import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiStateTest {

    // -------------------------------------------------------------------------
    // safeApiCall — successful response with a non-null body
    // -------------------------------------------------------------------------

    @Test
    fun `safeApiCall returns Success when response is successful and body is not null`() = runTest {
        val body = "result"
        val result = safeApiCall { Response.success(body) }

        assertTrue(result is ApiState.Success)
        assertEquals(body, (result as ApiState.Success).data)
    }

    // -------------------------------------------------------------------------
    // safeApiCall — successful response with null body
    // -------------------------------------------------------------------------

    @Test
    fun `safeApiCall returns Error with empty body message when body is null`() = runTest {
        val result = safeApiCall<String> { Response.success(null) }

        assertTrue(result is ApiState.Error)
        val error = result as ApiState.Error
        assertEquals("Empty body", error.message)
        assertNull(error.code)
    }

    // -------------------------------------------------------------------------
    // safeApiCall — 404 HTTP error
    // -------------------------------------------------------------------------

    @Test
    fun `safeApiCall returns Error with code 404 and Not found message on 404 response`() = runTest {
        val errorBody = "".toResponseBody("application/json".toMediaType())
        val result = safeApiCall<String> { Response.error(404, errorBody) }

        assertTrue(result is ApiState.Error)
        val error = result as ApiState.Error
        assertEquals(404, error.code)
        assertEquals("Not found", error.message)
    }

    // -------------------------------------------------------------------------
    // safeApiCall — other HTTP errors (e.g. 500)
    // -------------------------------------------------------------------------

    @Test
    fun `safeApiCall returns Error with response code and message on generic HTTP error`() = runTest {
        val errorBody = "Internal Server Error".toResponseBody("text/plain".toMediaType())
        val result = safeApiCall<String> { Response.error(500, errorBody) }

        assertTrue(result is ApiState.Error)
        val error = result as ApiState.Error
        assertEquals(500, error.code)
        // Retrofit sets the message from the HTTP reason phrase; we just verify it is not null/blank
        assertTrue(!error.message.isNullOrBlank())
    }

    @Test
    fun `safeApiCall returns Error with code 401 on unauthorised response`() = runTest {
        val errorBody = "".toResponseBody("application/json".toMediaType())
        val result = safeApiCall<String> { Response.error(401, errorBody) }

        assertTrue(result is ApiState.Error)
        assertEquals(401, (result as ApiState.Error).code)
    }

    // -------------------------------------------------------------------------
    // safeApiCall — SocketTimeoutException
    // -------------------------------------------------------------------------

    @Test
    fun `safeApiCall returns Error with code -1 on SocketTimeoutException`() = runTest {
        val result = safeApiCall<String> { throw SocketTimeoutException("timeout") }

        assertTrue(result is ApiState.Error)
        val error = result as ApiState.Error
        assertEquals(-1, error.code)
        assertEquals("timeout", error.message)
    }

    @Test
    fun `safeApiCall returns fallback message when SocketTimeoutException message is null`() = runTest {
        val result = safeApiCall<String> { throw SocketTimeoutException() }

        assertTrue(result is ApiState.Error)
        val error = result as ApiState.Error
        assertEquals(-1, error.code)
        assertEquals("Servidor inacessível", error.message)
    }

    // -------------------------------------------------------------------------
    // safeApiCall — UnknownHostException
    // -------------------------------------------------------------------------

    @Test
    fun `safeApiCall returns Error with no internet message on UnknownHostException`() = runTest {
        val result = safeApiCall<String> { throw UnknownHostException("host not found") }

        assertTrue(result is ApiState.Error)
        val error = result as ApiState.Error
        assertEquals(-1, error.code)
        assertEquals("Sem conexão com a internet", error.message)
    }

    // -------------------------------------------------------------------------
    // safeApiCall — generic Exception
    // -------------------------------------------------------------------------

    @Test
    fun `safeApiCall returns Error with code -1 on generic Exception`() = runTest {
        val result = safeApiCall<String> { throw RuntimeException("something went wrong") }

        assertTrue(result is ApiState.Error)
        val error = result as ApiState.Error
        assertEquals(-1, error.code)
        assertEquals("something went wrong", error.message)
    }

    @Test
    fun `safeApiCall returns fallback message when generic Exception message is null`() = runTest {
        val result = safeApiCall<String> { throw object : Exception() {} }

        assertTrue(result is ApiState.Error)
        val error = result as ApiState.Error
        assertEquals(-1, error.code)
        assertEquals("Erro inesperado", error.message)
    }

    // -------------------------------------------------------------------------
    // apiError helper
    // -------------------------------------------------------------------------

    @Test
    fun `apiError returns ApiState Error with given message and null code`() {
        val result: ApiState<String> = apiError("Something failed")

        assertTrue(result is ApiState.Error)
        val error = result as ApiState.Error
        assertEquals("Something failed", error.message)
        assertNull(error.code)
    }

    @Test
    fun `apiError with empty string returns ApiState Error with empty message`() {
        val result: ApiState<Int> = apiError("")

        assertTrue(result is ApiState.Error)
        assertEquals("", (result as ApiState.Error).message)
    }

    // -------------------------------------------------------------------------
    // ApiState.Success — data class properties
    // -------------------------------------------------------------------------

    @Test
    fun `ApiState Success holds the provided data`() {
        val success = ApiState.Success(42)
        assertEquals(42, success.data)
    }

    @Test
    fun `ApiState Success equality is based on data value`() {
        val a = ApiState.Success("hello")
        val b = ApiState.Success("hello")
        assertEquals(a, b)
    }

    @Test
    fun `ApiState Success copy reflects updated data`() {
        val original = ApiState.Success(100)
        val copy = original.copy(data = 200)
        assertEquals(200, copy.data)
    }

    // -------------------------------------------------------------------------
    // ApiState.Error — data class properties
    // -------------------------------------------------------------------------

    @Test
    fun `ApiState Error holds message and code`() {
        val error = ApiState.Error(message = "Not found", code = 404)
        assertEquals("Not found", error.message)
        assertEquals(404, error.code)
    }

    @Test
    fun `ApiState Error code defaults to null when not provided`() {
        val error = ApiState.Error(message = "oops")
        assertNull(error.code)
    }

    @Test
    fun `ApiState Error equality is based on message and code`() {
        val a = ApiState.Error("fail", 500)
        val b = ApiState.Error("fail", 500)
        assertEquals(a, b)
    }

    @Test
    fun `ApiState Error copy reflects updated fields`() {
        val original = ApiState.Error("original error", 400)
        val copy = original.copy(message = "updated error", code = 422)
        assertEquals("updated error", copy.message)
        assertEquals(422, copy.code)
    }
}
