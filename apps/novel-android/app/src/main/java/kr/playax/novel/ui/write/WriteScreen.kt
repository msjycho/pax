package kr.playax.novel.ui.write

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kr.playax.novel.data.UserChapter

@Composable
fun WriteScreen(appViewModel: AppViewModel) {
    val works by appViewModel.works.collectAsState()
    val chapters by appViewModel.selectedChapters.collectAsState()
    var selectedWorkId by remember { mutableStateOf<String?>(null) }
    var newTitle by remember { mutableStateOf("") }

    LaunchedEffect(selectedWorkId) {
        appViewModel.selectWork(selectedWorkId)
    }

    val selected = works.find { it.id == selectedWorkId }
    var editing by remember(selectedWorkId, chapters) {
        mutableStateOf(chapters.firstOrNull())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.write_subtitle),
            style = MaterialTheme.typography.bodyMedium,
        )

        OutlinedTextField(
            value = newTitle,
            onValueChange = { newTitle = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("새 작품 제목") },
            singleLine = true,
        )
        Button(onClick = {
            appViewModel.addWork(newTitle)
            newTitle = ""
        }) {
            Text("작품 만들기")
        }

        if (selected == null) {
            Text("내 작품", style = MaterialTheme.typography.titleMedium)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(works, key = { it.id }) { work ->
                    Text(
                        text = work.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedWorkId = work.id }
                            .padding(vertical = 8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            if (works.isEmpty()) {
                Text("아직 작품이 없습니다. 위에서 첫 작품을 만드세요.")
            }
        } else {
            Text(
                text = "← ${selected.title}",
                modifier = Modifier.clickable {
                    selectedWorkId = null
                    editing = null
                },
                style = MaterialTheme.typography.titleMedium,
            )
            val chapter = editing
            if (chapter != null) {
                ChapterEditor(
                    chapter = chapter,
                    onChange = { updated ->
                        editing = updated
                        appViewModel.upsertChapter(selected.id, updated)
                    },
                )
            }
        }
    }
}

@Composable
private fun ChapterEditor(chapter: UserChapter, onChange: (UserChapter) -> Unit) {
    var title by remember(chapter.id) { mutableStateOf(chapter.title) }
    var body by remember(chapter.id) { mutableStateOf(chapter.body) }

    OutlinedTextField(
        value = title,
        onValueChange = {
            title = it
            onChange(chapter.copy(title = it, body = body))
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("회차 제목") },
        singleLine = true,
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = body,
        onValueChange = {
            body = it
            onChange(chapter.copy(title = title, body = it))
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        label = { Text("본문") },
    )
    Text("${body.length}자 · ${chapter.status} · 자동 저장", style = MaterialTheme.typography.labelMedium)
}
