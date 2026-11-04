package kr.playax.novel.content

import android.content.res.AssetManager
import org.json.JSONObject

data class CatalogChapter(
    val index: Int,
    val title: String,
    val assetPath: String,
    val sceneTags: List<String>,
    val hasAdultPassages: Boolean,
)

data class CatalogVolume(
    val index: Int,
    val title: String,
    val chapters: List<CatalogChapter>,
)

data class CatalogWork(
    val slug: String,
    val title: String,
    val authorCredit: String,
    val contentRating: String,
    val synopsis: String,
    val origin: String,
    val volumes: List<CatalogVolume>,
)

class BundledCatalog(private val assets: AssetManager) {
    private val cached by lazy { loadManifest() }

    fun works(): List<CatalogWork> = cached

    fun loadChapterMarkdown(workSlug: String, volumeIndex: Int, chapterIndex: Int): String {
        val work = cached.find { it.slug == workSlug } ?: return ""
        val chapter = work.volumes
            .find { it.index == volumeIndex }
            ?.chapters
            ?.find { it.index == chapterIndex }
            ?: return ""
        return assets.open(chapter.assetPath).bufferedReader().use { it.readText() }
    }

    private fun loadManifest(): List<CatalogWork> {
        val root = assets.open("novels/catalog.json").bufferedReader().use { it.readText() }
        val arr = JSONObject(root).getJSONArray("works")
        return buildList {
            for (i in 0 until arr.length()) {
                val w = arr.getJSONObject(i)
                val volumesArr = w.getJSONArray("volumes")
                val volumes = buildList {
                    for (v in 0 until volumesArr.length()) {
                        val vol = volumesArr.getJSONObject(v)
                        val chArr = vol.getJSONArray("chapters")
                        val chapters = buildList {
                            for (c in 0 until chArr.length()) {
                                val ch = chArr.getJSONObject(c)
                                val tagsJson = ch.optJSONArray("sceneTags")
                                val tags = buildList {
                                    if (tagsJson != null) {
                                        for (t in 0 until tagsJson.length()) add(tagsJson.getString(t))
                                    }
                                }
                                add(
                                    CatalogChapter(
                                        index = ch.getInt("index"),
                                        title = ch.getString("title"),
                                        assetPath = ch.getString("assetPath"),
                                        sceneTags = tags,
                                        hasAdultPassages = ch.optBoolean("hasAdultPassages", false),
                                    ),
                                )
                            }
                        }
                        add(
                            CatalogVolume(
                                index = vol.getInt("index"),
                                title = vol.getString("title"),
                                chapters = chapters,
                            ),
                        )
                    }
                }
                add(
                    CatalogWork(
                        slug = w.getString("slug"),
                        title = w.getString("title"),
                        authorCredit = w.getString("authorCredit"),
                        contentRating = w.getString("contentRating"),
                        synopsis = w.getString("synopsis"),
                        origin = w.getString("origin"),
                        volumes = volumes,
                    ),
                )
            }
        }
    }
}

object AdultPassageFilter {
    private val adultBlock = Regex(
        """<!--\s*adult:start\s*-->.*?<!--\s*adult:end\s*-->""",
        setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE),
    )

    fun apply(markdown: String, adultUnlocked: Boolean, hideAdultPassages: Boolean, lockedPlaceholder: String): String {
        if (adultUnlocked && !hideAdultPassages) return markdown
        return adultBlock.replace(markdown, "\n\n*$lockedPlaceholder*\n\n")
    }
}
