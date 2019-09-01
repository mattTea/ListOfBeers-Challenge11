package list.of.beers

import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response

internal fun obtainListOfBeers(pubFinderResponseBody: String): String {
    // current failing test as I haven't built the new Beers json response in here yet

    return ""
}

internal fun pubFinder(url: String): Response {
    val client = OkHttp()

    return client(Request(GET, url))
}