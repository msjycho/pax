package kr.playax.novel.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.playax.novel.data.db.ChapterEntity
import kr.playax.novel.data.db.NovelDatabase
import kr.playax.novel.data.db.WorkEntity
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
 * Room-backed writing store — works/chapters survive process death and app restart.
 */
class UserWritingRepository(context: Context) {
    private val dao = NovelDatabase.get(context).writingDao()

    val works: Flow<List<UserWork>> = dao.observeWorks().map { list ->
        list.map { it.toUserWork() }
    }

    fun observeChapters(workId: String): Flow<List<UserChapter>> =
        dao.observeChapters(workId).map { list -> list.map { it.toUserChapter() } }

    suspend fun chaptersFor(workId: String): List<UserChapter> =
        dao.chaptersFor(workId).map { it.toUserChapter() }

    suspend fun addWork(title: String) {
        val trimmed = title.trim().ifEmpty { "무제 작품" }
        val now = System.currentTimeMillis()
        val workId = UUID.randomUUID().toString()
        val work = WorkEntity(
            id = workId,
            title = trimmed,
            updatedAtEpochMs = now,
            createdAtEpochMs = now,
        )
        val chapter = ChapterEntity(
            id = UUID.randomUUID().toString(),
            workId = workId,
            index = 1,
            title = "1화",
            body = "",
            status = "draft",
            updatedAtEpochMs = now,
        )
        dao.insertWorkWithFirstChapter(work, chapter)
    }

    suspend fun upsertChapter(workId: String, chapter: UserChapter) {
        val now = System.currentTimeMillis()
        dao.upsertChapterAndTouchWork(
            chapter = ChapterEntity(
                id = chapter.id,
                workId = workId,
                index = chapter.index,
                title = chapter.title,
                body = chapter.body,
                status = chapter.status,
                updatedAtEpochMs = now,
            ),
            workUpdatedAt = now,
        )
    }

    private fun WorkEntity.toUserWork() = UserWork(
        id = id,
        title = title,
        updatedAtEpochMs = updatedAtEpochMs,
    )

    private fun ChapterEntity.toUserChapter() = UserChapter(
        id = id,
        index = index,
        title = title,
        body = body,
        status = status,
    )
}
