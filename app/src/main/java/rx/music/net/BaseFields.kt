package rx.music.net

import java.util.*

/** Created by Maksim Sukhotski on 4/9/2017. */
class BaseFields {
    companion object {
        const val VK_API = "https://api.vk.com/method/"
        const val VK_VALIDATION_API = "https://oauth.vk.com/token/"
        const val GCM_API = "https://android.clients.google.com/c2dm/register3"
        const val GOOGLE_API = "https://www.googleapis.com/customsearch/v1"

        const val APP_LOG = "applog"
        const val V = 5.65
        const val SCOPE = "nohttps,all"
        const val TWO_FA_SUPPORTED = 1
        const val GRANT_TYPE = "password"
        const val LIBVERIFY_SUPPORT = 1
        const val HTTPS = 1
        const val SEARCH_TYPE = "image"
        const val IMG_SIZE = "large"
        const val MIN_USER_INFO = "photo_max_orig,music,can_see_audio"
        const val PAGINATION_COUNT = 100
        const val MAX_PAGINATION_COUNT = 500

        var fullyLoaded = false
        var albumPreviewSize = 0
        var albumSize = 0
        val lang = Locale.getDefault().language!!
    }
}