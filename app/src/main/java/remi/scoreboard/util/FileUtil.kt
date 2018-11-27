package remi.scoreboard.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileUtil {
    // TODO jpeg encode with limited res
    fun streamUriToFile(context: Context, uri: Uri, file: File) {
        context.contentResolver?.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(1024)
                var len = inputStream.read(buffer)
                while (len > 0) {
                    outputStream.write(buffer, 0, len)
                    len = inputStream.read(buffer)
                }
            }
        }
    }
}