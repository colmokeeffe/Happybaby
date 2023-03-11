package ie.wit.happybaby.views.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import ie.wit.happybaby.R
import ie.wit.happybaby.databinding.ActivityRegisterBinding
import ie.wit.happybaby.main.MainApp

class RegisterView : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    lateinit var app : MainApp
    lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        binding.progressBar.visibility = View.GONE

        presenter = RegisterPresenter(this)

        binding.registerButton.setOnClickListener {
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()
            if (email == "" || password == "") {
                showSnackBar(R.string.warning_enter_credentials.toString())
            }
            else {
                presenter.doRegisterUser(email,password)
            }
        }
    }

    fun showSnackBar(message: CharSequence){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .show()
    }

    fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

}