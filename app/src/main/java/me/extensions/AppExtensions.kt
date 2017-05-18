package me.extensions

import Items
import Response
import User
import android.app.Activity
import com.bluelinelabs.conductor.Router
import rx.music.net.models.audio.Audio
import rx.music.ui.audio.AudioController
import rx.music.ui.main.MainActivity

/** Created by Maksim Sukhotski on 5/15/2017. */


@Suppress("UNCHECKED_CAST")
fun <E> MutableList<E>.toAudioResponse(): Response<Items<MutableList<Audio>>> =
        Response(response = Items(this.size, this as MutableList<Audio>))

@Suppress("UNCHECKED_CAST")
fun <E> MutableList<E>.toUsersResponse(): Response<List<E>>? =
        Response(response = this)

fun String.toLongArray(): LongArray {
    val strings = this.split(",")
    val l = LongArray(strings.size)
    strings.forEachIndexed { index, s -> l[index] = s.toLong() }
    return l
}

fun Activity.toMain(): MainActivity = this as MainActivity

fun Response<List<User>>.toStringArray(): LongArray? {
    val l = LongArray(this.response?.size ?: 0)
    this.response?.forEachIndexed { index, user -> l[index] = user.id }
    return l
}

val Router.audioController: AudioController
    get() = this.getControllerWithTag("audio") as AudioController