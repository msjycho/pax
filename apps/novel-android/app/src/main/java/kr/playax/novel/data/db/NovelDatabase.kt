package kr.playax.novel.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WorkEntity::class, ChapterEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class NovelDatabase : RoomDatabase() {
    abstract fun writingDao(): WritingDao

    companion object {
        @Volatile
        private var instance: NovelDatabase? = null

        fun get(context: Context): NovelDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NovelDatabase::class.java,
                    "playax_novel.db",
                ).build().also { instance = it }
            }
    }
}
