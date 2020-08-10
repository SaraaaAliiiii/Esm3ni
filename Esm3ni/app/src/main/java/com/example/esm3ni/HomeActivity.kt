package com.example.esm3ni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

   // private val buttonSign = findViewById<Button>(R.id.btnSign)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

              btnSign.setOnClickListener {
                  val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

              }
        outbtn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        btnText.setOnClickListener {
            val intent = Intent(this, TextActivity::class.java)
            startActivity(intent)

        }


    }
}





