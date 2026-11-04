package kr.playax.novel.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WritingDao {
    @Query("SELECT * FROM works ORDER BY updatedAtEpochMs DESC")
    fun observeWorks(): Flow<List<WorkEntity>>

    @Query("SELECT * FROM chapters WHERE workId = :workId ORDER BY `index` ASC")
    fun observeChapters(workId: String): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE workId = :workId ORDER BY `index` ASC")
    suspend fun chaptersFor(workId: String): List<ChapterEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWork(work: WorkEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterEntity)

    @Update
    suspend fun updateChapter(chapter: ChapterEntity)

    @Query("UPDATE works SET updatedAtEpochMs = :updatedAt WHERE id = :workId")
    suspend fun touchWork(workId: String, updatedAt: Long)

    @Transaction
    suspend fun insertWorkWithFirstChapter(work: WorkEntity, chapter: ChapterEntity) {
        insertWork(work)
        insertChapter(chapter)
    }

    @Transaction
    suspend fun upsertChapterAndTouchWork(chapter: ChapterEntity, workUpdatedAt: Long) {
        val existing = chaptersFor(chapter.workId).any { it.id == chapter.id }
        if (existing) {
            updateChapter(chapter)
        } else {
            insertChapter(chapter)
        }
        touchWork(chapter.workId, workUpdatedAt)
    }
}
