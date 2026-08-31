package eu.kanade.tachiyomi.extension.ar.anime3rb

import eu.kanade.tachiyomi.animesource.model.SAnime
import eu.kanade.tachiyomi.animesource.model.AnimesPage
import eu.kanade.tachiyomi.animesource.model.Episode
import eu.kanade.tachiyomi.animesource.model.Video
import eu.kanade.tachiyomi.animesource.online.AnimeHttpSource
import okhttp3.Response

class Anime3rb : AnimeHttpSource() {

    override val name = "Anime3rb"
    override val baseUrl = "https://anime3rb.com"
    override val lang = "ar"
    override val supportsLatest = true

    override fun popularAnimeParse(response: Response): AnimesPage {
        val document = response.asJsoup()
        val animes = document.select("div.anime-card, div.item").map { element ->
            SAnime.create().apply {
                title = element.select("h3, a.title").text()
                setUrlWithoutDomain(element.select("a").attr("abs:href"))
                thumbnail_url = element.select("img").attr("abs:src")
            }
        }
        return AnimesPage(animes, false)
    }

    override fun episodeListParse(response: Response): List<Episode> {
        val document = response.asJsoup()
        return document.select("ul.episodes-list li, div.episode-card").map { element ->
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