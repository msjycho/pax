package kr.playax.novel.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class UserWork(
    val id: String,
    val title: String,
    val updatedAtEpochMs: Long,
)

data class UserChapter(
    val id: String,
    val index: Int,
    val title: String,
    val body: String,
    val status: String = "draft",
)

/**
 * In-memory writing store for the scaffold. Replace with Room in M1.
 */
class UserWritingRepository(@Suppress("UNUSED_PARAMETER") context: Context) {
    private val _works = MutableStateFlow<List<UserWork>>(emptyList())
    val works: StateFlow<List<UserWork>> = _works.asStateFlow()

    private val chapters = mutableMapOf<String, MutableList<UserChapter>>()

    suspend fun addWork(title: String) {
        val trimmed = title.trim().ifEmpty { "무제 작품" }
        val work = UserWork(
            id = UUID.randomUUID().toString(),
            title = trimmed,
            updatedAtEpochMs = System.currentTimeMillis(),
        )
        chapters[work.id] = mutableListOf(
            UserChapter(
                id = UUID.randomUUID().toString(),
                index = 1,
                title = "1화",
                body = "",
            ),
        )
        _works.value = _works.value + work
    }

    fun chaptersFor(workId: String): List<UserChapter> =
        chapters[workId]?.sortedBy { it.index }.orEmpty()

    suspend fun upsertChapter(workId: String, chapter: UserChapter) {
        val list = chapters.getOrPut(workId) { mutableListOf() }
        val idx = list.indexOfFirst { it.id == chapter.id }
        if (idx >= 0) list[idx] = chapter else list.add(chapter)
        _works.value = _works.value.map {
            if (it.id == workId) it.copy(updatedAtEpochMs = System.currentTimeMillis()) else it
        }
    }
}
