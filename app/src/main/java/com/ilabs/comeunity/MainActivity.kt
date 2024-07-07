package com.ilabs.comeunity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var daytime: TextView? = null
    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var hiusername: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hook()
        mAuth = FirebaseAuth.getInstance()
        daytime = findViewById(R.id.daytime)

        setGreeting()

        val ref: DatabaseReference = database.getReference().child("Users").child(auth.uid.toString())
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                hiusername.text = "Hi " + snapshot.child("name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onStart() {
        super.onStart()
        val rootLayout: RelativeLayout = findViewById(R.id.root_layout)
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in)
        rootLayout.startAnimation(animation)
    }

    private fun setGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val greeting = when {
            hour in 6..11 -> "Good morning!"
            hour in 12..17 -> "Good afternoon!"
            hour in 18..21 -> "Good evening!"
            else -> "Good night!"
        }
        daytime!!.text = greeting
    }



    private fun hook() {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        hiusername = findViewById(R.id.username)
    }
}
