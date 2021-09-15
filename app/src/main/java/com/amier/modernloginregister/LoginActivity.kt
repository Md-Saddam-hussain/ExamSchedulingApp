package com.amier.modernloginregister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        btnRegLogin.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left)
        }
        auth = FirebaseAuth.getInstance()

        val currentuser = auth.currentUser
        if(currentuser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity2::class.java))
            finish()
        }
        login()
    }
    private fun login() {

        LoginButton.setOnClickListener {

            if(TextUtils.isEmpty(emailL.text.toString())){
                emailL.setError("Please enter username")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(passwordL.text.toString())){
                passwordL.setError("Please enter password")
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(emailL.text.toString(), passwordL.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        startActivity(Intent(this@LoginActivity, MainActivity2::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed, please try again! ", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
}
