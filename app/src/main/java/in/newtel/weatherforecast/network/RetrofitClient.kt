package `in`.newtel.weatherforecast.model.network

import `in`.newtel.weatherforecast.util.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()

    val client: Retrofit
        get() {
            if (retrofit == null) {
                synchronized(Retrofit::class.java) {
                    if (retrofit == null) {

                        val httpClient = OkHttpClient.Builder()
                                .addInterceptor(QueryParameterAddInterceptor())

                        // for pretty log of HTTP request-response
                        /*httpClient.addInterceptor(
                                LoggingInterceptor.Builder()
                                        .loggable(BuildConfig.DEBUG)
                                        .setLevel(Level.BASIC)
                                        .log(Platform.INFO)
                                        .request("LOG")
                                        .response("LOG")
                                        .executor(Executors.newSingleThreadExecutor())
                                        .build())*/

                        val client = httpClient.build()

                        retrofit = Retrofit.Builder()
                                .baseUrl(Constants.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .client(client)
                                .build()
                    }
                }

            }
            return retrofit!!
        }
}