package com.arsoft.newsfeed.ui.video

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arsoft.newsfeed.helpers.Prefs
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.mvp.video.VideoPLayerView
import com.arsoft.newsfeed.mvp.video.VideoPlayerPresenter
import kotlinx.android.synthetic.main.fragment_video_player.*

class VideoPlayerFragment: MvpAppCompatFragment(), VideoPLayerView {


    @InjectPresenter
    lateinit var presenter: VideoPlayerPresenter

    companion object{
        fun getNewInstance(ownerID: Long, videoID: String) = VideoPlayerFragment().apply {
            arguments = Bundle().apply {
                putLong("owner_id", ownerID)
                putString("video_id", videoID)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = MediaController(context)
        controller.setMediaPlayer(video_view)
        video_view.setMediaController(controller)

        if (savedInstanceState == null) {
            presenter.loadVideo(
                ownerID = arguments!!.getLong("owner_id"),
                videoID = arguments!!.getString("video_id")!!,
                accessToken = Prefs(context!!).accessToken)
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        video_view.stopPlayback()
    }

    //MARK - View implementation
    override fun initializePlayer(videoURL: String) {
        video_view.setVideoURI(Uri.parse(videoURL))

    }

    override fun playVideo() {
        video_view.apply {
            setOnPreparedListener {
                video_view.seekTo(1)
                video_view.start()
            }
            setOnCompletionListener {
                video_view.seekTo(0)
            }
        }
    }

    override fun showLoading() {
        video_view.visibility = View.INVISIBLE
        video_cpv.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        video_view.visibility = View.VISIBLE
        video_cpv.visibility = View.INVISIBLE
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}