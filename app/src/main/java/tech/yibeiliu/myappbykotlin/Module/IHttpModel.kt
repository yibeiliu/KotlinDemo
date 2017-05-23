package tech.yibeiliu.myappbykotlin.Module

import io.reactivex.Observer
import tech.yibeiliu.myappbykotlin.bean.Movies

/**
 * Created by LiuPeiyi on 2017/05/23.
 */
interface IHttpModel {
    fun requestMoviesList(city: String, start: Int, count: Int, observer: Observer<Movies>)
    fun requestMoviesListMore(city: String, start: Int, count: Int, observer: Observer<Movies>)
}