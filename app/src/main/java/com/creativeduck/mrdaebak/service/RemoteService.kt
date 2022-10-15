package com.creativeduck.mrdaebak.service

import retrofit2.Call
import retrofit2.http.GET

interface RemoteService {

    @GET("pokemon")
    fun listPokemons(): Call<Response>


}

data class Response(
    val count: Int,
    val previous: String,
    val next: String,
    val results: List<PokemonResult>
)

data class PokemonResult(
    val url: String,
    val name: String
)