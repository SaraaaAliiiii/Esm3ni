package com.example.esm3ni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        val TAG = "LoginActivity"
    }
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSignIN.setOnClickListener {

            login()
        }

        btnSignUP.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

        btnForget.setOnClickListener{
            startActivity(Intent(this,ForgotPassActivity::class.java))
            finish()
        }
    }

    private fun login()
    {
        if (editEmail.editText?.text.toString().isEmpty()) {
            editEmail.error = "Please enter email"
            editEmail.requestFocus()
            return
        }
        else {
            editEmail.error = null
            editEmail.clearFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(editEmail.editText?.text.toString()).matches()) {
            editEmail.error = "Please enter valid email"
            editEmail.requestFocus()
            return
        }
        else {
            editEmail.error = null
            editEmail.clearFocus()
        }

        if (editpass.editText?.text.toString().isEmpty()) {
            editpass.error = "Please enter password"
            editpass.requestFocus()
            return
        }
        else {
            editEmail.error = null
            editEmail.clearFocus()
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(editEmail.editText?.text.toString(), editpass.editText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (user != null && !user.isEmailVerified) {
                        Toast.makeText(baseContext, "please verify your mail",
                            Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Log.d(TAG, "signInWithEmail:success")
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()
                    }

                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Login failed."+task.exception?.message,
                        Toast.LENGTH_LONG).show()

                }
            }
    }
}
