package kr.playax.novel.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kr.playax.novel.AppViewModel
import kr.playax.novel.R
import kr.playax.novel.content.CatalogWork

@Composable
fun LibraryScreen(
    appViewModel: AppViewModel,
    onOpenChapter: (slug: String, volume: Int, chapter: Int) -> Unit,
) {
    val adultUnlocked by appViewModel.adultUnlocked.collectAsState()
    val works = remember { appViewModel.catalogWorks() }
    var pendingAdultOpen by remember { mutableStateOf<Triple<String, Int, Int>?>(null) }
    var showGate by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("PAX · 원작 라이브러리", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.content_policy), style = MaterialTheme.typography.bodySmall)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(works, key = { it.slug }) { work ->
                WorkBlock(
                    work = work,
                    adultUnlocked = adultUnlocked,
                    onChapterClick = { slug, vol, ch, needsAdult ->
                        if (needsAdult && !adultUnlocked) {
                            pendingAdultOpen = Triple(slug, vol, ch)
                            showGate = true
                        } else {
                            onOpenChapter(slug, vol, ch)
                        }
                    },
                )
            }
        }
    }

    if (showGate) {
        AlertDialog(
            onDismissRequest = {
                showGate = false
                pendingAdultOpen = null
            },
            title = { Text(stringResource(R.string.adult_gate_title)) },
            text = { Text(stringResource(R.string.adult_gate_body)) },
            confirmButton = {
                Button(onClick = {
                    appViewModel.setAdultUnlocked(true)
                    showGate = false
                    pendingAdultOpen?.let { (s, v, c) -> onOpenChapter(s, v, c) }
                    pendingAdultOpen = null
                }) {
                    Text(stringResource(R.string.adult_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showGate = false
                    pendingAdultOpen = null
                }) {
                    Text("취소")
                }
            },
        )
    }
}

@Composable
private fun WorkBlock(
    work: CatalogWork,
    adultUnlocked: Boolean,
    onChapterClick: (slug: String, volume: Int, chapter: Int, needsAdult: Boolean) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(work.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            if (work.contentRating == "adult") {
                AssistChip(
                    onClick = {},
                    label = { Text(if (adultUnlocked) "18+" else "18+ 잠금") },
                )
            }
        }
        Text(work.authorCredit, style = MaterialTheme.typography.labelMedium)
        Text(work.synopsis, style = MaterialTheme.typography.bodyMedium)
        work.volumes.forEach { volume ->
            Text("${volume.index}권 · ${volume.title}", style = MaterialTheme.typography.titleSmall)
            volume.chapters.forEach { chapter ->
                val locked = chapter.hasAdultPassages && !adultUnlocked
                Text(
                    text = buildString {
                        append("${chapter.index}. ${chapter.title}")
                        if (locked) append(" 🔒")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onChapterClick(
                                work.slug,
                                volume.index,
                                chapter.index,
                                chapter.hasAdultPassages || work.contentRating == "adult",
                            )
                        }
                        .padding(vertical = 6.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
