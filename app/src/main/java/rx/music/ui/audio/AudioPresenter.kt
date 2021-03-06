package rx.music.ui.audio

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmList
import me.extensions.isNotNull
import me.extensions.toStr
import rx.music.business.audio.AudioInteractor
import rx.music.dagger.Dagger
import rx.music.data.preferences.PreferencesRepo
import rx.music.net.models.base.Response
import rx.music.net.models.vk.Audio
import rx.music.net.models.vk.Audios
import rx.music.net.models.vk.MusicPage
import javax.inject.Inject


/** Created by Maksim Sukhotski on 4/8/2017. */
@InjectViewState
class AudioPresenter(val realm: Realm) : MvpPresenter<AudioView>() {
    @Inject lateinit var audioInteractor: AudioInteractor
    @Inject lateinit var preferencesRepo: PreferencesRepo

    private var isRecyclerLoaded = false

    init {
        Dagger.instance.userComponent?.inject(this@AudioPresenter)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getMusicPage()
    }

    fun getMusicPage(audioCount: Int? = null, audioOffset: Int? = null):
            Disposable = with(preferencesRepo.credentials) {
        audioInteractor
                .getMusicPage(audioCount = audioCount, audioOffset = audioOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ setRecycler() })
//                .subscribe()
                .subscribe({ response, error -> handleMusicPageResponse(response, error) })
    }

    private fun handleMusicPageResponse(musicPage: Response<MusicPage>?, error: Throwable?) {
        if (musicPage?.response?.owner != null && !isRecyclerLoaded) setRecycler()
        if (musicPage?.executeErrors != null) viewState.showSnackbar(musicPage.executeErrors.toStr())
        if (error != null) viewState.showSnackbar(error.localizedMessage)
    }

    fun setRecycler(): Disposable = with(preferencesRepo.credentials) {
        Single
                .fromCallable {
                    val audios = realm.where(Audios::class.java)
                            .equalTo(Audios::userId.name, userId)
                            .findFirst()?.items
                    if (audios != null) return@fromCallable audios
                    else return@fromCallable RealmList<Audio>()
                }
                .filter({
                    it?.isValid ?: false
                            && it?.isManaged ?: false
                            && !isRecyclerLoaded
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.showRecycler(AudioAdapter(it, onClick =
                    { audio, pos -> handleAudio(realm.copyFromRealm(audio), pos) }))
                    isRecyclerLoaded = true
                })
    }


    fun handleAudio(audio: Audio, pos: Int): Disposable = with(viewState) {
        audioInteractor
                .handleAudio(audio)
                .filter { audio.isNotNull }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showPlayer(audio); showSelectedPos(pos) }
                .subscribe({ audio -> showPlayer(audio) })
    }
}

