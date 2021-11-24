package com.example.androidclubapp.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.util.*

class UserIdGeneration {

    private val INSTALLATION = "INSTALLATION"

    fun makeNewUserId(): String {

        fun id() = UUID.randomUUID().toString()

        println("generated id - ${id()}")

        return id()
    }

    @Synchronized
    fun getOrGenerateUserId(context: Context, userId: String?, overrideMakeNewUserId: Boolean = false): String {

        return if(userId != null && !overrideMakeNewUserId){
            println("getOrGenerateUserId userId is defined - not generating new id. UserId: $userId")
            userId
        } else {
            val _path = context.filesDir
            val path2 = Environment.getStorageDirectory()
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val installation = File(path, INSTALLATION)
            try {

                println("installation file exists ${installation.exists()}")
                println("overrideMakeNewUserId ${overrideMakeNewUserId}")

                when {
                    installation.exists() && overrideMakeNewUserId -> {
                        installation.delete()
                        writeInstallationFile(installation)
                    }
                    !installation.exists() -> {
                        writeInstallationFile(installation)
                    }
                }
                readInstallationFile(installation)

            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    @Throws(IOException::class)
    private fun readInstallationFile(installation: File): String {
        val f = RandomAccessFile(installation, "r")
        val bytes = ByteArray(f.length().toInt())
        println("getOrGenerateUserId userId exists reading userId from file")
        f.readFully(bytes)
        f.close()
        return String(bytes)
    }

    @Throws(IOException::class)
    private fun writeInstallationFile(installation: File) {
        val out = FileOutputStream(installation)
        val id: String = makeNewUserId()
        println("getOrGenerateUserId userId is empty - generating new id. New UserId: $id")
        out.write(id.toByteArray())
        out.close()
    }
}