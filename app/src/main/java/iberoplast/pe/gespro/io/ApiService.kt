package iberoplast.pe.gespro.io

import Role
import iberoplast.pe.gespro.io.response.LoginResponse
import iberoplast.pe.gespro.io.response.SimpleResponse
import iberoplast.pe.gespro.io.response.charts.ChartDataResponse
import iberoplast.pe.gespro.io.response.charts.ResponseCounts
import iberoplast.pe.gespro.io.response.charts.UserRoleResponse
import iberoplast.pe.gespro.model.Countrie
import iberoplast.pe.gespro.model.MethodPayment
import iberoplast.pe.gespro.model.Policy
import iberoplast.pe.gespro.model.Question
import iberoplast.pe.gespro.model.RequestData
import iberoplast.pe.gespro.model.StateRequest
import iberoplast.pe.gespro.model.Supplier
import iberoplast.pe.gespro.model.SupplierRequest
import iberoplast.pe.gespro.model.TypePayment
import iberoplast.pe.gespro.model.User
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @Multipart
    @POST("upload")
    @Headers("Accept: application/json")
    fun uploadFiles(@Header("Authorization") authHeader: String, @Part files: List<MultipartBody.Part>): Call<Void>
    //CHARTS
    @GET("requests-by-weekend")
    @Headers("Accept: application/json")
    fun requestsByWeek(): Call<ChartDataResponse>
    @GET("counts")
    fun getCounts(): Call<ResponseCounts>

    @GET("getUsersByRole")
    @Headers("Accept: application/json")
    fun getUsersByRole(): Call<List<UserRoleResponse>>
    //END CHARTS

    //CRUD USERS
    @POST("user/{id}/update")
    @Headers("Accept: application/json")
    fun updateUser(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("id_role") id_role: Int?
    ): Call<Void>

    @GET("user/{id}/edit")
    @Headers("Accept: application/json")
    fun editUser(@Header("Authorization") authHeader: String,@Path("id") id: Int): Call<User>

    @GET("users")
    @Headers("Accept: application/json")
    fun getUsers(@Header("Authorization") authHeader: String): Call<ArrayList<User>>

    @POST("user/store")
    @Headers("Accept: application/json")
    fun postUser(
        @Header("Authorization") authHeader: String,
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("id_role") id_role: Int?
    ): Call<Void>
    // END CRUD USERS

    //CRUD ROL QUESTIONS
    @POST("question/{id}/update")
    @Headers("Accept: application/json")
    fun updateQuestion(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Query("question") question: String
    ): Call<Void>
    @GET("question/{id}/edit")
    @Headers("Accept: application/json")
    fun editQuestion(@Header("Authorization") authHeader: String,@Path("id") id: Int): Call<Question>
    @GET("questions")
    @Headers("Accept: application/json")
    fun getQuestions(@Header("Authorization") authHeader: String): Call<ArrayList<Question>>
    @POST("question/store")
    @Headers("Accept: application/json")
    fun postQuestion(
        @Header("Authorization") authHeader: String,
        @Query("question") question: String
    ): Call<Void>
    // END CRUD ROL QUESTIONS

    //CRUD METHODS PAYMENTS
    @POST("method-payment/{id}/update")
    @Headers("Accept: application/json")
    fun updateMethodPayment(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Query("name") name: String,
        @Query("description") description: String
    ): Call<Void>
    @GET("method-payment/{id}/edit")
    @Headers("Accept: application/json")
    fun editMethodPayment(@Header("Authorization") authHeader: String,@Path("id") id: Int): Call<MethodPayment>
    @POST("method-payment/store")
    @Headers("Accept: application/json")
    fun postMethodPayment(
        @Header("Authorization") authHeader: String,
        @Query("name") name: String,
        @Query("description") description: String
    ): Call<Void>
    // END CRUD METHODS PAYMENTS

    //CRUD CONDITIONS PAYMENTS
    @POST("type-or-condition-payment/{id}/update")
    @Headers("Accept: application/json")
    fun updateConditionPayment(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Query("name") name: String,
        @Query("description") description: String
    ): Call<Void>
    @GET("type-or-condition-payment/{id}/edit")
    @Headers("Accept: application/json")
    fun editConditionPayment(@Header("Authorization") authHeader: String,@Path("id") id: Int): Call<TypePayment>
    @GET("types-or-conditions-payments")
    @Headers("Accept: application/json")
    fun getConditionsPayments(@Header("Authorization") authHeader: String): Call<ArrayList<TypePayment>>
    @POST("type-or-condition-payment/store")
    @Headers("Accept: application/json")
    fun postConditionPayment(
        @Header("Authorization") authHeader: String,
        @Query("name") name: String,
        @Query("description") description: String
    ): Call<Void>
    // END CRUD CONDITIONS PAYMENTS

    //CRUD ROL STATES
    @POST("state/{id}/update")
    @Headers("Accept: application/json")
    fun updateState(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Query("description") description: String
    ): Call<Void>
    @GET("state/{id}/edit")
    @Headers("Accept: application/json")
    fun editState(@Header("Authorization") authHeader: String,@Path("id") id: Int): Call<StateRequest>
    @GET("states")
    @Headers("Accept: application/json")
    fun getStates(@Header("Authorization") authHeader: String): Call<ArrayList<StateRequest>>
    @POST("state/store")
    @Headers("Accept: application/json")
    fun postState(
        @Header("Authorization") authHeader: String,
        @Query("name") name: String,
        @Query("description") description: String
    ): Call<Void>
    // END CRUD ROL STATES

    //CRUD ROL USERS
    @POST("role/{id}/update")
    @Headers("Accept: application/json")
    fun updateRole(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Query("description") description: String
    ): Call<Void>
    @GET("role/{id}/edit")
    @Headers("Accept: application/json")
    fun editRole(@Header("Authorization") authHeader: String,@Path("id") id: Int): Call<Role>
    @GET("roles")
    @Headers("Accept: application/json")
    fun getRoles(@Header("Authorization") authHeader: String): Call<ArrayList<Role>>
    @POST("role/store")
    @Headers("Accept: application/json")
    fun postRole(
        @Header("Authorization") authHeader: String,
        @Query("name") name: String,
        @Query("description") description: String
    ): Call<Void>
    // END CRUD ROL USERS


    @POST("supplier/store")
    @Headers("Accept: application/json")
    fun storeSupplier(
        @Header("Authorization") authHeader: String,
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("nic_ruc") nic_ruc: String,
        @Query("locality") locality: String,
        @Query("street_and_number") street_and_number: String?, // Puede ser nulo
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
    @POST("request/{id}/cancel")
    @Headers("Accept: application/json")
    fun cancelRequest(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int
    ): Call<Void>
    @POST("request/{id}/disapprove")
    @Headers("Accept: application/json")
    fun disapproveRequest(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int
    ): Call<Void>
    @POST("request/{id}/approve")
    @Headers("Accept: application/json")
    fun approveRequest(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int
    ): Call<Void>
    @POST("request/{id}/validate")
    @Headers("Accept: application/json")
    fun validateRequest(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int
    ): Call<Void>
    @POST("request/{id}/receive")
    @Headers("Accept: application/json")
    fun receiveRequest(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int
    ): Call<Void>
    @POST("request/{id}/update")
    @Headers("Accept: application/json")
    fun updateRequest(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Query("nacionality") nationality: String,
        @Query("nic_ruc") nic_ruc: String?,
        @Query("locality") locality: String,
        @Query("street_and_number") street_and_number: String?, // Puede ser nulo
        @Query("typePayment") typePayment: String?,
        @Query("methodPayment") methodPayment: String?,
        @Body requestData: RequestData
    ): Call<Void>
    @GET("request/{id}/edit")
    @Headers("Accept: application/json")
    fun editRequest(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int)
            : Call<SupplierRequest>
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
    fun getUser(@Header("Authorization") authHeader: String): Call<User>

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
    @Headers("Accept: application/json")
    fun getMethodsPayments(@Header("Authorization") authHeader: String): Call<ArrayList<MethodPayment>>
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
        @Query("locality") locality: String?,
        @Query("street_and_number") street_and_number: String?,
        @Query("typePayment") typePayment: String?,
        @Query("methodPayment") methodPayment: String?,
//        @Part files: List<MultipartBody.Part>,

        @Body requestData: RequestData
    ): Call<SimpleResponse>
    @GET("countries")
    fun getCountries(): Call<ArrayList<Countrie>>
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