package eu.kanade.tachiyomi.extension.en.customstream

import eu.kanade.tachiyomi.animesource.model.AnimesPage
import eu.kanade.tachiyomi.animesource.model.Episode
import eu.kanade.tachiyomi.animesource.model.SAnime
import eu.kanade.tachiyomi.animesource.model.Video
import eu.kanade.tachiyomi.animesource.online.AnimeHttpSource
import okhttp3.Response

/**
 * Boilerplate for the custom streaming source.
 *
 * Replace the selectors in the parser methods when the site's HTML
 * structure is known.
 */
class CustomStream : AnimeHttpSource() {

    override val name = "Custom Stream"
    override val baseUrl = "https://example.com"
    override val lang = "en"
    override val supportsLatest = true

    override fun popularAnimeParse(response: Response): AnimesPage {
        val anime = response.asJsoup().select("a.title, .item a").mapNotNull { element ->
            val title = element.text().trim()
            val url = element.attr("abs:href")
            if (title.isBlank() || url.isBlank()) return@mapNotNull null

            SAnime.create().apply {
                this.title = title
                setUrlWithoutDomain(url)
                thumbnail_url = element.select("img").attr("abs:src")
            }
        }
        return AnimesPage(anime, false)
    }

    override fun episodeListParse(response: Response): List<Episode> =
        response.asJsoup().select("a.episode, .episodes a").map { element ->
            Episode.create().apply {
                name = element.text()
                url = element.attr("abs:href")
            }
        }

    override fun videoListParse(response: Response): List<Video> = emptyList()
}