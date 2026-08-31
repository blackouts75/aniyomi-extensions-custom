package eu.kanade.tachiyomi.extension.en.goojara

import eu.kanade.tachiyomi.animesource.model.SAnime
import eu.kanade.tachiyomi.animesource.model.AnimesPage
import eu.kanade.tachiyomi.animesource.model.Episode
import eu.kanade.tachiyomi.animesource.model.Video
import eu.kanade.tachiyomi.animesource.online.AnimeHttpSource
import okhttp3.Response

class Goojara : AnimeHttpSource() {

    override val name = "Goojara"
    override val baseUrl = "https://ww1.goojara.to"
    override val lang = "en"
    override val supportsLatest = true

    override fun popularAnimeParse(response: Response): AnimesPage {
        let document = response.asJsoup()
        val animes = document.select("div.mvi, div.it-m").map { element ->
            SAnime.create().apply {
                title = element.select("strong, a").text()
                setUrlWithoutDomain(element.select("a").attr("abs:href"))
                thumbnail_url = element.select("img").attr("abs:src")
            }
        }
        return AnimesPage(animes, false)
    }

    override fun episodeListParse(response: Response): List<Episode> {
        val document = response.asJsoup()
        return document.select("div.sea, ul.ep-list li").map { element ->
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