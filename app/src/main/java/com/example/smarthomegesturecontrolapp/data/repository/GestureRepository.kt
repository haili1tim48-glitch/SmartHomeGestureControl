package com.example.smarthomegesturecontrolapp.data.repository

import android.content.Context
import com.example.smarthomegesturecontrolapp.data.model.Gesture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GestureRepository(private val context: Context) {

    companion object {
        private const val GESTURES_DIR = "gestures"
    }

    /**
     * Gets the list of available gesture video files from assets.
     * This simulates fetching a list from a server.
     */
    private fun getAssetGestureList(): List<Pair<String, String>> {
        return try {
            val assetFiles = context.assets.list("") ?: emptyArray()
            assetFiles
                .filter { it.endsWith(".mp4") }
                .mapIndexed { index, fileName ->
                    val gestureName = fileName
                        .removeSuffix(".mp4")
                        .replace("_", " ")
                        .replaceFirstChar { it.uppercase() }
                    Pair("${index + 1}", fileName)
                }
        } catch (e: IOException) {
            emptyList()
        }
    }

    /**
     * Downloads (copies) all gesture videos from assets to internal storage.
     * This simulates downloading from a URL by copying from assets.
     * Always performs a fresh copy (force refresh).
     *
     * @return Result containing list of Gesture objects with localPath set, or an error
     */
    suspend fun downloadGestures(): Result<List<Gesture>> = withContext(Dispatchers.IO) {
        try {
            val gesturesDir = File(context.filesDir, GESTURES_DIR)

            // Force refresh: Delete existing directory and recreate
            if (gesturesDir.exists()) {
                gesturesDir.deleteRecursively()
            }
            gesturesDir.mkdirs()

            val assetFiles = context.assets.list("") ?: emptyArray()
            val videoFiles = assetFiles.filter { it.endsWith(".mp4") }

            if (videoFiles.isEmpty()) {
                return@withContext Result.failure(IOException("No gesture videos found in assets"))
            }

            val gestures = videoFiles.mapIndexed { index, fileName ->
                val destinationFile = File(gesturesDir, fileName)

                // Copy file from assets to internal storage
                context.assets.open(fileName).use { inputStream ->
                    FileOutputStream(destinationFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }

                // Create Gesture object with the local path
                val gestureName = fileName
                    .removeSuffix(".mp4")
                    .replace("_", " ")
                    .split(" ")
                    .joinToString(" ") { word ->
                        word.replaceFirstChar { it.uppercase() }
                    }

                Gesture(
                    id = "${index + 1}",
                    name = gestureName,
                    downloadUrl = "asset:///$fileName",
                    localPath = destinationFile.absolutePath
                )
            }

            Result.success(gestures)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(IOException("Failed to download gestures: ${e.message}"))
        }
    }

    /**
     * Gets the list of already downloaded gestures from internal storage.
     * Returns empty list if no gestures have been downloaded.
     */
    suspend fun getDownloadedGestures(): List<Gesture> = withContext(Dispatchers.IO) {
        val gesturesDir = File(context.filesDir, GESTURES_DIR)
        if (!gesturesDir.exists()) {
            return@withContext emptyList()
        }

        gesturesDir.listFiles()
            ?.filter { it.extension == "mp4" }
            ?.mapIndexed { index, file ->
                val gestureName = file.nameWithoutExtension
                    .replace("_", " ")
                    .split(" ")
                    .joinToString(" ") { word ->
                        word.replaceFirstChar { it.uppercase() }
                    }

                Gesture(
                    id = "${index + 1}",
                    name = gestureName,
                    downloadUrl = "asset:///${file.name}",
                    localPath = file.absolutePath
                )
            } ?: emptyList()
    }
}
