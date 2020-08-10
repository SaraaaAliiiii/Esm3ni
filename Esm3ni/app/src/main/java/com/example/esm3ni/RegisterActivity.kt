package com.example.esm3ni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
    companion object {
        val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if(auth.currentUser != null && user?.isEmailVerified!!){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()}
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

                already_have_account.setOnClickListener{
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }
                btnRegister.setOnClickListener{
                    signUpUser()
                }
            }
    private fun verify(){

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "Email sent.")
                }
            }
    }
    
    private fun signUpUser() {
                if (editName.editText?.text.toString().isEmpty()) {
                    editName.error = "Please enter Name"
                    editName.requestFocus()
                    return
                }
                else {
                    editName.error = null
                    editName.clearFocus()
                }

                if (editRegisterEmail.editText?.text.toString().isEmpty()) {
                    editRegisterEmail.error = "Please enter email"
                    editRegisterEmail.requestFocus()
                    return
                }
                else {
                    editRegisterEmail.error = null
                    editRegisterEmail.clearFocus()
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(editRegisterEmail.editText?.text.toString()).matches()) {
                    editRegisterEmail.error = "Please enter valid email"
                    editRegisterEmail.requestFocus()
                    return
                }
                else {
                    editRegisterEmail.error = null
                    editRegisterEmail.clearFocus()
                }

                if (editPassRegister.editText?.text.toString().isEmpty()) {
                    editPassRegister.error = "Please enter password"
                    editPassRegister.requestFocus()
                    return
                }
                else {
                    editPassRegister.error = null
                    editPassRegister.clearFocus()
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(editRegisterEmail.editText?.text.toString(),  editPassRegister.editText?.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            verify()
                            startActivity(Intent(this,LoginActivity::class.java))
                            finish()
                            Toast.makeText(baseContext, "Success! please verify your mail to continue", Toast.LENGTH_SHORT
                            ).show()

                            Log.d(TAG, "createUserWithEmail:success")
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed."+task.exception?.message,
                                Toast.LENGTH_SHORT).show()
                        }

                    }
            }
        }



