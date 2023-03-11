package ie.wit.happybaby.views.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import ie.wit.happybaby.R

class SplashScreenView : AppCompatActivity() {

    lateinit var presenter: SplashScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val backgroundImage: ImageView = findViewById(R.id.splashImage)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        //passing in the animation id side_slide to move image id imageView from left to right by 50%
        backgroundImage.startAnimation(slideAnimation)
        presenter = SplashScreenPresenter(this)
        presenter.doSplashScreen()
    }
}