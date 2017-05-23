package tech.yibeiliu.myappbykotlin.ui.topmovies


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import tech.yibeiliu.myappbykotlin.R
import tech.yibeiliu.myappbykotlin.bean.Subjects
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class TopMoviesFragment : Fragment(), ITopMoviesContract.IView, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private var mRecyclerView: RecyclerView? = null
    private var pullToRefreshAdapter: PullToRefreshAdapter? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mPresenter: ITopMoviesContract.IPresenter? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_top_movies, container, false)

        mRecyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        mRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        mSwipeRefreshLayout!!.setOnRefreshListener(this)
        mSwipeRefreshLayout!!.setColorSchemeColors(Color.RED, Color.MAGENTA, Color.BLUE)

        initAdapter()
        return view
    }

    override fun onResume() {
        super.onResume()
        onRefresh()//首次进入时刷新页面
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter!!.onDestroy()//页面销毁时，与该页面相关的 P 层和 M 层也应该销毁
    }


    override fun setPresenter(t: ITopMoviesContract.IPresenter) {
        this.mPresenter = t
    }

    override fun responseMoviesList(subjects: List<Subjects>) {
        pullToRefreshAdapter!!.setDataRefresh(subjects)
        mSwipeRefreshLayout!!.isRefreshing = false
        pullToRefreshAdapter!!.setEnableLoadMore(true)
    }

    override fun responseError(message: String) {
        mSwipeRefreshLayout!!.isEnabled = true
        mSwipeRefreshLayout!!.isRefreshing = false
        Toast.makeText(activity, "错误信息： " + message, Toast.LENGTH_SHORT).show()
        pullToRefreshAdapter!!.setEnableLoadMore(true)
    }

    override fun noMoreData() {
        mSwipeRefreshLayout!!.isEnabled = true
        pullToRefreshAdapter!!.loadMoreEnd(false)
    }

    override fun responseMoviesListMore(subjects: List<Subjects>) {
        mSwipeRefreshLayout!!.isEnabled = true
        pullToRefreshAdapter!!.setDataMore(subjects)//给 adapter 去刷新数据
        pullToRefreshAdapter!!.loadMoreComplete()
    }

    override fun onLoadMoreRequested() {
        mSwipeRefreshLayout!!.isEnabled = false//上拉加载的同时不能下拉刷新
        mPresenter!!.onLoadMore("北京")
    }

    override fun onRefresh() {
        mSwipeRefreshLayout!!.setRefreshing(true)//显示加载小圆圈
        pullToRefreshAdapter!!.setEnableLoadMore(false)//下拉刷新的同时不能上拉加载
        mPresenter!!.onRefresh("北京")
    }

    private fun initAdapter() {
        pullToRefreshAdapter = PullToRefreshAdapter()
        pullToRefreshAdapter!!.setOnLoadMoreListener(this, mRecyclerView)//利用 BaseQuickAdapter 实现 上拉加载更多
        pullToRefreshAdapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)//开启动画
        mRecyclerView!!.setAdapter(pullToRefreshAdapter)

    }

    /**
     * 该 adapter 采用了 BRVAH ，参见：http://www.recyclerview.org/
     * 使用该 adapter 可以很方便的简化 recyclerView 的 adapter，并且一句代码实现上拉加载，配合 SwipeRefreshLayout 下拉刷新，十分酸爽
     */

    internal inner class PullToRefreshAdapter : BaseQuickAdapter<Subjects, BaseViewHolder>(R.layout.list_item_topmovies, ArrayList<Subjects>()) {

        override fun convert(helper: BaseViewHolder, item: Subjects) {
            helper.setText(R.id.itemTitle, item.title)
            helper.setText(R.id.itemdes, "大陆 " + item.mainland_pubdate + " 上映  片长: " + item.durations)
            Glide.with(activity).load(item.images.medium).into(helper.getView(R.id.itemImage))
        }

        fun setDataRefresh(lists: List<Subjects>) {
            this.data.clear()
            this.data.addAll(lists)
            notifyDataSetChanged()
        }

        fun setDataMore(lists: List<Subjects>) {
            this.data.addAll(lists)
            notifyDataSetChanged()
        }
    }

}
