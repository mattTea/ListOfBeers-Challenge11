package list.of.beers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response

internal data class Beer(
    val name: String,
    val pubName: String,
    val pubService: String,
    val regularBeer: Boolean?
)

internal data class Beers(val beers: List<Beer>)

internal data class Pub(
    val name: String,
    val regularBeers: List<String>?,
    val guestBeers: List<String>?,
    val pubService: String,
    val id: String?,
    val branch: String?,
    val createTs: String?
)

internal data class PubsInArea(
    val pubs: List<Pub>?
)

internal fun obtainListOfBeers(pubFinder: Response): String {
    val beers = mutableListOf<Beer>()

    val returnedPubs = deserializePubsResponse(pubFinder)

    returnedPubs?.pubs ?: return "{}"

    removePubDuplicates(returnedPubs.pubs).map { pub ->
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

// TODO unit test this newly extracted method
internal fun deserializePubsResponse(pubFinder: Response): PubsInArea? {
    return jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .readValue(
            pubFinder.bodyString(),
            PubsInArea::class.java
        )
}

internal fun removePubDuplicates(pubs: List<Pub>): List<Pub> {
    return pubs
        .sortedByDescending { it.createTs }
        .distinctBy { it.id + it.branch }
}

internal fun pubFinder(url: String): Response {
    val client = OkHttp()
    return client(Request(GET, url))
}