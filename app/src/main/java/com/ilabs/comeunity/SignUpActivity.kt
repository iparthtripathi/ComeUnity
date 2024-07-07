package com.ilabs.comeunity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ilabs.comeunity.Models.User

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val passwordToggle = findViewById<ImageButton>(R.id.passwordToggle)
        val flatNumberEditText = findViewById<EditText>(R.id.flatNumberEditText)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val signInTextView = findViewById<TextView>(R.id.signInTextView)


        val radioButtons = listOf(
            findViewById<RadioButton>(R.id.blockA),
            findViewById<RadioButton>(R.id.blockB),
            findViewById<RadioButton>(R.id.blockC),
            findViewById<RadioButton>(R.id.krea),
            findViewById<RadioButton>(R.id.iiit),
            findViewById<RadioButton>(R.id.industry)
        )

        radioButtons.forEach { radioButton ->
            radioButton.setOnClickListener {
                radioButtons.forEach { it.isChecked = false }
                radioButton.isChecked = true
            }
        }


        passwordToggle.setOnClickListener {
            passwordVisible = !passwordVisible
            if (passwordVisible) {
                passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordToggle.setImageResource(R.drawable.eye)
            } else {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordToggle.setImageResource(R.drawable.closed_eye)
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val flatNumber = flatNumberEditText.text.toString()
            val selectedResidence = radioButtons.find { it.isChecked }?.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && selectedResidence.isNotEmpty() && flatNumber.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.let {
                                val uid = it.uid
                                val user = User(uid, email, name, selectedResidence, flatNumber)
                                FirebaseDatabase.getInstance().getReference("Users").child(uid).setValue(user)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            startActivity(Intent(this, MainActivity::class.java))
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }

        signInTextView.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}


