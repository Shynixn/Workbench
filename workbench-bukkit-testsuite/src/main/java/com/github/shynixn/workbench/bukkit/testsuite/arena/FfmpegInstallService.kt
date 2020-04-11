package com.github.shynixn.workbench.bukkit.testsuite.arena

import net.lingala.zip4j.ZipFile
import org.apache.commons.io.FileUtils
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
class FfmpegInstallService {
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
    fun install(folder: Path, osArchitectureTypes: OSArchitectureTypes): Path {
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
        Files.copy(urlConnection.getInputStream(), downloadFile)

        val extractAbleFile = ZipFile(downloadFile.toFile())
        extractAbleFile.extractAll(downloadFolder.toFile().absolutePath)
        val downloadedFile = downloadFolder.toFile().listFiles { f -> f.name != "download.zip" }!!.first()
        return downloadedFile.toPath()
    }
}