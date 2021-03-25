package vinova.kane.touristguide.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import vinova.kane.touristguide.R
import vinova.kane.touristguide.ui.home.MainActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_auth)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            Timber.d("Login")
        }else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show()
        }
    }
}