package suhockii.rxmusic.ui.audio

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import suhockii.rxmusic.App
import suhockii.rxmusic.business.audio.AudioInteractor
import suhockii.rxmusic.business.preferences.PreferencesInteractor
import suhockii.rxmusic.data.repositories.audio.models.Audio
import javax.inject.Inject

/** Created by Maksim Sukhotski on 4/8/2017. */
@InjectViewState
class AudioPresenter : MvpPresenter<AudioView>() {

    @Inject lateinit var preferencesInteractor: PreferencesInteractor
    @Inject lateinit var audioInteractor: AudioInteractor

    init {
        App.instance.userComponent?.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        validateCredentials()
    }

    fun getAudio(ownerId: String = preferencesInteractor.getCredentials().user_id,
                 count: String = "30",
                 offset: String = "0") {
        audioInteractor.getAudio(ownerId, count, offset)
                .subscribe({
                    viewState.showAudio(it.response)
                }, { })
    }

    fun validateCredentials() {
        if (preferencesInteractor.isEmpty()) {
            viewState.showAuthController()
        } else {
            getAudio()
        }
    }

    fun playAudio(audio: Audio) {
//        viewState.showPlayer()
    }

}