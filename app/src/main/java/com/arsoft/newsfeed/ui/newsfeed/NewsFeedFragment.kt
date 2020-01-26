package com.arsoft.newsfeed.ui.newsfeed

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.adapters.NewsFeedRecyclerAdapter
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.arsoft.newsfeed.data.newsfeed.request.NewsFeedResponse
import com.arsoft.newsfeed.mvp.newsfeed.NewsFeedPresenter
import com.arsoft.newsfeed.mvp.newsfeed.NewsFeedView
import com.arsoft.newsfeed.navigation.screens.Screens
import kotlinx.android.synthetic.main.fragment_newsfeed.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class NewsFeedFragment: MvpAppCompatFragment(), NewsFeedView, NewsFeedRecyclerAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsFeedRecyclerAdapter

    companion object{
        fun getNewInstance(accessToken: String) = NewsFeedFragment().apply {
            arguments = Bundle().apply {
                putString("access_token", accessToken)
            }
        }
    }

    @Inject
    lateinit var router: Router

    @InjectPresenter
    lateinit var presenter: NewsFeedPresenter

    init {
        NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_newsfeed, container, false)
        recyclerView = view.findViewById(R.id.newsfeed_recycler)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            presenter.loadNewsFeed(arguments?.getString("access_token")!!)
        }

        adapter = NewsFeedRecyclerAdapter(onItemClickListener = this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.newsfeed_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.logout -> {
                router.newRootChain(Screens.LoginScreen())
                NewsFeedApplication.prefs!!.accessToken = "0"
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //MARK - View implementation

    override fun loadNewsFeed(items: ArrayList<FeedItemModel>) {
        adapter.setupNewsFeedList(items = items)
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        recyclerView.visibility = View.INVISIBLE
        newsfeed_cpv.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        recyclerView.visibility = View.VISIBLE
        newsfeed_cpv.visibility = View.INVISIBLE
    }

    override fun showEmptyList() {
    }

    override fun showError(message: String) {
    }

    // OnItemClickListener implementation
    override fun onPhotoClick(photoURLs: ArrayList<String>, position: Int) {
        router.navigateTo(Screens.ViewPhotoScreen(photoURLs = photoURLs,position =  position))
    }

    override fun onVideoClick(videoID: String, videoOwnerID: String) {
        router.navigateTo(Screens.VideoPlayerScreen(videoID = videoID, videoOwnerID = videoOwnerID))
    }
}