package com.rick.morty.services

import com.rick.morty.models.DataModel
import retrofit2.http.GET

// Interface defining the authentication-related network API calls
interface AuthService {

    // A GET request to fetch the list of characters
    // The function is marked as suspend, indicating that it is a coroutine-based function
    // that performs a network operation.
    @GET("api/character")
    suspend fun getCharacters(): DataModel
}
