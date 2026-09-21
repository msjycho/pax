package kr.playax.novel.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "works")
data class WorkEntity(
    @PrimaryKey val id: String,
    val title: String,
    val updatedAtEpochMs: Long,
    val createdAtEpochMs: Long = updatedAtEpochMs,
)

@Entity(
    tableName = "chapters",
    foreignKeys = [
        ForeignKey(
            entity = WorkEntity::class,
            parentColumns = ["id"],
            childColumns = ["workId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("workId"), Index(value = ["workId", "index"], unique = true)],
)
data class ChapterEntity(
    @PrimaryKey val id: String,
    val workId: String,
    val index: Int,
    val title: String,
    val body: String,
    val status: String = "draft",
    val updatedAtEpochMs: Long = System.currentTimeMillis(),
)
