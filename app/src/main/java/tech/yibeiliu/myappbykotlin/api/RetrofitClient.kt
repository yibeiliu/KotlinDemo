package tech.yibeiliu.myappbykotlin.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by LiuPeiyi on 2017/05/23.
 */
class RetrofitClient private constructor() {

    @Volatile private var apiService: Any? = null
    private var client: OkHttpClient? = null

    init {
        initInterceptor()//初始化 okhttp 拦截器
    }

    private object RetrofitClientHolder {
        val INSTANCE = RetrofitClient()
    }

    companion object {
        fun getInstance() = RetrofitClientHolder.INSTANCE
    }

    fun <T> getAPIService(clazz: Class<T>, baseUrl: String): T {
        if (apiService == null) {
            synchronized(RetrofitClient::class.java) {
                if (apiService == null) {
                    val mRetrofit = Retrofit.Builder()
                            .client(client)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(baseUrl)
                            .build()
                    apiService = mRetrofit.create(clazz)
                }
            }
        }
        return apiService as T
    }

    private fun initInterceptor() {
        // okHttp 日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val a = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            response
        }

        client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(a)
                .build()
    }
}