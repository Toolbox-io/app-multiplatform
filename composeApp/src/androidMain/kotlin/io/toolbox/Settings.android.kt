package io.toolbox

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioManager.STREAM_ALARM
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import io.toolbox.App.Companion.context
import io.toolbox.Settings.UnlockProtection
import kotlinx.io.IOException

actual fun appsDefault() =
    context
    .packageManager
    .getInstalledPackages(0)
    .map { it.packageName }
    .toSet()

actual fun runActions() {
    val mediaPlayer = MediaPlayer()
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    // Take the required actions
    if (UnlockProtection.Alarm.enabled) {
        mediaPlayer.apply {
            if (mediaPlayer.isPlaying) stop()
            reset()
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
            if (UnlockProtection.Alarm.current == "") {
                val afd = context.assets.openFd("alarm.mp3")
                setDataSource(
                    afd.fileDescriptor,
                    afd.startOffset,
                    afd.length
                )
            } else {
                try {
                    setDataSource(context, UnlockProtection.Alarm.current.toUri())
                } catch (_: IOException) {
                    Log.w("DeviceAdmin", "Invalid custom alarm URI, falling back to default")
                    UnlockProtection.Alarm.current = ""
                    val afd = context.assets.openFd("alarm.mp3")
                    setDataSource(
                        afd.fileDescriptor,
                        afd.startOffset,
                        afd.length
                    )
                }
            }
            prepare()
            start()

            Thread {
                while (mediaPlayer.isPlaying) {
                    audioManager.setStreamVolume(STREAM_ALARM, audioManager.getStreamMaxVolume(STREAM_ALARM), 0)
                    Thread.sleep(100)
                }
            }.start()
        }
    }
    // TODO implement
    /*if (UnlockProtection.IntruderPhoto.enabled) {
        takePhoto(context, "${System.currentTimeMillis()}")
    }*/
}