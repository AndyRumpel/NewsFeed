package com.arsoft.newsfeed.onClick


interface OnAttachmentClickListener {
    fun onPhotoClick(photoURLs: ArrayList<String?> ,position: Int)
    fun onVkVideoClick(ownerID: Long, videoID: String)
    fun onExternalVideoClick(ownerID: Long, videoID: String)
}