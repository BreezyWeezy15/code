package com.rick.morty.services

import com.rick.morty.models.DataModel
import retrofit2.http.GET

interface AuthService {


    @GET("api/character")
    suspend fun getCharacters() : DataModel
}