package iberoplast.pe.gespro.io

import iberoplast.pe.gespro.io.response.LoginResponse
import iberoplast.pe.gespro.io.response.SimpleResponse
import iberoplast.pe.gespro.model.Countrie
import iberoplast.pe.gespro.model.MethodPayment
import iberoplast.pe.gespro.model.Policy
import iberoplast.pe.gespro.model.Question
import iberoplast.pe.gespro.model.RequestData
import iberoplast.pe.gespro.model.Supplier
import iberoplast.pe.gespro.model.SupplierRequest
import iberoplast.pe.gespro.model.TypePayment
import iberoplast.pe.gespro.model.User
import iberoplast.pe.gespro.model.ubigeo_peru.Department
import iberoplast.pe.gespro.model.ubigeo_peru.District
import iberoplast.pe.gespro.model.ubigeo_peru.Province
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @POST("supplier/store")
    @Headers("Accept: application/json")
    fun storeSupplier(
        @Header("Authorization") authHeader: String,
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("nic_ruc") nic_ruc: String,
        @Query("nacionality") nacionality: String
    ): Call<Supplier>
    @POST("supplier/{id}/update")
    @Headers("Accept: application/json")
    fun updateSupplier(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Query("name") name: String,
        @Query("nic_ruc") nic_ruc: String,
        @Query("email") email: String
    ): Call<Void>
    @GET("supplier/{id}/edit")
    @Headers("Accept: application/json")
    fun editSupplier(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int)
    : Call<Supplier>

    @GET("request/{id}")
    @Headers("Accept: application/json")
    fun getRequestDetails(
        @Header(value = "Authorization") authHeader: String,
        @Path("id") id: Int
    ): Call<SupplierRequest>
    @GET("supplier/{id}")
    @Headers("Accept: application/json")
    fun getSupplierDetails(
        @Header(value = "Authorization") authHeader: String,
        @Path("id") id: Int
    ): Call<Supplier>
    @GET("user")
    @Headers("Accept: application/json")
    fun getUser(@Header("Authorization") authHeader: String,): Call<User>

    @POST("user")
    @Headers("Accept: application/json")
    fun postUser(
        @Header("Authorization") authHeader: String,
        @Query("name") name: String,
        @Query("email") email: String
        ): Call<Void>
    @POST("login")
    fun postLogin(
        @Query(value = "email") email: String,
        @Query(value = "password") password: String
    ): Call<LoginResponse>

    @POST("register")
    @Headers("Accept: application/json")
    fun postRegister(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("password_confirmation") password_confirmation: String
    ): Call<LoginResponse>

    @POST("fcm/token")
    @Headers("Accept: application/json")
    fun postToken(
        @Header("Authorization") authHeader: String,
        @Query("device_token") token: String,
    ): Call<Void>

    @POST(value = "logout")
    fun postLogout(@Header(value = "Authorization") authHeader: String): Call<Void>
    @GET("suppliers")
    @Headers("Accept: application/json")
    fun getSuppliers(@Header("Authorization") authHeader: String): Call<ArrayList<Supplier>>

    @GET("types-payments")
    fun getTypesPayments(): Call<ArrayList<TypePayment>>

    @GET("methods-payments")
    fun getMethodsPayments(): Call<ArrayList<MethodPayment>>
    @GET("policies")
    fun getPolicies(): Call<ArrayList<Policy>>

    @GET("questions-requests")
    fun getQuestions(): Call<ArrayList<Question>>

    @GET("requests-suppliers")
    @Headers("Accept: application/json")
    fun getSupplierRequests(@Header("Authorization") authHeader: String): Call<ArrayList<SupplierRequest>>

//    @Multipart
    @POST("requests-suppliers")
    @Headers("Accept: application/json")
    fun postSupplierRequests(
        @Header("Authorization") authHeader: String,
        @Query("nacionality") nationality: String,
        @Query("nic_ruc") nic_ruc: String?,
//        @Query("nameSupplier") nameSupplier: String?,
//        @Query("emailSupplier") emailSupplier: String?,
//        @Query("locality") locality: String?,
//        @Query("StreetNumber") StreetNumber: String?,
        @Query("typePayment") typePayment: String?,
        @Query("methodPayment") methodPayment: String?,
//        @Part files: List<MultipartBody.Part>,
        @Body requestData: RequestData
    ): Call<SimpleResponse>

    // start code - get countries/departments/provinces/districts
    @GET("countries")
    fun getCountries(): Call<ArrayList<Countrie>>
    @GET("departments")
    fun getDepartments(): Call<ArrayList<Department>>
    @GET("departments/{department}/provinces")
    fun getProvinces(@Path(/* value = */ "department") departmentId: String): Call<ArrayList<Province>>
    @GET("provinces/{province}/districts")
    fun getDistricts(@Path(/* value = */ "province") provinceId: String): Call<ArrayList<District>>
    // end code

    companion object Factory {
        private const val BASE_URL = "https://gespro-iberoplast.tech/api/"

        fun create(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}