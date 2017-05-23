package tech.yibeiliu.myappbykotlin.ui.topmovies

import tech.yibeiliu.myappbykotlin.IBasePresenter
import tech.yibeiliu.myappbykotlin.IBaseView
import tech.yibeiliu.myappbykotlin.bean.*

/**
 * Created by LiuPeiyi on 2017/05/23.
 */
interface ITopMoviesContract {
    interface IPresenter : IBasePresenter {

        fun onDestroy()

        fun onRefresh(city: String)

        fun onLoadMore(city: String)

    }

    interface IView : IBaseView<IPresenter> {

        fun responseMoviesList(subjects: List<Subjects>)

        fun responseError(message: String)

        fun noMoreData()

        fun responseMoviesListMore(subjects: List<Subjects>)

    }
}