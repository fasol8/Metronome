package com.sol.metronome

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MetronomeScreen() {
    var bpm by remember { mutableStateOf(120f) }
    var isPlaying by remember { mutableStateOf(false) }

    val interval = (60000 / bpm).toLong() // Intervalo en milisegundos

    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_MUSIC, 100) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Spacer(modifier = Modifier.height(64.dp))

            Slider(
                value = bpm,
                onValueChange = { bpm = it },
                valueRange = 40f..240f,
                steps = 200,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { bpm-- }, modifier = Modifier.padding(8.dp)) {
                    Text(text = "-")
                }
                Text(
                    "BPM: ${bpm.toInt()}",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Button(onClick = { bpm++ }, modifier = Modifier.padding(8.dp)) {
                    Text(text = "+")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            Button(onClick = { isPlaying = !isPlaying }) {
                Text(if (isPlaying) "Detener" else "Iniciar")
            }
        }
    }

    LaunchedEffect(isPlaying, bpm) {
        if (isPlaying) {
            while (isPlaying) {
                toneGenerator.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
                delay(interval)
            }
        } else {
            toneGenerator.stopTone()
        }
    }
}