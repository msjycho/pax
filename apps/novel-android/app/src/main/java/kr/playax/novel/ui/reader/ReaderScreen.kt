package kr.playax.novel.ui.reader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.playax.novel.AppViewModel
import kr.playax.novel.R
import kr.playax.novel.content.AdultPassageFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    appViewModel: AppViewModel,
    workSlug: String,
    volumeIndex: Int,
    chapterIndex: Int,
    onBack: () -> Unit,
) {
    val adultUnlocked by appViewModel.adultUnlocked.collectAsState()
    val hideAdult by appViewModel.hideAdultPassages.collectAsState()
    val lockedPlaceholder = stringResource(R.string.adult_locked_placeholder)
    val body = remember(workSlug, volumeIndex, chapterIndex, adultUnlocked, hideAdult) {
        val raw = appViewModel.loadChapterBody(workSlug, volumeIndex, chapterIndex)
        AdultPassageFilter.apply(raw, adultUnlocked, hideAdult, lockedPlaceholder)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("${volumeIndex}권 · ${chapterIndex}화") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "뒤로")
                }
            },
        )
        Text(
            text = body,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp),
        )
    }
}
