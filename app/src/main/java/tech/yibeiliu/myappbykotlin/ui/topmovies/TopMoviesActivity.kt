package tech.yibeiliu.myappbykotlin.ui.topmovies

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import tech.yibeiliu.myappbykotlin.R

class TopMoviesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolBar()

        var topMoviesFragment: TopMoviesFragment? = supportFragmentManager
                .findFragmentById(R.id.fragment_container) as TopMoviesFragment?

        if (topMoviesFragment == null) {
            topMoviesFragment = TopMoviesFragment()

            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragment_container, topMoviesFragment)
            transaction.commit()
        }
        // 把 view 传递给 presenter
        TopMoviesPresenter(this, topMoviesFragment)
    }

    private fun initToolBar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = "TopMovies"
        setSupportActionBar(toolbar)
    }

}
