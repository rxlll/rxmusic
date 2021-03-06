package rx.music.dagger

import android.app.Application
import rx.music.App.Companion.appComponent
import rx.music.dagger.components.AuthComponent
import rx.music.dagger.components.DaggerAppComponent
import rx.music.dagger.components.UserComponent
import rx.music.dagger.modules.AppModule
import rx.music.dagger.modules.AuthModule
import rx.music.dagger.modules.UserModule

/** Created by Maksim Sukhotski on 5/8/2017. */
class Dagger(val app: Application) {
    companion object {
        lateinit var instance: Dagger private set
    }

    init {
        instance = this
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .build()
    }

    var userComponent: UserComponent?
        get() = appComponent.plus(UserModule())
        set(value) {}

    var authComponent: AuthComponent?
        get() = appComponent.plus(AuthModule())
        set(value) {}
}