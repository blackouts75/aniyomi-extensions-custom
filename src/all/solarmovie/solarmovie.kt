package eu.kanade.tachiyomi.extension.en.solarmovie

import eu.kanade.tachiyomi.animesource.model.SAnime
import eu.kanade.tachiyomi.animesource.model.AnimesPage
import eu.kanade.tachiyomi.animesource.model.Episode
import eu.kanade.tachiyomi.animesource.model.Video
import eu.kanade.tachiyomi.animesource.online.AnimeHttpSource
import okhttp3.Response

class SolarMovie : AnimeHttpSource() {

    override val name = "SolarMovie"
    override val baseUrl = "https://vvw2.solarmovie.ma"
    override val lang = "en"
    override val supportsLatest = true

    override fun popularAnimeParse(response: Response): AnimesPage {
        val document = response.asJsoup()
        val animes = document.select("div.flw-item, div.item").map { element ->
            SAnime.create().apply {
                title = element.select("film-name a, h2.film-name a, a.dynamic-name").text()
                setUrlWithoutDomain(element.select("film-name a, h2.film-name a, a.dynamic-name").attr("abs:href"))
                thumbnail_url = element.select("img").attr("abs:src")
            }
        }
        return AnimesPage(animes, false)
    }

    override fun episodeListParse(response: Response): List<Episode> {
        val document = response.asJsoup()
        return document.select("div.detail-seasons div.dropdown-menu a, ul.episodes-list li").map { element ->
            Episode.create().apply {
                name = element.text()
                url = element.attr("abs:href")
            }
        }
    }

    override fun videoListParse(response: Response): List<Video> {
        return emptyList()
    }
}