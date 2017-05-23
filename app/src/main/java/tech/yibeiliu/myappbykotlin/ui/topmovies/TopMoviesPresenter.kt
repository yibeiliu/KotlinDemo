package tech.yibeiliu.myappbykotlin.ui.topmovies

import android.content.Context
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import tech.yibeiliu.myappbykotlin.Module.HttpModelImpl
import tech.yibeiliu.myappbykotlin.Module.IHttpModel
import tech.yibeiliu.myappbykotlin.bean.Movies

/**
 * Created by LiuPeiyi on 2017/05/23.
 */
class TopMoviesPresenter(context: Context, private var mView: ITopMoviesContract.IView?) : ITopMoviesContract.IPresenter {

    private val disposableSet: CompositeDisposable = CompositeDisposable() //Rxjava2.0新增，通过他可以管理订阅
    private val COUNT = 10
    private var start = 0  // 请求起始
    private var total = 0  // 服务器共有 total 个电影数据
    private val mHttpModelImpl: IHttpModel


    init {
        mView!!.setPresenter(this)
        mHttpModelImpl = HttpModelImpl.getInstance()

    }

    override fun unSubscribe() {
        disposableSet.clear()
    }

    override fun onDestroy() {
        unSubscribe()//解除订阅同时取消网络访问
        mView = null//防止内存泄漏
    }

    /**
     * 下拉刷新

     * @param city
     */
    override fun onRefresh(city: String) {
        start = 0//刷新时，从 0 开始加载
        //调用 M 层访问网络
        val observer = object : Observer<Movies> {
            override fun onSubscribe(d: Disposable) {
                //注册该 observer，方便取消订阅与取消访问
                disposableSet.add(d)
            }

            override fun onNext(movies: Movies) {

                if (mView != null) {  //如果 fragment 销毁了，但是网络访问回来的结果又调用了 mView.responseMoviesList 导致空指针异常
                    //把获得的 movies 对象返回给 view 展示
                    mView!!.responseMoviesList(movies.subjects)
                    total = movies.total//记录一下服务器上总共有多少个数据，方便本地判断是否还有更多数据需要加载
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                //错误处理
                if (mView != null) {
                    mView!!.responseError(e.message!!)
                }
            }

            override fun onComplete() {}
        }

        mHttpModelImpl.requestMoviesList(city, start, COUNT, observer)
    }

    /**
     * 上拉加载更多

     * @param city
     */
    override fun onLoadMore(city: String) {
        start += COUNT//往后推 COUNT 个数据
        if (start > total) {
            // 没有更多的数据了
            if (mView != null) {
                mView!!.noMoreData()
            }
            return
        }

        val observer = object : Observer<Movies> {
            override fun onSubscribe(d: Disposable) {
                disposableSet.add(d)
            }

            override fun onNext(movies: Movies) {
                total = movies.total
                if (mView != null) {
                    mView!!.responseMoviesListMore(movies.subjects)
                }
            }

            override fun onError(e: Throwable) {
                if (mView != null) {
                    mView!!.responseError(e.message!!)
                }
            }

            override fun onComplete() {}
        }


        mHttpModelImpl.requestMoviesListMore(city, start, COUNT, observer)
    }
}
