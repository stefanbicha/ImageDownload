package at.interactivecuriosity.imagedownload

import android.app.IntentService
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Message
import android.os.Messenger
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class MyService : IntentService("DownloadImageServices") {
    override fun onHandleIntent(p0: Intent?) {
        if (p0 != null) {
            if(p0.extras != null) {

                val urlString = p0.getStringExtra("urlString")
                val fileName = p0.getStringExtra("fileName")
                if (urlString != null && fileName != null) {
                    try {
                        val url = URL(urlString)
                        val connection = url.openConnection()
                        connection.connect()
                        val inputStream = connection.getInputStream()
                        val file = File(getExternalFilesDir(null), fileName)
                        FileOutputStream(file).use { output ->
                            inputStream.copyTo(output)
                        }
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)

                        val messenger = p0.getParcelableExtra<Messenger>("messenger")
                        val msg = Message.obtain()
                        msg.obj = bitmap
                        messenger?.send(msg)


                    } catch (e: Exception) {
                        e.printStackTrace()

                        val messenger = p0.getParcelableExtra<Messenger>("messenger")
                        val msg = Message.obtain()
                        msg.obj = "Error"
                        messenger?.send(msg)
                    }
                }






            }
        }
    }

}