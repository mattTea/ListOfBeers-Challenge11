package list.of.beers

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ObtainListOfBeersTest : Spek({
    describe("obtainListOfBeers") {
        val client = OkHttp()
        val url = "https://pubcrawlapi.appspot.com/pubcache/?uId=mike&lng=-0.141499&lat=51.496466&deg=0.003"

        it("should GET a success response from pub api") {
            val response = client(Request(GET, url))

            assertThat(response.status).isEqualTo(OK)
        }

        it("should return json string containing 'Pubs' array") {
            val response = client(Request(GET, url))

            assertThat(response.bodyString()).contains("Pubs")
        }
    }
})