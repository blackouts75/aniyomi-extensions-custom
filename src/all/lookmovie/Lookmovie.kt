package eu.kanade.tachiyomi.extension.en.lookmovie

import eu.kanade.tachiyomi.animesource.model.SAnime
import eu.kanade.tachiyomi.animesource.model.AnimesPage
import eu.kanade.tachiyomi.animesource.model.Episode
import eu.kanade.tachiyomi.animesource.model.Video
import eu.kanade.tachiyomi.animesource.online.AnimeHttpSource
import okhttp3.Response

class LookMovie : AnimeHttpSource() {

    override val name = "LookMovie"
    override val baseUrl = "https://www.lookmovie2.to"
    override val lang = "en"
    override val supportsLatest = true

    override fun popularAnimeParse(response: Response): AnimesPage {
        val document = response.asJsoup()
        val animes = document.select("div.item-movies, div.movie-item").map { element ->
            SAnime.create().apply {
                title = element.select("a.title, .movie-title").text()
                setUrlWithoutDomain(element.select("a").attr("abs:href"))
                thumbnail_url = element.select("img").attr("abs:src")
            }
        }
        return AnimesPage(animes, false)
    }

    override fun episodeListParse(response: Response): List<Episode> {
        val document = response.asJsoup()
        return document.select("div.episode-item, .seasons-item").map { element ->
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