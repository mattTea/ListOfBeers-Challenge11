package list.of.beers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response

internal fun obtainListOfBeers(pubFinder: Response): String {

    val beers = mutableListOf<Beer>()

    // 1. Deserialise the response from pub api (get certain fields back from response body json string)
    val returnedPubs = jacksonObjectMapper().readValue(pubFinder.bodyString(), PubsInArea::class.java)

    if (returnedPubs.pubs == null) return "{}"

    // 2. Construct a Beers object with required fields
    returnedPubs.pubs.map { pub ->
        val regularBeersList = pub.regularBeers?.toTypedArray() ?: emptyArray()
        val guestBeersList = pub.guestBeers?.toTypedArray() ?: emptyArray()

        val beersList = (regularBeersList + guestBeersList).toList()
        beers.addAll(beersList.map {
            Beer(
                name = it,
                pubName = pub.name,
                pubService = pub.pubService,
                regularBeer = pub.regularBeers?.contains(it) ?: false
            )
        })
    }

    return jacksonObjectMapper().writeValueAsString(Beers(beers))
}

internal fun pubFinder(url: String): Response {
    val client = OkHttp()

    return client(Request(GET, url))
}

internal class Beers(val beers: List<Beer>)

internal data class Beer(
    val name: String,
    val pubName: String,
    val pubService: String,
    val regularBeer: Boolean?
)

internal data class Pub(
    val name: String,
    val regularBeers: List<String>?,
    val guestBeers: List<String>?,
    val pubService: String
)

internal data class PubsInArea(
    val pubs: List<Pub>?
)