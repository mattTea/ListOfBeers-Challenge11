package list.of.beers

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ObtainListOfBeersFunctionalTest : Spek({
    val client = OkHttp()
    val url = "https://pubcrawlapi.appspot.com/pubcache/?uId=mike&lng=-0.141499&lat=51.496466&deg=0.003"

    describe("GET /pubcrawlapi") {

        it("should return a success response") {
            val response = client(Request(Method.GET, url))

            assertThat(response.status).isEqualTo(Status.OK)
        }

        it("should return json string containing 'Pubs'") {
            val response = client(Request(Method.GET, url))

            assertThat(response.bodyString()).contains("Pubs")
        }
    }

    describe("obtainListOfBeers") {
        it("should return Beers response") {
            assertThat(obtainListOfBeers(pubFinder(url))).contains("beers")
        }

        it("should return json formatted Beer records") {
            // this test should deserialize, then assert an instance of Beer
            val deserializedBeers = jacksonObjectMapper()
                .readValue(
                    obtainListOfBeers(pubFinder(url)),
                    Beers::class.java
                )

            assertThat(deserializedBeers.beers[0]).isInstanceOf(Beer::class.java)
        }
    }
})