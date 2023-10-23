package iberoplast.pe.gespro.io

import iberoplast.pe.gespro.model.MethodPayment
import iberoplast.pe.gespro.model.StateRequest
import iberoplast.pe.gespro.model.Supplier
import iberoplast.pe.gespro.model.SupplierRequest
import iberoplast.pe.gespro.model.TypePayment
import iberoplast.pe.gespro.model.ubigeo_peru.Department
import iberoplast.pe.gespro.model.ubigeo_peru.District
import iberoplast.pe.gespro.model.ubigeo_peru.Province
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("states")
    fun getStates(): Call<ArrayList<StateRequest>>
    @GET("suppliers")
    fun getSuppliers(): Call<ArrayList<Supplier>>

    @GET("types-payments")
    fun getTypesPayments(): Call<ArrayList<TypePayment>>

    @GET("methods-payments")
    fun getMethodsPayments(): Call<ArrayList<MethodPayment>>

    @GET("requests-suppliers")
    fun getSupplierRequests(): Call<ArrayList<SupplierRequest>>

    // start code - get departments/provinces/districts
    @GET("departments")
    fun getDepartments(): Call<ArrayList<Department>>
    @GET("departments/{department}/provinces")
    fun getProvinces(@Path("department") departmentId: Int): Call<ArrayList<Province>>
    @GET("districts")
    fun getDistricts(): Call<ArrayList<District>>
    // end code

    companion object Factory {
        private const val BASE_URL = "https://gespro-iberoplast.tech/api/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}