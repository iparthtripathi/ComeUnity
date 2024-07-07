package com.ilabs.comeunity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // User is signed in, direct to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // User is not signed in, direct to SignUpActivity
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            finish()
        }, 2000) // 2 seconds delay
    }
}
