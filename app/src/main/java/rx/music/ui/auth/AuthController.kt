package rx.music.ui.auth

import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.controller_auth.view.*
import kotlinx.android.synthetic.main.part_auth.view.*
import kotlinx.android.synthetic.main.part_captcha.view.*
import kotlinx.android.synthetic.main.part_containers.*
import kotlinx.android.synthetic.main.part_validation.view.*
import me.ext.hideKeyboard
import me.ext.onClick
import rx.music.App
import rx.music.R
import rx.music.data.net.models.Captcha
import rx.music.data.net.models.Validation
import rx.music.ui.audio.AudioController
import rx.music.ui.base.MainActivity
import rx.music.ui.base.MoxyController


class AuthController : MoxyController(), AuthView {

    @InjectPresenter
    lateinit var authPresenter: AuthPresenter

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_auth, container, false)
    }

    override fun onViewBound(view: View) {
        view.loginButton.onClick {
            authPresenter.login(view.usernameEditText.text.toString(),
                    view.passwordEditText.text.toString())
        }
    }

    override fun showSnackbar(text: String) {
        with(view!!) {
            Snackbar.make(loginLayout, text, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun showLogin(string: Any) {
        with(view!!) {
            flipLayout.showView(flipLayout.loginView!!)
            showSnackbar(string as String)
        }
    }

    override fun showCaptcha(captcha: Captcha) {
        with(view!!) {
            flipLayout.showView(flipLayout.captchaView!!)
            Glide.with(activity!!)
                    .load(captcha.captcha_img)
                    .error(R.drawable.oh)
                    .into(captchaImageView)
            loginButton.onClick {
                authPresenter.login(usernameEditText.text.toString(),
                        passwordEditText.text.toString(),
                        captcha.captcha_sid,
                        captchaEditText.text.toString())
                captchaEditText.text.clear()
            }
        }
    }

    override fun showValidation(validation: Validation) {
        with(view!!) {
            flipLayout.showView(flipLayout.validationView!!)
            validationTextView.text = context.getString(R.string.code_sent, validation.phone_mask)
            loginButton.onClick {
                authPresenter.login(usernameEditText.text.toString(),
                        passwordEditText.text.toString(),
                        code = validationEditText.text.toString())
            }
        }
    }

    override fun showAudioController() {
        showNavigation()
        App.instance.authComponent = null
        router.setRoot(RouterTransaction.with(AudioController())
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler()))
    }

    fun showNavigation() {
        view?.hideKeyboard()
        (activity as MainActivity).bottomNavigation.animate().translationY(0f)
                .withStartAction { (activity as MainActivity).bottomNavigation.visibility = View.VISIBLE }
                .withEndAction { (activity as MainActivity).slidingLayout.panelHeight = resources!!.getDimension(R.dimen.navigation).toInt() }
                .startDelay = 300
    }

    override fun hideNavigation() {
        (activity as MainActivity).slidingLayout.panelHeight = 0
        (activity as MainActivity).bottomNavigation.animate()
                .translationY(resources!!.getDimension(R.dimen.navigation))
                .withEndAction { (activity as MainActivity).bottomNavigation.visibility = View.GONE }
    }
}