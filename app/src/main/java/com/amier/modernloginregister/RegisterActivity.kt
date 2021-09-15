package com.amier.modernloginregister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        btnLogRegister.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)

        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("User")

        register()
    }
    private fun register() {

        RegisterButton.setOnClickListener {

            if(TextUtils.isEmpty(nameR.text.toString())) {
                nameR.setError("Please enter first name ")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(emailR.text.toString())) {
                nameR.setError("Please enter email ")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(passwordR.text.toString())) {
                nameR.setError("Please enter password ")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(emailR.text.toString(), passwordR.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        val currentUser = auth.currentUser
                        val currentUSerDb = databaseReference?.child((currentUser?.uid!!))
                        currentUSerDb?.child("Name")?.setValue(nameR.text.toString())
                        currentUSerDb?.child("Phone")?.setValue(phoneR.text.toString())
                        currentUSerDb?.child("Address")?.setValue(addressR.text.toString())
                        currentUSerDb?.child("Email")?.setValue(emailR.text.toString())


                        Toast.makeText(this@RegisterActivity, "Registration Success. ", Toast.LENGTH_LONG).show()
                        finish()

                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration failed, please try again! ", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

}
