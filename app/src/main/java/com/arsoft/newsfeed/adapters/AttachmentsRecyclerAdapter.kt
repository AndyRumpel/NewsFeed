package com.arsoft.newsfeed.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.data.models.IAttachment
import com.arsoft.newsfeed.data.models.PhotoModel
import com.arsoft.newsfeed.data.models.VideoModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class AttachmentsRecyclerAdapter(private val onAttachmentClickListener: OnAttachmentClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val attachments = ArrayList<IAttachment>()

    fun setupAttachments(attachments: ArrayList<IAttachment>) {
        this.attachments.clear()
        this.attachments.addAll(attachments)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            AttachmentTypes.ATTACHMENT_TYPE_PHOTO.ordinal -> AttachmentPhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachment_photo, parent, false), onAttachmentClickListener)
            else ->                                          AttachmentVideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachment_video, parent, false))

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
                attachmentVideoViewHolder.bind(attachments[position] as VideoModel)
            }
            else -> {}
        }
    }

    class AttachmentPhotoViewHolder(itemView: View, val onAttachmentClickListener: OnAttachmentClickListener): RecyclerView.ViewHolder(itemView) {
        private val photoImageView = itemView.findViewById<ImageView>(R.id.attachment_photo_image_view)

        fun bind(model: PhotoModel, attachments: ArrayList<IAttachment>, position: Int) {


            Glide.with(itemView.context)
                .load(model.photoURL)
                .apply(bitmapTransform(CropSquareTransformation()))
                .into(photoImageView)

            photoImageView.setOnClickListener {
                val photos = ArrayList<String?>()
                for (attachment in attachments) {
                    if (attachment is PhotoModel) {
                        photos.add(attachment.photoURL)
                    }
                }
                onAttachmentClickListener.onPhotoClick(photos, position)
            }
        }
    }
    class AttachmentVideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(model: VideoModel) {

        }
    }

    enum class AttachmentTypes {
        ATTACHMENT_TYPE_PHOTO,
        ATTACHMENT_TYPE_VIDEO
    }

    interface OnAttachmentClickListener{
        fun onPhotoClick(photoURLs: ArrayList<String?> ,position: Int)
        fun onVideoClick(videoID: String, videoOwnerID: String)
    }

}