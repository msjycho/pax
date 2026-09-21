package kr.playax.novel.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kr.playax.novel.AppViewModel
import kr.playax.novel.R

@Composable
fun SettingsScreen(appViewModel: AppViewModel) {
    val adultUnlocked by appViewModel.adultUnlocked.collectAsState()
    val hideAdult by appViewModel.hideAdultPassages.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("설정", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.content_policy), style = MaterialTheme.typography.bodySmall)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("18+ 콘텐츠 잠금 해제", style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.adult_gate_body), style = MaterialTheme.typography.bodySmall)
            }
            Switch(
                checked = adultUnlocked,
                onCheckedChange = { appViewModel.setAdultUnlocked(it) },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("성인 장면 숨기기", style = MaterialTheme.typography.titleMedium)
                Text("무협·줄거리만 남기고 성인 마커 구간을 가립니다.", style = MaterialTheme.typography.bodySmall)
            }
            Switch(
                checked = hideAdult,
                onCheckedChange = { appViewModel.setHideAdultPassages(it) },
            )
        }
    }
}
