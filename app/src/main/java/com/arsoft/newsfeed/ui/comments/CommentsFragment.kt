package com.arsoft.newsfeed.ui.comments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.adapters.AttachmentsRecyclerAdapter
import com.arsoft.newsfeed.adapters.CommentsRecyclerAdapter
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.data.likes.request.LikesResponse
import com.arsoft.newsfeed.data.models.CommentModel
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.arsoft.newsfeed.helpers.MyDateTimeFormatHelper
import com.arsoft.newsfeed.helpers.Prefs
import com.arsoft.newsfeed.helpers.recycler.MultipleSpanGridLayoutManager
import com.arsoft.newsfeed.mvp.comments.CommentsPresenter
import com.arsoft.newsfeed.mvp.comments.CommentsView
import com.arsoft.newsfeed.navigation.screens.Screens
import com.arsoft.newsfeed.onClick.OnAttachmentClickListener
import com.arsoft.newsfeed.onClick.OnCommentsItemClickListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.feed_item.*
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.repost_item.*
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CommentsFragment: MvpAppCompatFragment(), CommentsView, OnCommentsItemClickListener, OnAttachmentClickListener{

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
        NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(fragment = this)
    }

    private lateinit var attachmentsRecyclerAdapter: AttachmentsRecyclerAdapter
    private lateinit var commentsAdapter: CommentsRecyclerAdapter
    private lateinit var layoutManager: MultipleSpanGridLayoutManager
    private lateinit var accessToken: String
    private lateinit var feedItemModel: FeedItemModel
    private var name = ""
    private var replyToComment = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedItemModel = arguments!!.getParcelable("model")!!
        setupPost(model = feedItemModel)

        accessToken = Prefs(context = context!!).accessToken

        presenter.loadComments(
            accessToken = accessToken,
            ownerId = feedItemModel.ownerId,
            postId = feedItemModel.postId
        )

        commentsAdapter = CommentsRecyclerAdapter(onCommentsItemClickListener = this, onAttachmentClickListener = this)
        commentsAdapter.setHasStableIds(true)
        comments_recycler_view.adapter = commentsAdapter
        comments_recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        comments_recycler_view.isNestedScrollingEnabled = false

        comment_send_imagebutton.setOnClickListener {
            if (comment_edittext.text.toString() != "") {
                if (replyToComment == 0L) {
                    presenter.createComment(
                        ownerId = feedItemModel.ownerId,
                        postId = feedItemModel.postId,
                        accessToken = accessToken,
                        message = comment_edittext.text.toString())
                } else {
                    presenter.replyComment(
                        ownerId = feedItemModel.ownerId,
                        postId = feedItemModel.postId,
                        accessToken = accessToken,
                        replyToComment = replyToComment,
                        message = "$name, ${comment_edittext.text}")
                    replyToComment = 0L
                }
                comment_edittext.text.clear()
            }
        }

        likes_button.setOnClickListener {
            if (feedItemModel.isFavorite) {
                presenter.deleteLike(type = "post", ownerId = feedItemModel.ownerId, itemId = feedItemModel.postId, accessToken = accessToken)
                feedItemModel.isFavorite = false
                likes_button.setImageResource(R.drawable.ic_favorite_border)
                likes_count_textview.setTextColor(Color.WHITE)
            } else {
                presenter.addLike(type = "post", ownerId = feedItemModel.ownerId, itemId = feedItemModel.postId, accessToken = accessToken)
                feedItemModel.isFavorite = true
                likes_button.setImageResource(R.drawable.ic_favorite)
                likes_count_textview.setTextColor(resources.getColor(R.color.colorAccent))
            }
        }

        if (!feedItemModel.canComment) {
            comments_nothing_to_show_here.text = resources.getText(R.string.comments_closed)
            comment_edittext.visibility = View.INVISIBLE
            comment_send_imagebutton.visibility = View.INVISIBLE
        } else {
            comments_nothing_to_show_here.text = resources.getText(R.string.comments_nothing_to_show)
            comment_edittext.visibility = View.VISIBLE
            comment_send_imagebutton.visibility = View.VISIBLE
        }
    }



    private fun setupPost(model: FeedItemModel) {
        Glide.with(context!!)
            .load(model.avatar)
            .into(post_avatar)
        post_source_name.text = model.sourceName
        val currentTime: Long = Calendar.getInstance().timeInMillis
        post_date_time.text = MyDateTimeFormatHelper.timeFormat(postDate = model.date * 1000, currentTime = currentTime)
        if (model.postText.isNullOrEmpty()) {
            post_expandable_text_view.visibility = View.INVISIBLE
        } else {
            setupPostText(model.postText)
        }
        with(model) {
            likes_count_textview.text =  if(likes.count > 0) likes.count.toString() else ""
            comments_count_textview.text = if(comments.count > 0) comments.count.toString() else ""
            reposts_count_textview.text = if(reposts.count > 0) reposts.count.toString() else ""
        }

        views_count_textview.text = model.views.count.toString()

        layoutManager = MultipleSpanGridLayoutManager(
            context = context!!,
            spanCount = 4,
            items = model.attachments
        )
        attachmentsRecyclerAdapter = AttachmentsRecyclerAdapter(this)
        attachments_recycler_view.layoutManager = layoutManager
        attachments_recycler_view.setHasFixedSize(true)
        attachments_recycler_view.isNestedScrollingEnabled = false
        attachments_recycler_view.adapter = attachmentsRecyclerAdapter
        attachmentsRecyclerAdapter.setupAttachments(attachments = model.attachments)
        attachmentsRecyclerAdapter.notifyDataSetChanged()

        if (!model.copyHistory.isNullOrEmpty()) {
            repost_item.visibility = View.VISIBLE
            with(model.copyHistory.first()){

                Glide.with(context!!)
                    .load(avatar)
                    .into(repost_avatar)

                repost_source_name.text = sourceName
                repost_date_time.text = MyDateTimeFormatHelper.timeFormat(postDate = date * 1000, currentTime = currentTime)
                if (postText.isNullOrEmpty()) {
                    post_expandable_text_view.visibility = View.INVISIBLE
                } else {
                    setupPostText(postText)
                }

                layoutManager = MultipleSpanGridLayoutManager(
                    context = context!!,
                    spanCount = 4,
                    items = attachments
                )
                attachments_recycler_view.isNestedScrollingEnabled = false
                attachments_recycler_view.layoutManager = layoutManager
                attachments_recycler_view.adapter = attachmentsRecyclerAdapter
                attachments_recycler_view.setHasFixedSize(true)
                attachmentsRecyclerAdapter.setupAttachments(attachments = attachments)
                attachmentsRecyclerAdapter.notifyDataSetChanged()
            }
        } else {
            repost_item.visibility = View.GONE
        }
    }

    private fun setupPostText(postText: String) {
        post_expandable_text_view.visibility = View.VISIBLE
        post_expandable_text_view.text = postText
        post_expandable_text_view.post {
            if (post_expandable_text_view.lineCount >= 10){
                show_more_text_view.visibility = View.VISIBLE
                show_more_text_view.textSize = 14F
            } else {
                show_more_text_view.visibility = View.INVISIBLE
                show_more_text_view.textSize = 0F
            }
        }

        show_more_text_view.setOnClickListener {
            if (post_expandable_text_view.isExpanded) {
                post_expandable_text_view.collapse()
                show_more_text_view.text = resources.getText(R.string.show_more_text)
            } else {
                post_expandable_text_view.expand()
                show_more_text_view.text = resources.getText(R.string.hide_text)
            }
        }
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

    override fun showLoading() {
        comments_recycler_view.visibility = View.INVISIBLE
        comments_nothing_to_show_here.visibility = View.INVISIBLE
        comments_cpv.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        comments_cpv.visibility = View.INVISIBLE
    }

    override fun openExternalPlayer(videoURL: String) {
        router.navigateTo(Screens.ExternalPlayerActivity(videoURL = videoURL))
    }

    override fun updatePostLikesCount(likes: LikesResponse) {
        with(likes_count_textview){
            if (likes.response.likes == 0) {
                text = ""
            } else {
                text = likes.response.likes.toString()
            }
        }
    }

    override fun updateCommentLikesCount(likes: LikesResponse, viewItemId: Long) {
        if (comments_recycler_view.findViewHolderForItemId(viewItemId) != null) {
            with(comments_recycler_view.findViewHolderForItemId(viewItemId).itemView.comment_likes_count) {
                if (likes.response.likes == 0) {
                    text = ""
                } else {
                    text = likes.response.likes.toString()
                }
            }
        } else {
            comments_recycler_view.forEach {
                if (it.thread_comments_recycler.findViewHolderForItemId(viewItemId) != null) {
                    with(it.thread_comments_recycler.findViewHolderForItemId(viewItemId).itemView.comment_likes_count) {
                        if (likes.response.likes == 0) {
                            text = ""
                        } else {
                            text = likes.response.likes.toString()
                        }
                    }
                }
            }
        }
    }

    override fun updateComments() {
        presenter.loadComments(accessToken, feedItemModel.ownerId, feedItemModel.postId)
    }


    //MARK - OnAttachmentClickListener implementation
    override fun onPhotoClick(photoURLs: ArrayList<String?>, position: Int) {
        router.navigateTo(Screens.ViewPhotoScreen(photoURLs = photoURLs,position =  position))
    }

    override fun onVkVideoClick(ownerID: Long, videoID: String) {
        router.navigateTo(Screens.VideoPlayerScreen(ownerID = ownerID, videoID = videoID))
    }

    override fun onExternalVideoClick(ownerID: Long, videoID: String) {
        presenter.loadExternalVideo(ownerID = ownerID, videoID = videoID, accessToken = accessToken)
    }

    //MARK - OnCommentsItemClickListener implementation
    override fun onAddLikeClick(type: String, ownerId: Long, itemId: Long, viewItemId: Long) {
        presenter.addLike(type = type, ownerId = ownerId, itemId = itemId, viewItemId = viewItemId, accessToken = accessToken)
    }

    override fun onDeleteLikeClick(type: String, ownerId: Long, itemId: Long, viewItemId: Long) {
        presenter.deleteLike(type = type, ownerId = ownerId, itemId = itemId, viewItemId = viewItemId, accessToken = accessToken)
    }

    override fun onReplyButtonClick(model: CommentModel, itemId: Long) {
        comment_edittext.requestFocus()
        comment_edittext.let {
            val inputmethodManager: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputmethodManager.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
        name = model.name
        replyToComment = model.itemId
    }
}