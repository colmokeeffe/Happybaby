package ie.wit.happybaby.views.register

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import ie.wit.happybaby.main.MainApp
import ie.wit.happybaby.models.ActivityFireStore
import ie.wit.happybaby.views.activitylist.ActivityListView

class RegisterPresenter (val view: RegisterView) {

    var app: MainApp = view.application as MainApp
    var fireStore: ActivityFireStore? = null
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var activityIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerActivityCallback()
        if (app.activities is ActivityFireStore) {
            fireStore = app.activities as ActivityFireStore
        }
    }

    fun doRegisterUser(email: String, password: String) {
        view.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                fireStore!!.fetchActivities {
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

    private fun registerActivityCallback() {
        activityIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}