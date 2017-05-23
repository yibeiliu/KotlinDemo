package tech.yibeiliu.myappbykotlin.Module

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tech.yibeiliu.myappbykotlin.api.APIService
import tech.yibeiliu.myappbykotlin.bean.Movies

/**
 * Created by LiuPeiyi on 2017/05/23.
 */
/**
 * Created by YiBeiLiu on 2017/04/25.
 *
 *
 * 为了体现单一职责原则，将访问网络的具体代码抽出放在 HttpModelImpl 中，让 presenter 只负责调度（主持人的角色）
 */

class HttpModelImpl private constructor() : IHttpModel {

    /**
     * 下拉刷新

     * @param city
     * *
     * @param start
     * *
     * @param count
     */
    override fun requestMoviesList(city: String, start: Int, count: Int, observer: Observer<Movies>) {
        APIService.Builder.apiService
                .getMoviesList(city, start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

    /**
     * 上拉加载更多

     * @param city
     * *
     * @param start
     * *
     * @param count
     */
    override fun requestMoviesListMore(city: String, start: Int, count: Int, observer: Observer<Movies>) {
        APIService.Builder.apiService
                .getMoviesList(city, start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

    companion object {

        fun getInstance() = HttpModelImplHolder.instance

    }

    private object HttpModelImplHolder {
        val instance = HttpModelImpl()
    }
}
