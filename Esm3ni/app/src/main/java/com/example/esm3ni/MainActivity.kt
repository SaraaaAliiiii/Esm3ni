package com.example.esm3ni

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.speech.tts.TextToSpeech
import android.util.DisplayMetrics
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Button
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions
import  kotlinx.android.synthetic.main.activity_main.*
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*

private const val REQUEST_CODE_PERMISSIONS = 10
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)


class MainActivity : AppCompatActivity(), LifecycleOwner, TextToSpeech.OnInitListener {
    private var Twords: String = ""

    private var modelDownloaded = false
    private lateinit var labeler: FirebaseVisionImageLabeler

    private var tts:TextToSpeech? = null

    private var buttonspeek:Button? = null
    private val mModelPath = "model.tflite"
    private val mLabelPath = "dict.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val conditions = FirebaseModelDownloadConditions.Builder()
            .requireWifi()
            .build()


        val localModel = FirebaseAutoMLLocalModel.Builder()
            .setAssetFilePath("manifest.json")
            .build()
        val options = FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
            .setConfidenceThreshold(0.5f)
            .build()

        labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options)




start_btn.setOnClickListener {
    if (allPermissionsGranted()) {
        viewFinder.post { startCamera()
            start_btn.visibility= View.GONE
        }
    } else {
        ActivityCompat.requestPermissions(
            this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
        )
    }

    viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        updateTransform()
    }
}




        //--------------------------------------------------***/

//convert text to sound


        /*     tts = TextToSpeech(this,this)

          // buttonspeek = this.btnvoice
   //        buttonspeek!!.isEnabled=false

     //      buttonspeek!!.setOnClickListener {

             var text = text2.text.toString()


               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
               } else {
                   val hash = HashMap<String, String>()
                   hash.put(
                       TextToSpeech.Engine.KEY_PARAM_STREAM,
                       AudioManager.STREAM_NOTIFICATION.toString()
                   )
                   tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, hash)
               }

           }*/
       }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result != TextToSpeech.LANG_MISSING_DATA ||
                result != TextToSpeech.LANG_NOT_SUPPORTED
            )
                buttonspeek!!.isEnabled = true
        }
    }




    public override fun onDestroy() {

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        super.onDestroy()
    }

    //--------------------------//

    //convert sign to text //

    private fun startCamera() {
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)

        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(screenAspectRatio)
            setTargetRotation(viewFinder.display.rotation)
        }.build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener {
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
            val analyzerThread = HandlerThread(
                "Analysis"
            ).apply { start() }
            setCallbackHandler(Handler(analyzerThread.looper))
            setTargetResolution(Size(1280, 720))

            setImageReaderMode(
                ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE
            )
        }.build()

        val analyzerUseCase = ImageAnalysis(analyzerConfig)

        analyzerUseCase.setAnalyzer { imageProxy: ImageProxy, degrees: Int ->
            val mediaImage = imageProxy.image
            val imageRotation = degreesToFirebaseRotation(degrees)
            if (mediaImage != null ) {
                val image = FirebaseVisionImage.fromMediaImage(mediaImage, imageRotation)
                labeler.processImage(image)
                    .addOnSuccessListener { labels ->
                        for (label in labels) {
                            val food = label.text
                            val confidence = label.confidence
                            // foodItem.text = " $food حرف ال "
                            val textView = findViewById<TextView>(R.id.text2)
                            //   textView.setText(" $food حرف ال ").toString()
                            Twords = food.toString()
                            //  Tw = Array<String>(Twords)
                           // textView.setText("  $Twords")

                            val docRef = FirebaseFirestore.getInstance().collection("word").document("gomla")
                            docRef.get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        val textView = findViewById<TextView>(R.id.text2)
                                        val word=document.data
                                        if (word != null) {
                                            for ((k,m) in word){
                                                if(k==Twords)
                                                    textView.setText("  ${m} ").toString()


                                            }
                                        }


                                    } else {
                                        Toast.makeText(getApplicationContext(), "No such document", Toast.LENGTH_LONG).show();

                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(getApplicationContext(), "get failed with", Toast.LENGTH_LONG).show();

                                }
                        }
                    }


            }
        }
        CameraX.bindToLifecycle(this, preview, analyzerUseCase)
    }

    private fun updateTransform() {
        val matrix = Matrix()
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }

        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
        viewFinder.setTransform(matrix)
    }

    private fun degreesToFirebaseRotation(degrees: Int): Int = when (degrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }

    }
}
