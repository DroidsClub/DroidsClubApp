package com.example.androidclubapp.connectors

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import java.io.*
import java.util.*

@SuppressLint("StaticFieldLeak")
@Suppress("DEPRECATION")
class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {

    override fun doInBackground(vararg urls: String): Bitmap? {
        val imageURL = urls[0]
        var image: Bitmap? = null
        try {
            val `in` = java.net.URL(imageURL).openStream()
            image = BitmapFactory.decodeStream(`in`)
        }
        catch (e: Exception) {
            Log.e("Error Message", e.message.toString())
            e.printStackTrace()
        }
        return image
    }
    override fun onPostExecute(result: Bitmap?) {

        if (result != null) {
            FileUtils.createFile(result)
        }
        imageView.setImageBitmap(result)
    }
}

object FileUtils {

    fun import(uri: Uri, context: Context): String?{
        val inputStream: InputStream

        return try {
            inputStream = context.contentResolver?.openInputStream(uri)!!
            inputStream.bufferedReader().use { it.readLine() }
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }

    fun createFile(bitmap: Bitmap) {

        println("About to save")

        val randomFilename = UUID.randomUUID().toString()

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, "$randomFilename.png")

        val outputStream: FileOutputStream
        try {
            outputStream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
            outputStream.flush()
            outputStream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}