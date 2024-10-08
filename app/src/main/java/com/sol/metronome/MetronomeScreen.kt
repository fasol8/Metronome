package com.sol.metronome

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun MetronomeScreen() {
    var bpm by remember { mutableStateOf(120f) }
    var isPlaying by remember { mutableStateOf(false) }

    val interval = (60000 / bpm).toLong() // Intervalo en milisegundos

    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_MUSIC, 100) }

    val bpmColor = when {
        bpm <= 120f -> lerp(Color.Blue, Color.Green, (bpm - 40f) / 80f)
        else -> lerp(Color.Green, Color.Red, (bpm - 120f) / 120f)
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Slider(
                value = bpm,
                onValueChange = { bpm = it },
                valueRange = 40f..240f,
                steps = 200,
                colors = SliderDefaults.colors(
                    thumbColor = bpmColor, // Color del "thumb" del slider
                    activeTrackColor = bpmColor, // Color de la parte activa del slider
                    inactiveTrackColor = bpmColor,
                    inactiveTickColor = Color.Gray,
                    activeTickColor = Color.Gray,

                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { bpm-- }, modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(bpmColor)
                ) {
                    Text(text = "-")
                }
                Text(
                    "BPM: ${bpm.toInt()}",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Button(
                    onClick = { bpm++ }, modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(bpmColor)
                ) {
                    Text(text = "+")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = { isPlaying = !isPlaying },
                colors = ButtonDefaults.buttonColors(bpmColor)
            ) {
                Icon(
                    painter = if (isPlaying) painterResource(id = R.drawable.ic_pause) else painterResource(
                        id = R.drawable.ic_play_arrow
                    ), contentDescription = ""
                )
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