package com.arsoft.newsfeed.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.data.models.*
import com.arsoft.newsfeed.onClick.OnAttachmentClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.felipecsl.gifimageview.library.GifImageView
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class AttachmentsRecyclerAdapter(private val onAttachmentClickListener: OnAttachmentClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val attachments = ArrayList<IAttachment>()

    fun setupAttachments(attachments: ArrayList<IAttachment>) {
        this.attachments.clear()
        this.attachments.addAll(attachments)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            AttachmentTypes.ATTACHMENT_TYPE_PHOTO.ordinal -> AttachmentPhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachment_photo, parent, false), onAttachmentClickListener)
            AttachmentTypes.ATTACHMENT_TYPE_VIDEO.ordinal -> AttachmentVideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachment_video, parent, false), onAttachmentClickListener)
            AttachmentTypes.ATTACHMENT_TYPE_DOC.ordinal -> AttachmentDocViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachment_doc, parent, false), onAttachmentClickListener)
            else -> AttachmentStickerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.attachment_sticker, parent, false))

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
            is StickerModel -> {
                AttachmentTypes.ATTACHMENT_TYPE_STICKER.ordinal
            }
            else -> -1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            AttachmentTypes.ATTACHMENT_TYPE_PHOTO.ordinal -> {
                val attachmentPhotoViewHolder: AttachmentPhotoViewHolder = holder as AttachmentPhotoViewHolder
                attachmentPhotoViewHolder.bind(model = attachments[position] as PhotoModel, attachments = attachments, position = position)
            }
            AttachmentTypes.ATTACHMENT_TYPE_VIDEO.ordinal -> {
                val attachmentVideoViewHolder: AttachmentVideoViewHolder = holder as AttachmentVideoViewHolder
                attachmentVideoViewHolder.bind(attachments[position] as VideoModel, attachments = attachments)
            }
            AttachmentTypes.ATTACHMENT_TYPE_DOC.ordinal -> {
                val attachmentDocViewHolder: AttachmentDocViewHolder = holder as AttachmentDocViewHolder
                attachmentDocViewHolder.bind(attachments[position] as DocModel)
            }
            AttachmentTypes.ATTACHMENT_TYPE_STICKER.ordinal -> {
                val attachmentStickerViewHolder:AttachmentStickerViewHolder = holder as AttachmentStickerViewHolder
                attachmentStickerViewHolder.bind(attachments[position] as StickerModel)
            }
            else -> {}
        }
    }

    class AttachmentStickerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val stickerImageView = itemView.findViewById<ImageView>(R.id.attachment_sticker_imageview)

        fun bind(model: StickerModel) {

            Log.e("STICKER", model.imageUrl)
            Glide.with(itemView.context)
                .load(model.imageUrl)
                .into(stickerImageView)
        }

    }

    class AttachmentDocViewHolder(itemView: View, private val onAttachmentClickListener: OnAttachmentClickListener): RecyclerView.ViewHolder(itemView) {
        private val docImageView = itemView.findViewById<GifImageView>(R.id.attachment_doc_image_view)

        fun bind(model: DocModel){

            Glide.with(itemView.context)
                .asGif()
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

    class AttachmentPhotoViewHolder(itemView: View, private val onAttachmentClickListener: OnAttachmentClickListener): RecyclerView.ViewHolder(itemView) {
        private val photoImageView = itemView.findViewById<ImageView>(R.id.attachment_photo_image_view)

        fun bind(model: PhotoModel, attachments: ArrayList<IAttachment>, position: Int) {

            val multiTransformation =  MultiTransformation<Bitmap>(
                CropSquareTransformation(),
                RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
            )


            if (attachments.count() > 1) {
                Glide.with(itemView.context)
                    .load(model.url)
                    .apply(bitmapTransform(multiTransformation))
                    .placeholder(R.drawable.image_placeholder)
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
                onAttachmentClickListener.onPhotoClick(photos, position)
            }
        }
    }
    class AttachmentVideoViewHolder(itemView: View, private val onAttachmentClickListener: OnAttachmentClickListener): RecyclerView.ViewHolder(itemView) {

        private val previewImageView = itemView.findViewById<ImageView>(R.id.video_preview_image_view)
        private val videoDurationTextView = itemView.findViewById<TextView>(R.id.video_duration_text_view)
        private val platformTextView = itemView.findViewById<TextView>(R.id.video_platform_textview)
        private val titleTextView = itemView.findViewById<TextView>(R.id.video_title_text_view)

        @SuppressLint("SetTextI18n")
        fun bind(model: VideoModel, attachments: ArrayList<IAttachment>) {

            val multiTransformation =  MultiTransformation<Bitmap>(
                CropSquareTransformation(),
                RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)
            )
            if (attachments.count() > 1) {
                Glide.with(itemView.context)
                    .load(model.previewImage)
                    .apply(bitmapTransform(multiTransformation))
                    .placeholder(R.drawable.image_placeholder)
                    .into(previewImageView)
            } else {
                Glide.with(itemView.context)
                    .load(model.previewImage)
                    .into(previewImageView)
            }

            previewImageView.setOnClickListener {
                if(model.platform == "YouTube") {
                    onAttachmentClickListener.onExternalVideoClick(ownerID = model.ownerID, videoID = model.videoID)
                } else {
                    onAttachmentClickListener.onVkVideoClick(ownerID = model.ownerID, videoID = model.videoID)
                }
            }

            platformTextView.text = model.platform
            titleTextView.text = model.title

            val videoDurationInSeconds = model.duration % 60
            val videoDurationInMinutes = model.duration.div(60)

            videoDurationTextView.text =
                String.format("%02d", videoDurationInMinutes) + ":" + String.format("%02d", videoDurationInSeconds)
        }
    }

    enum class AttachmentTypes {
        ATTACHMENT_TYPE_PHOTO,
        ATTACHMENT_TYPE_VIDEO,
        ATTACHMENT_TYPE_DOC,
        ATTACHMENT_TYPE_STICKER
    }


}