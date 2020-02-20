package com.arsoft.newsfeed.ui.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.adapters.AttachmentsRecyclerAdapter
import com.arsoft.newsfeed.adapters.CommetnsRecyclerAdapter
import com.arsoft.newsfeed.adapters.NewsFeedRecyclerAdapter
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.data.models.CommentModel
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.arsoft.newsfeed.helpers.MyDateTimeFormatHelper
import com.arsoft.newsfeed.helpers.Prefs
import com.arsoft.newsfeed.helpers.recycler.MultipleSpanGridLayoutManager
import com.arsoft.newsfeed.mvp.comments.CommentsPresenter
import com.arsoft.newsfeed.mvp.comments.CommentsView
import com.arsoft.newsfeed.navigation.screens.Screens
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.fragment_comments.post_avatar
import kotlinx.android.synthetic.main.fragment_comments.post_date_time
import kotlinx.android.synthetic.main.fragment_comments.post_source_name
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CommentsFragment: MvpAppCompatFragment(), CommentsView, NewsFeedRecyclerAdapter.NewsFeedViewHolder.OnNewsFeedItemClickListener {

    companion object {
        fun getNewInstance(model: FeedItemModel) = CommentsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("model", model)
            }
        }
    }

    @Inject
    lateinit var router: Router

    @InjectPresenter
    lateinit var presenter: CommentsPresenter

    init {
        NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(this)
    }

    private lateinit var attachmentsRecyclerAdapter: AttachmentsRecyclerAdapter
    private lateinit var commentsAdapter: CommetnsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model: FeedItemModel = arguments!!.getParcelable("model")!!
        setupPost(model = model)

        val accessToken = Prefs(context = context!!).accessToken

        presenter.loadCommetns(
            accessToken = accessToken,
            ownerId = model.ownerId,
            postId = model.postId
        )
        commentsAdapter = CommetnsRecyclerAdapter()
        comments_recycler_view.adapter = commentsAdapter
        comments_recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }



    private fun setupPost(model: FeedItemModel) {
        Glide.with(context!!)
            .load(model.avatar)
            .into(post_avatar)
        post_source_name.text = model.sourceName
        val currentTime = Calendar.getInstance().timeInMillis
        post_date_time.text = MyDateTimeFormatHelper.timeFormat(postDate = model.date * 1000, currentTime = currentTime)
        post_text_view.text = model.postText
        likes_count_textview.text = model.likes.count.toString()
        comments_count_textview.text = model.comments.count.toString()
        reposts_count_textview.text = model.reposts.count.toString()
        views_count_textview.text = model.views.count.toString()

        attachmentsRecyclerAdapter = AttachmentsRecyclerAdapter(this)
        attachments_recycler_view.layoutManager = MultipleSpanGridLayoutManager(
            context = context!!,
            spanCount = 4,
            items = model.attachments
        )
        attachments_recycler_view.setHasFixedSize(true)
        attachments_recycler_view.isNestedScrollingEnabled = false
        attachments_recycler_view.adapter = attachmentsRecyclerAdapter
        attachmentsRecyclerAdapter.setupAttachments(attachments = model.attachments)
        attachmentsRecyclerAdapter.notifyDataSetChanged()
    }

    // MARK --- View Implementation

    override fun loadCommentsList(commentsList: ArrayList<CommentModel>) {
        commentsAdapter.setupComments(commentsList)
        commentsAdapter.notifyDataSetChanged()
    }

    override fun showCommentsList() {
        comments_recycler_view.visibility = View.VISIBLE
        comments_cpv.visibility = View.INVISIBLE
        comments_nothing_to_show_here.visibility = View.INVISIBLE
    }

    override fun showEmptyCommentsList() {
        comments_nothing_to_show_here.visibility = View.VISIBLE
    }

    override fun sendComment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        comments_recycler_view.visibility = View.INVISIBLE
        comments_nothing_to_show_here.visibility = View.INVISIBLE
        comments_cpv.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        comments_cpv.visibility = View.INVISIBLE
    }

    // OnItemClickListener implementation
    override fun onPhotoClick(photoURLs: ArrayList<String?>, position: Int) {
        router.navigateTo(Screens.ViewPhotoScreen(photoURLs = photoURLs,position =  position))
    }

    override fun onVideoClick(videoID: String, videoOwnerID: String) {
        router.navigateTo(Screens.VideoPlayerScreen(videoID = videoID, videoOwnerID = videoOwnerID))
    }

    override fun onAddLikeClick(ownerId: Long, itemId: Long, position: Int) {
        //presenter.addLike(ownerId = ownerId, itemId = itemId, position = position, accessToken = arguments!!.getString("access_token")!!)
    }

    override fun onDeleteLikeClick(ownerId: Long, itemId: Long, position: Int) {
        //presenter.deleteLike(ownerId = ownerId, itemId = itemId, position = position, accessToken = arguments!!.getString("access_token")!!)
    }

    override fun onCommentsButtonClick(model: FeedItemModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}