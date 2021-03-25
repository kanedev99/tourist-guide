package vinova.kane.touristguide.di

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vinova.kane.touristguide.BuildConfig
import vinova.kane.touristguide.api.ApiHelper
import vinova.kane.touristguide.api.ApiHelperImpl
import vinova.kane.touristguide.api.ApiService
import vinova.kane.touristguide.utils.Api.Companion.BASE_URL
import vinova.kane.touristguide.utils.NetworkHelper
import java.util.concurrent.TimeUnit

val retrofitModule = module {
//    single { provideOkHttpClient() }
    single { createOkHttpClient() }
    single { retrofit() }
    single { provideApiService(get()) }
    single { provideNetworkHelper(androidContext()) }
    single<ApiHelper> { return@single ApiHelperImpl(get()) }
}

private fun provideNetworkHelper(context: Context) = NetworkHelper(context)

//private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
//    val loggingInterceptor = HttpLoggingInterceptor()
//    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//    OkHttpClient.Builder()
//        .addInterceptor(loggingInterceptor)
//        .connectTimeout(2, TimeUnit.MINUTES)
//        .writeTimeout(2, TimeUnit.MINUTES)
//        .readTimeout(2, TimeUnit.MINUTES)
//        .build()
//} else OkHttpClient
//    .Builder()
//    .connectTimeout(2, TimeUnit.MINUTES)
//    .writeTimeout(2, TimeUnit.MINUTES)
//    .readTimeout(2, TimeUnit.MINUTES)
//    .build()

fun createOkHttpClient( ): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
    addTimeout(clientBuilder)
    clientBuilder.addInterceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        requestBuilder
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .method(original.method, original.body)
        chain.proceed(requestBuilder.build())
    }.build()


    clientBuilder.addInterceptor(loggingInterceptor())


    return clientBuilder.build()
}


/**
 * This method add timeout for CRUD API calls
 * @param clientBuilder Builder
 */
private fun addTimeout(clientBuilder: OkHttpClient.Builder) {
    clientBuilder
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
}


/**
 * This method add logging interceptop
 * The logs are at level body and will only be added for debug mode
 * @return HttpLoggingInterceptor
 */
fun loggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoggingInterceptor
}

private fun retrofit() = Retrofit.Builder()
    .callFactory(OkHttpClient.Builder().build())
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private fun provideApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)