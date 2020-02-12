package com.arsoft.newsfeed.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Animatable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.data.models.DocModel
import com.arsoft.newsfeed.data.models.IAttachment
import com.arsoft.newsfeed.data.models.PhotoModel
import com.arsoft.newsfeed.data.models.VideoModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.felipecsl.gifimageview.library.GifImageView
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class AttachmentsRecyclerAdapter(private val onNewsFeedItemClickListener: NewsFeedRecyclerAdapter.NewsFeedViewHolder.OnNewsFeedItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val attachments = ArrayList<IAttachment>()

    fun setupAttachments(attachments: ArrayList<IAttachment>) {
        this.attachments.clear()
        this.attachments.addAll(attachments)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            AttachmentTypes.ATTACHMENT_TYPE_PHOTO.ordinal -> AttachmentPhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachment_photo, parent, false), onNewsFeedItemClickListener)
            AttachmentTypes.ATTACHMENT_TYPE_VIDEO.ordinal ->   AttachmentVideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachment_video, parent, false), onNewsFeedItemClickListener)
            else ->                                          AttachmentDocViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachment_doc, parent, false), onNewsFeedItemClickListener)
        }
    }

    override fun getItemCount(): Int {
        return attachments.count()
    }

    override fun getItemViewType(position: Int): Int {
        return when(attachments[position]) {
            is PhotoModel -> {
                AttachmentTypes.ATTACHMENT_TYPE_PHOTO.ordinal
            }
            is VideoModel -> {
                AttachmentTypes.ATTACHMENT_TYPE_VIDEO.ordinal
            }
            is DocModel -> {
                AttachmentTypes.ATTACHMENT_TYPE_DOC.ordinal
            }
            else -> -1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            AttachmentTypes.ATTACHMENT_TYPE_PHOTO.ordinal -> {
                val attachmentPhotoViewHolder = holder as AttachmentPhotoViewHolder
                attachmentPhotoViewHolder.bind(model = attachments[position] as PhotoModel, attachments = attachments, position = position)
            }
            AttachmentTypes.ATTACHMENT_TYPE_VIDEO.ordinal -> {
                val attachmentVideoViewHolder = holder as AttachmentVideoViewHolder
                attachmentVideoViewHolder.bind(attachments[position] as VideoModel, attachments = attachments)
            }
            AttachmentTypes.ATTACHMENT_TYPE_DOC.ordinal -> {
                val attachmentDocViewHolder = holder as AttachmentDocViewHolder
                attachmentDocViewHolder.bind(attachments[position] as DocModel)
            }
            else -> {}
        }
    }

    class AttachmentDocViewHolder(itemView: View, private val onNewsFeedItemClickListener: NewsFeedRecyclerAdapter.NewsFeedViewHolder.OnNewsFeedItemClickListener): RecyclerView.ViewHolder(itemView) {
        private val docImageView = itemView.findViewById<GifImageView>(R.id.attachment_doc_image_view)

        fun bind(model: DocModel){

            Glide.with(itemView.context)
                .load(model.url)
                .into(docImageView)

            var isStopped = true

            docImageView.setOnClickListener {
                if (isStopped) {
                    docImageView.startAnimation()
                } else {
                    docImageView.stopAnimation()
                }
            }
        }
    }

    class AttachmentPhotoViewHolder(itemView: View, private val onNewsFeedItemClickListener: NewsFeedRecyclerAdapter.NewsFeedViewHolder.OnNewsFeedItemClickListener): RecyclerView.ViewHolder(itemView) {
        private val photoImageView = itemView.findViewById<ImageView>(R.id.attachment_photo_image_view)

        fun bind(model: PhotoModel, attachments: ArrayList<IAttachment>, position: Int) {

            val multiTransformation =  MultiTransformation<Bitmap>(
                CropSquareTransformation(),
                RoundedCornersTransformation(40, 0, RoundedCornersTransformation.CornerType.ALL)
            )


            if (attachments.count() > 1) {
                Glide.with(itemView.context)
                    .load(model.url)
                    .apply(bitmapTransform(multiTransformation))
                    .into(photoImageView)

            } else {
                Glide.with(itemView.context)
                    .load(model.url)
                    .into(photoImageView)
            }



            photoImageView.setOnClickListener {
                val photos = ArrayList<String?>()
                for (attachment in attachments) {
                    if (attachment is PhotoModel) {
                        photos.add(attachment.url)
                    }
                }
                onNewsFeedItemClickListener.onPhotoClick(photos, position)
            }
        }
    }
    class AttachmentVideoViewHolder(itemView: View, private val onNewsFeedItemClickListener: NewsFeedRecyclerAdapter.NewsFeedViewHolder.OnNewsFeedItemClickListener): RecyclerView.ViewHolder(itemView) {

        private val previewImageView = itemView.findViewById<ImageView>(R.id.video_preview_image)
        private val videoDurationTextView = itemView.findViewById<TextView>(R.id.video_duration_text_view)

        fun bind(model: VideoModel, attachments: ArrayList<IAttachment>) {

            val multiTransformation =  MultiTransformation<Bitmap>(
                CropSquareTransformation(),
                RoundedCornersTransformation(40, 0, RoundedCornersTransformation.CornerType.ALL)
            )
            if (attachments.count() > 1) {
                Glide.with(itemView.context)
                    .load(model.videoPreviewImage)
                    .apply(bitmapTransform(multiTransformation))
                    .into(previewImageView)
            } else {
                Glide.with(itemView.context)
                    .load(model.videoPreviewImage)
                    .into(previewImageView)
            }

            previewImageView.setOnClickListener {
                onNewsFeedItemClickListener.onVideoClick(videoID = model.videoID, videoOwnerID = model.videoOwnerID)
            }

            val videoDurationInSeconds = model.videoDuration % 60
            val videoDurationInMinutes = model.videoDuration.div(60)

            videoDurationTextView.text =
                String.format("%02d", videoDurationInMinutes) + ":" + String.format(
                    "%02d",
                    videoDurationInSeconds
                )
        }
    }

    enum class AttachmentTypes {
        ATTACHMENT_TYPE_PHOTO,
        ATTACHMENT_TYPE_VIDEO,
        ATTACHMENT_TYPE_DOC
    }


}