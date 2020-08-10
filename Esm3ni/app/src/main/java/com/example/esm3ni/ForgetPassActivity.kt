package com.example.esm3ni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_forget_pass.*

class ForgotPassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        Submit.setOnClickListener {
            forget_pass()
        }
    }
    private fun forget_pass(){
        val Auth = FirebaseAuth.getInstance()
        val email = forgot_your_password_email.toString()

        if (forgot_your_password_email.editText?.text.toString().isEmpty()) {
            forgot_your_password_email.error = "Please enter your email"
            forgot_your_password_email.requestFocus()
            return
        }
        else {
            forgot_your_password_email.error = null
            forgot_your_password_email.clearFocus()
        }

        Auth!!.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Success please Check your email to reset your password", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Fail to send reset password email, enter your registered email and check your network connection and try again"
                    , Toast.LENGTH_SHORT).show()
            }
        }

    }
}