package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityLogin: AppCompatActivity() {
    private val alb: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val signInCoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)
        setSupportActionBar(alb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Login"

        alb.signUpBt.setOnClickListener {
            startActivity(Intent(this, ActivityRegister::class.java))
        }

        alb.signInBt.setOnClickListener {
            if (alb.emailLoginEt.text.isEmpty()) {
                Toast.makeText(this, "The email field cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (alb.passwordLoginEt.text.isEmpty()) {
                Toast.makeText(this, "The password field cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signInCoroutineScope.launch {
                Firebase.auth.signInWithEmailAndPassword(
                    alb.emailLoginEt.text.toString().trim(),
                    alb.passwordLoginEt.text.toString().trim()
                ).addOnFailureListener {
                    Toast.makeText(
                        this@ActivityLogin,
                        "Failed to log in. Cause ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnSuccessListener {
                    openMainActivity()
                }
            }
        }

        alb.resetPasswordBt.setOnClickListener {
            signInCoroutineScope.launch {
                val email = alb.emailLoginEt.text.toString().trim()
                if (email.isNotEmpty()) {
                    Firebase.auth.sendPasswordResetEmail(
                        email
                    ).addOnSuccessListener {
                        Toast.makeText(
                            this@ActivityLogin,
                            "Password reset link sent to ${email}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@ActivityLogin,
                            "Failed to send reset link. Cause: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ActivityLogin,
                        "Please enter your email to reset your passwordtiago.",
                        Toast.LENGTH_SHORT)
                    .show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            openMainActivity()
        }
    }

    private fun openMainActivity() {
        startActivity(Intent(this@ActivityLogin, MainActivity::class.java))
        finish()
    }
}