package eu.kanade.tachiyomi.extension.en.streamex

import eu.kanade.tachiyomi.animesource.model.SAnime
import eu.kanade.tachiyomi.animesource.model.AnimesPage
import eu.kanade.tachiyomi.animesource.model.Episode
import eu.kanade.tachiyomi.animesource.model.Video
import eu.kanade.tachiyomi.animesource.online.AnimeHttpSource
import okhttp3.Response

class StreamEx : AnimeHttpSource() {

    override val name = "StreamEx"
    override val baseUrl = "https://streamex.net" // Change to your exact target URL if different
    override val lang = "en"
    override val supportsLatest = true

    override fun popularAnimeParse(response: Response): AnimesPage {
        val document = response.asJsoup()
        val animes = document.select("div.item, div.movie-card").map { element ->
            SAnime.create().apply {
                title = element.select("a.title, h3").text()
                setUrlWithoutDomain(element.select("a").attr("abs:href"))
                thumbnail_url = element.select("img").attr("abs:src")
            }
        }
        return AnimesPage(animes, false)
    }

    override fun episodeListParse(response: Response): List<Episode> {
        val document = response.asJsoup()
        return document.select("ul.episodes-list li, div.episode").map { element ->
            Episode.create().apply {
                name = element.text()
                url = element.select("a").attr("abs:href")
            }
        }
    }

    override fun videoListParse(response: Response): List<Video> {
        return emptyList()
    }
}