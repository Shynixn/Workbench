package com.github.shynixn.workbench.bukkit.testsuite.arena

import com.github.kiulian.downloader.YoutubeDownloader
import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import net.lingala.zip4j.ZipFile
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths


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
fun main(args: Array<String>) {
    val ffmpegInstallService = FfmpegInstallService()
    val ffmFolder = ffmpegInstallService.install(Paths.get("ffmpeg"), OSArchitectureTypes.WINDOWS_64)

    val ffmpeg = FFmpeg(ffmFolder.toFile().absolutePath + "/ffmpeg")
    val ffprobe = FFprobe(ffmFolder.toFile().absolutePath + "/ffprobe")

    val builder = FFmpegBuilder()
        .setInput("input.mp4") // Filename, or a FFmpegProbeResult
        .overrideOutputFiles(true) // Override the output if it exists
        .addOutput("output.mp4") // Filename for the destination
        .setFormat("mp4") // Format is inferred from filename, or can be set
        .setTargetSize(250000) // Aim for a 250KB file
        .disableSubtitle() // No subtiles
        .setAudioChannels(1) // Mono audio
        .setAudioCodec("aac") // using the aac codec
        .setAudioSampleRate(48000) // at 48KHz
        .setAudioBitRate(32768) // at 32 kbit/s
        .setVideoCodec("libx264") // Video using x264
        .setVideoFrameRate(24, 1) // at 24 frames per second
        .setVideoResolution(640, 480) // at 640x480 resolution
        .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
        .done()

    val executor = FFmpegExecutor(ffmpeg, ffprobe)

// Run a one-pass encode
    // Run a one-pass encode
    executor.createJob(builder).run()

// Or run a two-pass encode (which is better quality at the cost of being slower)
    // Or run a two-pass encode (which is better quality at the cost of being slower)
    executor.createTwoPassJob(builder).run()
}