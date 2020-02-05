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

class AttachmentsRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mAttachments = ArrayList<IAttachment>()

    fun setupAttachments(attachments: ArrayList<IAttachment>) {
        mAttachments.clear()
        mAttachments.addAll(attachments)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            AttachmentTypes.ATTACHMENT_TYPE_PHOTO.ordinal -> AttachmentPhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachment_photo, parent, false))
            else ->                                          AttachmentVideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachment_video, parent, false))

        }
    }

    override fun getItemCount(): Int {
        return mAttachments.count()
    }

    override fun getItemViewType(position: Int): Int {
        return when(mAttachments[position]) {
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
                attachmentPhotoViewHolder.bind(mAttachments[position] as PhotoModel)
            }
            AttachmentTypes.ATTACHMENT_TYPE_VIDEO.ordinal -> {
                val attachmentVideoViewHolder = holder as AttachmentVideoViewHolder
                attachmentVideoViewHolder.bind(mAttachments[position] as VideoModel)
            }
            else -> {}
        }
    }

    class AttachmentPhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val photoImageView = itemView.findViewById<ImageView>(R.id.attachment_photo_image_view)

        fun bind(model: PhotoModel) {

            Glide.with(itemView.context)
                .load(model.photoURL)
                .into(photoImageView)
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

}