package com.example.personaltasks.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityRegister: AppCompatActivity() {
    private val arb: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    val signUpCoroutine = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(arb.root)
        setSupportActionBar(arb.toolbarIn.toolbar)
        supportActionBar?.title = "Register"

        arb.signUpBt.setOnClickListener {
            signUpCoroutine.launch {
                Firebase.auth.createUserWithEmailAndPassword(
                    arb.emailRegisterEt.text.toString().trim(),
                    arb.passwordRegisterEt.text.toString().trim()
                ).addOnFailureListener {
                    Toast.makeText(
                        this@ActivityRegister,
                        "Failed to register . Cause: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnSuccessListener {
                    Toast.makeText(
                        this@ActivityRegister,
                        "Successful registration",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                finish()
            }
        }
    }
}