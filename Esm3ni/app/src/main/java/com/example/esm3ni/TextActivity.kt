package com.example.esm3ni

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_text.*


class TextActivity : AppCompatActivity() {

     lateinit var db: DatabaseReference
    lateinit var dbRef: DatabaseReference
    lateinit var stRef :StorageReference
    lateinit var viewVid :VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)

        db = FirebaseDatabase.getInstance().getReference("videos")
        dbRef = db.child("video")

        viewVid = findViewById(R.id.videoView)

      //  var voicetxt = findViewById<EditText>(R.id.voiceText)
       // val videoTxt = getText1.text


        val controller = MediaController(this)




        var vid1="https://firebasestorage.googleapis.com/v0/b/esm3ni-5fc0f.appspot.com/o/3gla.mp4?alt=media&token=07151be5-f429-40c0-aa96-36e4544673f7"
        var uri1 : Uri=Uri.parse(vid1)

        var vid2="https://firebasestorage.googleapis.com/v0/b/esm3ni-5fc0f.appspot.com/o/car.mp4?alt=media&token=51e24937-3a5f-4b7b-b01c-bdf950d0d5ed"
        var uri2: Uri=Uri.parse(vid2)

        var vid3="https://firebasestorage.googleapis.com/v0/b/esm3ni-5fc0f.appspot.com/o/n2l.mp4?alt=media&token=992169d6-d4d0-42b5-b665-42f90deadc88"
        var uri3:Uri=Uri.parse(vid3)


            playBtn.setOnClickListener {
                if (voiceText.editText?.getText().toString() == "عجله") {
                    viewVid.setVideoURI(uri1)
                    controller?.setAnchorView(viewVid)
                    viewVid.setMediaController(controller)
                    viewVid.start()


                }
                else if (voiceText.editText?.getText().toString() == "سياره") {
                    viewVid.setVideoURI(uri2)
                    viewVid.setMediaController(controller)
                    viewVid.start()
                }
                else if (voiceText.editText?.getText().toString() == "سياره نقل") {
                    viewVid.setVideoURI(uri3)
                    viewVid.setMediaController(controller)
                    viewVid.start()}
                else
              Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show()
            }

        voicebtn.setOnClickListener{speak()}

    }
    private fun speak() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG")
        try {
            startActivityForResult(intent, 1000)
        } catch (e: Exception) {
            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

            super.onActivityResult(requestCode, resultCode, data)

            when (requestCode) {
                1000 ->

                    if (resultCode == Activity.RESULT_OK && data != null) {
                        val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        voiceText.editText?.setText(result!![0])
                    }
            }
        }

    }















