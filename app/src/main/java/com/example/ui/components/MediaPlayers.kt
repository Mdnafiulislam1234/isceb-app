package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import com.example.ui.icons.Audiotrack
import com.example.ui.icons.ChevronLeft
import com.example.ui.icons.ChevronRight
import androidx.compose.material.icons.filled.Delete
import com.example.ui.icons.Description
import com.example.ui.icons.Forward10
import com.example.ui.icons.OpenInNew
import com.example.ui.icons.Pause
import androidx.compose.material.icons.filled.PlayArrow
import com.example.ui.icons.Replay10
import com.example.ui.icons.SwapHoriz
import com.example.ui.icons.Videocam
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LessonMedia
import com.example.util.MediaFileManager
import com.example.ui.theme.DangerRed
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.GoldSoft
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.NavySurface

// ======================= PDF VIEWER CARD =======================
@Composable
fun PdfViewerCard(
    pdfMedia: LessonMedia,
    isAdmin: Boolean,
    onReplace: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentPage by remember { mutableIntStateOf(1) }
    val totalPages = 4

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("card_pdf_viewer"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFFEBEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "PDF Document",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = pdfMedia.fileName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = "PDF Lecture Notes • ${pdfMedia.fileSizeFormatted}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF6B7F96),
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                if (isAdmin) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onReplace, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Replace PDF",
                                tint = NavyPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete PDF",
                                tint = DangerRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Embedded PDF Preview Paper Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFAFBFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Executive Document Sheet — Page $currentPage of $totalPages",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                        Text(
                            text = "CONFIDENTIAL SCM STUDY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 9.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    when (currentPage) {
                        1 -> {
                            Text(
                                text = "1. Executive Framework & Mathematical Foundations",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary,
                                    fontSize = 13.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Cost of Capital: r = WACC + Risk Buffer (typical: 18-25%)\n• Annual Holding Cost = (Q/2) * H + (ss * H)\n• Service Level Cycle factor: k(0.95) = 1.65, k(0.99) = 2.33\n• Safety Stock SS = k * σ_L * √Lead_Time",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF334155),
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                        2 -> {
                            Text(
                                text = "2. Incoterms & Trade Compliance Checkpoints",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary,
                                    fontSize = 13.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Ocean Freight: FOB (Port of Loading) vs CIF (Port of Destination)\n• Carrier Liability: Hamburg Rules vs Hague-Visby rules\n• Customs Valuation: Transaction value + Additions (packing, assists, royalties)\n• Air Cargo ratio: 1 CBM = 167 kg chargeable weight standard.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF334155),
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                        3 -> {
                            Text(
                                text = "3. S&OP Master Process & Demand Consensus",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary,
                                    fontSize = 13.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Step 1: Data Gathering (POS pings, order backlog, inventory audits)\n• Step 2: Unconstrained Commercial Demand Plan (Sales & Marketing)\n• Step 3: Supply Capability & Plant Capacity Constraints (Operations)\n• Step 4: Executive Consensus & Financial Reconciliation meeting.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF334155),
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                        else -> {
                            Text(
                                text = "4. Strategic Risk Matrix & Geopolitical Mitigation",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary,
                                    fontSize = 13.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Dual-Sourcing Mandate: 70/30 split between primary and nearshore\n• Buffer Lead Times: Add 14 days safety horizon for maritime choke points\n• Scope 3 Decarbonization: Target 30% reduction via intermodal rail\n• End of document notes.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF334155),
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Page navigation & External intent
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { if (currentPage > 1) currentPage-- },
                        enabled = currentPage > 1,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous Page",
                            tint = if (currentPage > 1) NavyPrimary else Color.LightGray
                        )
                    }

                    Text(
                        text = "$currentPage / $totalPages",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                    )

                    IconButton(
                        onClick = { if (currentPage < totalPages) currentPage++ },
                        enabled = currentPage < totalPages,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next Page",
                            tint = if (currentPage < totalPages) NavyPrimary else Color.LightGray
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        val opened = MediaFileManager.openMediaWithSystemViewer(context, pdfMedia)
                        if (!opened) {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(Uri.parse(pdfMedia.url.ifEmpty { "https://example.com/lecture.pdf" }), "application/pdf")
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        }
                    },
                    modifier = Modifier.height(34.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Open PDF", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ======================= AUDIO PLAYER CARD =======================
@Composable
fun AudioPlayerCard(
    audioMedia: LessonMedia,
    isPlaying: Boolean,
    progressMs: Long,
    durationMs: Long,
    playbackSpeed: Float,
    isAdmin: Boolean,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onSkipSeconds: (Int) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onReplace: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalMs = if (durationMs > 0) durationMs else 18 * 60 * 1000L
    val progressRatio = (progressMs.toFloat() / totalMs.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("card_audio_player"),
        colors = CardDefaults.cardColors(containerColor = NavyDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavySecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Audiotrack,
                            contentDescription = "Audio Lecture",
                            tint = GoldMetallic,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = audioMedia.fileName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = "Audio Lecture • ${audioMedia.fileSizeFormatted}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GoldSoft,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                if (isAdmin) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onReplace, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Replace Audio",
                                tint = GoldSoft,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Audio",
                                tint = DangerRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Animated waveform bars simulation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyPrimary.copy(alpha = 0.6f))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val heights = listOf(14, 24, 30, 18, 28, 12, 22, 32, 16, 26, 30, 20, 14, 28, 18, 24, 32, 15, 25, 20)
                heights.forEachIndexed { idx, h ->
                    val barProgress = idx.toFloat() / heights.size.toFloat()
                    val isActive = barProgress <= progressRatio
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(if (isPlaying) (h * (0.8f + (idx % 3) * 0.1f)).dp else (h * 0.5f).dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isActive) GoldMetallic else Color(0xFF2C4362))
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Slider
            Slider(
                value = progressRatio,
                onValueChange = { ratio ->
                    onSeek((ratio * totalMs).toLong())
                },
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = GoldMetallic,
                    activeTrackColor = GoldMetallic,
                    inactiveTrackColor = NavySecondary
                )
            )

            // Timestamps
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(progressMs),
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 11.sp)
                )
                Text(
                    text = formatTime(totalMs),
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF9FB2CA), fontSize = 11.sp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Controls: Skip -10, Play/Pause, Skip +10, Speed Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Playback speed selector chips
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f).forEach { speed ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (playbackSpeed == speed) GoldMetallic else NavySecondary)
                                .clickable { onSpeedChange(speed) }
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${speed}x",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (playbackSpeed == speed) NavyPrimary else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }

                // Playback buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onSkipSeconds(-10) }, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = "Rewind 10 seconds",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(GoldMetallic)
                            .clickable { onPlayPause() }
                            .testTag("btn_audio_play_pause"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = NavyPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    IconButton(onClick = { onSkipSeconds(10) }, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = "Forward 10 seconds",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

// ======================= VIDEO PLAYER CARD =======================
@Composable
fun VideoPlayerCard(
    videoMedia: LessonMedia,
    isPlaying: Boolean,
    positionMs: Long,
    durationMs: Long,
    isAdmin: Boolean,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onReplace: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalMs = if (durationMs > 0) durationMs else 12 * 60 * 1000L + 30 * 1000L
    val progressRatio = (positionMs.toFloat() / totalMs.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("card_video_player"),
        colors = CardDefaults.cardColors(containerColor = NavyDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavySecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Video Lesson",
                            tint = GoldMetallic,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = videoMedia.fileName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = "Executive Video • ${videoMedia.fileSizeFormatted}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GoldSoft,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                if (isAdmin) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onReplace, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Replace Video",
                                tint = GoldSoft,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Video",
                                tint = DangerRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Video Screen Canvas Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .background(Color.Black)
                    .clickable { onPlayPause() },
                contentAlignment = Alignment.Center
            ) {
                // Gradient backdrop
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    NavyPrimary.copy(alpha = 0.8f),
                                    NavyDark,
                                    Color.Black
                                )
                            )
                        )
                )

                // Visual telemetry overlay
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Red)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isPlaying) "STREAMING LIVE" else "READY",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        }

                        Text(
                            text = "1080p HD • BROWSER STREAMING",
                            color = GoldSoft,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Center Play/Pause Indicator
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(GoldMetallic.copy(alpha = 0.9f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = NavyPrimary,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    // Bottom Seek & Duration inside player
                    Column {
                        Slider(
                            value = progressRatio,
                            onValueChange = { ratio -> onSeek((ratio * totalMs).toLong()) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = GoldMetallic,
                                activeTrackColor = GoldMetallic,
                                inactiveTrackColor = Color.DarkGray
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${formatTime(positionMs)} / ${formatTime(totalMs)}",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Auto-bandwidth Adaptive",
                                color = Color(0xFFA0B4CB),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
