package at.interactivecuriosity.imagedownload

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.interactivecuriosity.imagedownload.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var downloadButton: Button
    private lateinit var deleteButton: Button
    private val imageUrl = "https://www.markusmaurer.at/fhj/eyecatcher.jpg" // URL des herunterzuladenden Bildes
    private val fileName = "downloadedImage.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        downloadButton = findViewById(R.id.downloadButton)
        deleteButton = findViewById(R.id.deleteButton)

        downloadButton.setOnClickListener {
            downloadImage(imageUrl, fileName)
        }

        deleteButton.setOnClickListener {
            deleteImage(fileName)
        }
    }

    private fun downloadImage(urlString: String, fileName: String) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "Bild download gestartet", Toast.LENGTH_SHORT).show()
        }

        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                val bitmapOrError = msg.obj

                if(bitmapOrError is Bitmap){
                    runOnUiThread {
                        imageView.setImageBitmap(bitmapOrError)
                        Toast.makeText(this@MainActivity, "Bild download beendet", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Bild download fehlgeschlagen", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val messenger = Messenger(handler)

        val intent = Intent(this@MainActivity, MyService::class.java)
        intent.putExtra("urlString",urlString)
        intent.putExtra("fileName",fileName)
        intent.putExtra("messenger", messenger)
        startService(intent)




    }

    private fun deleteImage(fileName: String) {
        val file = File(getExternalFilesDir(null), fileName)
        if (file.exists()) {
            file.delete()
            runOnUiThread {
                imageView.setImageBitmap(null)
                Toast.makeText(this, "Bild gelöscht", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
