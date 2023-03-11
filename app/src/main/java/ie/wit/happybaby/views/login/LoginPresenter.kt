package ie.wit.happybaby.views.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import ie.wit.happybaby.views.register.RegisterView
import ie.wit.happybaby.main.MainApp
import ie.wit.happybaby.models.ActivityFireStore
import ie.wit.happybaby.views.activitylist.ActivityListView

class LoginPresenter(private val view: LoginView) {

    var app: MainApp = view.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: ActivityFireStore? = null
    private lateinit var activityIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var registerIntentLauncher : ActivityResultLauncher<Intent>

    init{
        registerActivityCallback()
        registerRegisterCallback()
        if (app.activities is ActivityFireStore) {
            fireStore = app.activities as ActivityFireStore
        }
    }

    fun doLogin(username: String, password: String) {
        view.showProgress()
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchActivities {
                        view.hideProgress()
                        val launcherIntent = Intent(view, ActivityListView::class.java)
                        activityIntentLauncher.launch(launcherIntent)
                    }
            } else {
                    view.hideProgress()
                    val launcherIntent = Intent(view, ActivityListView::class.java)
                    activityIntentLauncher.launch(launcherIntent)
                }
            } else {
                view.hideProgress()
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }
    }

    fun doRegister() {
        val launcherIntent = Intent(view, RegisterView::class.java)
        registerIntentLauncher.launch(launcherIntent)
    }

    private fun registerActivityCallback() {
        activityIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
    private fun registerRegisterCallback() {
        registerIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }

}