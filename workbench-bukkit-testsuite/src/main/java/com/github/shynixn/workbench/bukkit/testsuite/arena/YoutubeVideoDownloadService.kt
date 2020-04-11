package com.github.shynixn.workbench.bukkit.testsuite.arena

import com.github.kiulian.downloader.YoutubeDownloader
import org.apache.commons.io.FileUtils
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
class YoutubeVideoDownloadService {
    /**
     * Downloads a single video to the given file.
     */
    fun downloadVideo(videoUrl: String, file: Path) {
        val v = YoutubeDownloader();
        val id = videoUrl.split("=")[1]
        val video = v.getVideo(id)

        val videoDownloadDirectory = file.parent.resolve("videodownload")
        Files.createDirectories(videoDownloadDirectory)
        video.download(video.audioFormats()[0], videoDownloadDirectory.toFile())
        Files.deleteIfExists(file)
        FileUtils.moveFile(videoDownloadDirectory.toFile().listFiles()!!.first(), file.toFile())
        FileUtils.deleteDirectory(videoDownloadDirectory.toFile())
    }
}