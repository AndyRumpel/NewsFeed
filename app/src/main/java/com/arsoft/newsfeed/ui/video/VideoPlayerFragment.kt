package com.arsoft.newsfeed.ui.video

import android.content.Intent
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
        fun getNewInstance(videoID: String, videoOwnerID: String) = VideoPlayerFragment().apply {
            arguments = Bundle().apply {
                putString("video_id", videoID)
                putString("video_owner_id", videoOwnerID)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = MediaController(context)
        controller.setMediaPlayer(video_view)
        video_view.setMediaController(controller)

        presenter.loadPLayer(
            ownerID = arguments!!.getString("video_owner_id")!!,
            videoID = arguments!!.getString("video_id")!!,
            accessToken = Prefs(context!!).accessToken,
            platform = arguments!!.getString("video_platform"))
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        video_view.stopPlayback()
    }

    //View implementation

    override fun initializePlayer(videoURL: String, platform: String?) {
        if (platform == "youtube") {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoURL))
            startActivity(intent)
        } else {
            video_view.setVideoURI(Uri.parse(videoURL))
        }
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

    override fun chooseQuality(qualities: ArrayList<String>){
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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