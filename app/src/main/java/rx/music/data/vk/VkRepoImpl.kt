package rx.music.data.vk

import io.reactivex.Observable
import io.reactivex.Single
import rx.music.dagger.Dagger
import rx.music.data.preferences.PreferencesRepo
import rx.music.net.apis.VkApi
import rx.music.net.models.auth.Credentials
import rx.music.net.models.base.Items
import rx.music.net.models.base.Response
import rx.music.net.models.vk.Audio
import rx.music.net.models.vk.MusicPage
import rx.music.net.models.vk.User
import javax.inject.Inject

/** Created by Maksim Sukhotski on 4/9/2017. */
class VkRepoImpl : VkRepo {
    @Inject lateinit var preferencesRepo: PreferencesRepo
    @Inject lateinit var vkApi: VkApi

    init {
        Dagger.instance.userComponent?.inject(this)
    }

    override fun getAudio(ownerId: Long?, count: Int,
                          offset: Int?): Observable<Response<Items<MutableList<Audio>>>> =
            vkApi.getAudio(ownerId ?: preferencesRepo.credentials.userId, count, offset)

    override fun getMusicPage(ownerId: Long?, audioCount: Int?,
                              audioOffset: Int?): Single<Response<MusicPage>> =
            vkApi.getMusicPage(ownerId = ownerId ?: preferencesRepo.credentials.userId,
                    audioCount = audioCount, audioOffset = audioOffset)

    override fun getUsers(userIds: String?, fields: String?): Single<Response<List<User>>> =
            vkApi.getUsers(userIds ?: preferencesRepo.credentials.userId.toString(), fields)

    override fun refreshToken(receipt: String): Single<Credentials> =
            vkApi.refreshToken(receipt).doOnSuccess { preferencesRepo.credentials = it }
}