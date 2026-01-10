package com.example.smarthomegesturecontrolapp.data.repository

import android.content.Context
import com.example.smarthomegesturecontrolapp.data.model.Gesture
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GestureRepository(private val context: Context) {

    companion object {
        private const val GESTURES_SUBFOLDER = "gestures"
    }

    /**
     * Gets the list of available gestures from assets.
     * This simulates getting metadata from a server.
     */
    fun getAvailableGestures(): List<Gesture> {
        return listOf(
            Gesture(
                id = "1",
                name = "Light On",
                downloadUrl = "fan_on.mp4",
                localPath = null
            ),
            Gesture(
                id = "2",
                name = "Light Off",
                downloadUrl = "light_off.mp4",
                localPath = null
            ),
            Gesture(
                id = "3",
                name = "Fan On",
                downloadUrl = "fan_on.mp4",
                localPath = null
            ),
            Gesture(
                id = "4",
                name = "Fan Off",
                downloadUrl = "fan_off.mp4",
                localPath = null
            )
        )
    }

    /**
     * Copies all gesture video files from assets to internal storage.
     * This simulates "downloading" files from a server.
     *
     * @return Result containing list of Gestures with updated localPath, or an error
     */
    fun downloadGesturesToInternalStorage(): Result<List<Gesture>> {
        return try {
            val gesturesDir = File(context.filesDir, GESTURES_SUBFOLDER)

            // Create the gestures directory if it doesn't exist
            if (!gesturesDir.exists()) {
                gesturesDir.mkdirs()
            }

            // Get the list of video files in assets
            val assetFiles = context.assets.list("") ?: emptyArray()
            val videoFiles = assetFiles.filter { it.endsWith(".mp4") }

            if (videoFiles.isEmpty()) {
                return Result.failure(IOException("No video files found in assets folder"))
            }

            val gestures = mutableListOf<Gesture>()

            videoFiles.forEachIndexed { index, fileName ->
                // Copy file from assets to internal storage
                val destinationFile = File(gesturesDir, fileName)

                // Always overwrite (force refresh)
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

                gestures.add(
                    Gesture(
                        id = (index + 1).toString(),
                        name = gestureName,
                        downloadUrl = "assets://$fileName",
                        localPath = destinationFile.absolutePath
                    )
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
     * Checks if gestures have already been downloaded to internal storage.
     */
    fun getDownloadedGestures(): List<Gesture> {
        val gesturesDir = File(context.filesDir, GESTURES_SUBFOLDER)
        if (!gesturesDir.exists()) {
            return emptyList()
        }

        return gesturesDir.listFiles()
            ?.filter { it.extension == "mp4" }
            ?.mapIndexed { index, file ->
                val gestureName = file.nameWithoutExtension
                    .replace("_", " ")
                    .split(" ")
                    .joinToString(" ") { word ->
                        word.replaceFirstChar { it.uppercase() }
                    }

                Gesture(
                    id = (index + 1).toString(),
                    name = gestureName,
                    downloadUrl = "assets://${file.name}",
                    localPath = file.absolutePath
                )
            } ?: emptyList()
    }
}
