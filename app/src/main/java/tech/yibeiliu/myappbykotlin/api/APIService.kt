package tech.yibeiliu.myappbykotlin.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import tech.yibeiliu.myappbykotlin.bean.Movies

/**
 * Created by LiuPeiyi on 2017/05/23.
 */
interface APIService {

    object Builder {
        val apiService: APIService
            get() = RetrofitClient.getInstance().getAPIService(APIService::class.java, BASE_URL)
    }

    @GET("in_theaters?apikey=0b2bdeda43b5688921839c8ecb20399b")
    fun getMoviesList(@Query("city") city: String, @Query("start") start: Int, @Query("count") count: Int): Observable<Movies>

    companion object {

        //    https://api.douban.com/v2/movie/in_theaters?apikey=0b2bdeda43b5688921839c8ecb20399b&city=%E5%8C%97%E4%BA%AC&start=0&count=100

        val BASE_URL = "https://api.douban.com/v2/movie/"
    }

}