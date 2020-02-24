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
import com.arsoft.newsfeed.data.likes.request.LikesResponse
import com.arsoft.newsfeed.data.models.CommentModel
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.arsoft.newsfeed.helpers.recycler.NewsFeedItemDecoration
import com.arsoft.newsfeed.mvp.newsfeed.NewsFeedPresenter
import com.arsoft.newsfeed.mvp.newsfeed.NewsFeedView
import com.arsoft.newsfeed.navigation.screens.Screens
import kotlinx.android.synthetic.main.feed_item.view.*
import kotlinx.android.synthetic.main.fragment_newsfeed.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class NewsFeedFragment: MvpAppCompatFragment(), NewsFeedView, NewsFeedRecyclerAdapter.LoadMoreOwner,
    NewsFeedRecyclerAdapter.NewsFeedViewHolder.OnNewsFeedItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsFeedRecyclerAdapter
    private var accessToken: String? = ""

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

        accessToken = arguments?.getString("access_token")

        if (arguments != null) {
            presenter.loadNewsFeed(accessToken = accessToken!!)
        }

        swipeRefreshLayout.setOnRefreshListener {
            presenter.refreshNewsFeed(accessToken = accessToken!!)
            swipeRefreshLayout.isRefreshing = false
        }

        adapter = NewsFeedRecyclerAdapter(onNewsFeedItemClickListener = this)
        adapter.setLoadMoreCallback(this)
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(NewsFeedItemDecoration(20))
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        setHasOptionsMenu(true)

//        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if(newState == adapter.itemCount) {
//
//                }
//            }
//        })

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

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun loadNewsFeed(items: ArrayList<FeedItemModel>) {
        adapter.setupNewsFeedList(items = items)
        adapter.notifyDataSetChanged()
    }

    override fun loadMoreNewsFeed(items: ArrayList<FeedItemModel>) {
        adapter.addMoreNewsFeedListItems(items)
        adapter.notifyDataSetChanged()
    }

    override fun refreshNewsFeed(items: ArrayList<FeedItemModel>) {
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


    override fun updateLikesCount(likes: LikesResponse, viewItemId: Long) {
        with(recyclerView.findViewHolderForItemId(viewItemId)!!.itemView.likes_count_textview){
            if (likes.response.likes == 0) {
                text = ""
            } else {
                text = likes.response.likes.toString()
            }
        }
    }

    override fun loadMore(startFrom: String) {
        presenter.loadMoreNewsFeed(accessToken = accessToken!!, startFrom = startFrom)
    }

    // OnItemClickListener implementation
    override fun onPhotoClick(photoURLs: ArrayList<String?>, position: Int) {
        router.navigateTo(Screens.ViewPhotoScreen(photoURLs = photoURLs,position =  position))
    }

    override fun onVideoClick(videoID: String, videoOwnerID: String) {
        router.navigateTo(Screens.VideoPlayerScreen(videoID = videoID, videoOwnerID = videoOwnerID))
    }

    override fun onAddLikeClick(type: String ,ownerId: Long, itemId: Long, viewItemId: Long) {
        presenter.addLike(type = type, ownerId = ownerId, itemId = itemId, viewItemId = viewItemId, accessToken = arguments!!.getString("access_token")!!)
    }

    override fun onDeleteLikeClick(type: String, ownerId: Long, itemId: Long, viewItemId: Long) {
        presenter.deleteLike(type = type, ownerId = ownerId, itemId = itemId, viewItemId = viewItemId, accessToken = arguments!!.getString("access_token")!!)
    }

    override fun onCommentsButtonClick(model: FeedItemModel) {
        router.navigateTo(Screens.CommentsScreen(model = model))
    }

    override fun onReplyButtonClick(model: CommentModel, itemId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}