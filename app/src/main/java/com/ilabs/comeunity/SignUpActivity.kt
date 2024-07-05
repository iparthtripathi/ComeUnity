package com.ilabs.comeunity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
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
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val residenceRadioGroup = findViewById<RadioGroup>(R.id.residenceRadioGroup)
        val flatNumberEditText = findViewById<EditText>(R.id.flatNumberEditText)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()
            val selectedResidenceId = residenceRadioGroup.checkedRadioButtonId
            val residence = findViewById<RadioButton>(selectedResidenceId).text.toString()
            val flatNumber = flatNumberEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && residence.isNotEmpty() && flatNumber.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = User(auth.currentUser!!.uid, email, name, residence, flatNumber)
                            database.child("users").child(auth.currentUser!!.uid).setValue(user)
                                .addOnCompleteListener {
                                    val intent = Intent(this, SignInActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        } else {
                            // Handle error
                        }
                    }
            } else {
                // Handle empty fields
            }
        }
    }
}

