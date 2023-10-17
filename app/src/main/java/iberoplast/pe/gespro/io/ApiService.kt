package iberoplast.pe.gespro.io

import iberoplast.pe.gespro.model.StateRequest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("states")
    fun getStates(): Call<ArrayList<StateRequest>>

    companion object Factory {
        private const val BASE_URL = "https://gespro-iberoplast.000webhostapp.com/api/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}