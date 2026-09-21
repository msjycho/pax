package kr.playax.novel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kr.playax.novel.content.BundledCatalog
import kr.playax.novel.content.CatalogWork
import kr.playax.novel.data.UserChapter
import kr.playax.novel.data.UserSettingsRepository
import kr.playax.novel.data.UserWork
import kr.playax.novel.data.UserWritingRepository

class AppViewModel(
    private val settingsRepository: UserSettingsRepository,
    private val writingRepository: UserWritingRepository,
    private val catalog: BundledCatalog,
) : ViewModel() {

    val adultUnlocked: StateFlow<Boolean> = settingsRepository.adultUnlocked
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val hideAdultPassages: StateFlow<Boolean> = settingsRepository.hideAdultPassages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val works: StateFlow<List<UserWork>> = writingRepository.works
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun catalogWorks(): List<CatalogWork> = catalog.works()

    fun loadChapterBody(workSlug: String, volumeIndex: Int, chapterIndex: Int): String =
        catalog.loadChapterMarkdown(workSlug, volumeIndex, chapterIndex)

    fun setAdultUnlocked(unlocked: Boolean) {
        viewModelScope.launch { settingsRepository.setAdultUnlocked(unlocked) }
    }

    fun setHideAdultPassages(hide: Boolean) {
        viewModelScope.launch { settingsRepository.setHideAdultPassages(hide) }
    }

    fun addWork(title: String) {
        viewModelScope.launch { writingRepository.addWork(title) }
    }

    fun chaptersFor(workId: String): List<UserChapter> = writingRepository.chaptersFor(workId)

    fun upsertChapter(workId: String, chapter: UserChapter) {
        viewModelScope.launch { writingRepository.upsertChapter(workId, chapter) }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val appContext = context.applicationContext
                    return AppViewModel(
                        settingsRepository = UserSettingsRepository(appContext),
                        writingRepository = UserWritingRepository(appContext),
                        catalog = BundledCatalog(appContext.assets),
                    ) as T
                }
            }
    }
}
