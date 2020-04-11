package com.github.shynixn.workbench.bukkit.testsuite.arena

import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import net.lingala.zip4j.ZipFile
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

/**
 * Created by Shynixn 2020.
 * <p>
 * Version 1.5
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2020 by Shynixn
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
class FfmpegService {
    private val ffmpegBinaries = mapOf(
        OSArchitectureTypes.WINDOWS_32 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffmpeg-4.2.1-win-32.zip",
        OSArchitectureTypes.WINDOWS_64 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffmpeg-4.2.1-win-64.zip",
        OSArchitectureTypes.LINUX_32 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffmpeg-4.2.1-linux-32.zip",
        OSArchitectureTypes.LINUX_64 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffmpeg-4.2.1-linux-64.zip",
        OSArchitectureTypes.LINUX_ARMHF to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffmpeg-4.2.1-linux-armhf-32.zip",
        OSArchitectureTypes.LINUX_ARMEL to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffmpeg-4.2.1-linux-armel-32.zip",
        OSArchitectureTypes.LINUX_ARM64 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffmpeg-4.2.1-linux-arm-64.zip",
        OSArchitectureTypes.OSX_64 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffmpeg-4.2.1-osx-64.zip"
    )

    private val ffProbeBinaries = mapOf(
        OSArchitectureTypes.WINDOWS_32 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffprobe-4.2.1-win-32.zip",
        OSArchitectureTypes.WINDOWS_64 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffprobe-4.2.1-win-64.zip",
        OSArchitectureTypes.LINUX_32 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffprobe-4.2.1-linux-32.zip",
        OSArchitectureTypes.LINUX_64 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffprobe-4.2.1-linux-64.zip",
        OSArchitectureTypes.LINUX_ARMHF to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffprobe-4.2.1-linux-armhf-32.zip",
        OSArchitectureTypes.LINUX_ARMEL to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffprobe-4.2.1-linux-armel-32.zip",
        OSArchitectureTypes.LINUX_ARM64 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffprobe-4.2.1-linux-arm-64.zip",
        OSArchitectureTypes.OSX_64 to "https://github.com/vot/ffbinaries-prebuilt/releases/download/v4.2.1/ffprobe-4.2.1-osx-64.zip"
    )

    /**
     * Installs the ffmpeg libraries for the given architecture.
     */
    fun checkAndInstall(folder: Path, osArchitectureTypes: OSArchitectureTypes): Path {
        Files.createDirectories(folder)
        val installationFolder = folder.resolve(osArchitectureTypes.identififer)

        if (Files.exists(installationFolder)) {
            return installationFolder
        }

        val downloadedffmpeg = downloadFileFromUrl(folder, ffmpegBinaries.getValue(osArchitectureTypes))
        Files.createDirectories(installationFolder)
        FileUtils.moveFileToDirectory(downloadedffmpeg.toFile(), installationFolder.toFile(), true)
        val downloadedffprobe = downloadFileFromUrl(folder, ffProbeBinaries.getValue(osArchitectureTypes))
        FileUtils.moveFileToDirectory(downloadedffprobe.toFile(), installationFolder.toFile(), true)
        FileUtils.deleteDirectory(folder.resolve("download").toFile())

        return installationFolder
    }

    fun convertToOgg(videoFile: Path, ffmFolder: Path) {
        val outPutPathMp3 = videoFile.parent.toFile().absolutePath + "/" + videoFile.toFile().nameWithoutExtension + ".mp3"
        val outPutPathOgg = videoFile.parent.toFile().absolutePath + "/" + videoFile.toFile().nameWithoutExtension + ".ogg"
        convertInputToOutPut(ffmFolder, videoFile.toFile().absolutePath, outPutPathMp3)
        convertInputToOutPut(ffmFolder, outPutPathMp3, outPutPathOgg)
    }

    private fun convertInputToOutPut(ffmFolder: Path, inputFile: String, outPutFile: String) {
        val ffmpeg = FFmpeg(ffmFolder.toFile().absolutePath + "/ffmpeg")
        val ffprobe = FFprobe(ffmFolder.toFile().absolutePath + "/ffprobe")

        val builder = FFmpegBuilder()
            .setInput(inputFile) // Filename, or a FFmpegProbeResult
            .overrideOutputFiles(true) // Override the output if it exists
            .addOutput(outPutFile) // Filename for the destination
            .done()

        val executor = FFmpegExecutor(ffmpeg, ffprobe)
        executor.createJob(builder).run()
    }

    private fun downloadFileFromUrl(folder: Path, url: String): Path {
        val downloadFolder = folder.resolve("download")

        if (Files.exists(downloadFolder)) {
            FileUtils.deleteDirectory(downloadFolder.toFile())
        }
        Files.createDirectories(downloadFolder)
        val downloadFile = downloadFolder.resolve("download.zip")

        val urlNet = URL(url)
        val urlConnection = urlNet.openConnection()
        urlConnection.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"
        )

        urlConnection.connect()
        val contentLength = urlConnection.contentLength.toLong()
        urlConnection.getInputStream().use { input ->
            ProgressFileOutStream(downloadFile.toFile(), contentLength) { progress ->
                println("Progress: " + progress)
            }.use { output ->
                IOUtils.copy(input, output)
            }
        }
        val extractAbleFile = ZipFile(downloadFile.toFile())
        extractAbleFile.extractAll(downloadFolder.toFile().absolutePath)
        val downloadedFile = downloadFolder.toFile().listFiles { f -> f.name != "download.zip" }!!.first()
        return downloadedFile.toPath()
    }

    class ProgressFileOutStream(file : File, val fileSize : Long, val progress : (Double) -> Unit) : FileOutputStream(file){
        var currentWrittenBytes : Long = 0

        /**
         * Writes the specified byte to this file output stream. Implements
         * the `write` method of `OutputStream`.
         *
         * @param      b   the byte to be written.
         * @exception  IOException  if an I/O error occurs.
         */
        override fun write(b: Int) {
            super.write(b)
            currentWrittenBytes++
            progress.invoke((currentWrittenBytes.toDouble() / fileSize.toDouble()))
        }

        /**
         * Writes `b.length` bytes from the specified byte array
         * to this file output stream.
         *
         * @param      b   the data.
         * @exception  IOException  if an I/O error occurs.
         */
        override fun write(b: ByteArray) {
            super.write(b)
            currentWrittenBytes += b.size
            progress.invoke((currentWrittenBytes.toDouble() / fileSize.toDouble()))
        }

        /**
         * Writes `len` bytes from the specified byte array
         * starting at offset `off` to this file output stream.
         *
         * @param      b     the data.
         * @param      off   the start offset in the data.
         * @param      len   the number of bytes to write.
         * @exception  IOException  if an I/O error occurs.
         */
        override fun write(b: ByteArray, off: Int, len: Int) {
            super.write(b, off, len)
            currentWrittenBytes += len
            progress.invoke((currentWrittenBytes.toDouble() / fileSize.toDouble()))
        }
    }
}