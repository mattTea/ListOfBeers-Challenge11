package list.of.beers

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ObtainListOfBeersTest : Spek({
    describe("GET /pubcrawlapi") {
        val client = OkHttp()
        val url = "https://pubcrawlapi.appspot.com/pubcache/?uId=mike&lng=-0.141499&lat=51.496466&deg=0.003"

        it("should return a success response") {
            val response = client(Request(GET, url))

            assertThat(response.status).isEqualTo(OK)
        }

        it("should return json string containing 'Pubs' array") {
            val response = client(Request(GET, url))

            assertThat(response.bodyString()).contains("Pubs")
        }
    }

    describe("pubFinder") {
        val url = "https://pubcrawlapi.appspot.com/pubcache/?uId=mike&lng=-0.141499&lat=51.496466&deg=0.003"

        it("should return a success response") {
            assertThat(pubFinder(url).status).isEqualTo(OK)
        }

        it("should return json string containing 'Pubs' array") {
            assertThat(pubFinder(url).bodyString()).contains("Pubs")
        }
    }

    describe("obtainListOfBeers") {

        context("for pub api response with pubs") {

            val fakePubFinderResponse = """{
              "pubs": [
                {"name": "Phoenix", "regularBeers": ["Youngs"], "guestBeers": ["Doom Bar"], "pubService": "phoenixPSS"},
                {"name": "Beer House", "regularBeers": ["Old Speckled Hen"], "pubService": "beerHousePubServiceString"}
              ]
            }"""

            val mockPubFinder = Response(OK)
                .body(fakePubFinderResponse)
                .header("Content-Type", "application/json")

            it("should return a String") {
                assertThat(obtainListOfBeers(mockPubFinder)).isInstanceOf(String::class.java)
            }

            it("should return Beers response") {
                assertThat(obtainListOfBeers(mockPubFinder)).contains("beers")
            }

            it("should return a list of beer records ") {

                val beers = """{
              "beers": [
                {
                  "name": "Youngs",
                  "pubName": "Phoenix",
                  "pubService": "phoenixPSS",
                  "regularBeer": true
                },
                {
                  "name": "Doom Bar",
                  "pubName": "Phoenix",
                  "pubService": "phoenixPSS",
                  "regularBeer": false
                },
                {
                  "name": "Old Speckled Hen",
                  "pubName": "Beer House",
                  "pubService": "beerHousePubServiceString",
                  "regularBeer": true
                }
              ]
            }""".replace("\\s".toRegex(), "")

                assertThat(obtainListOfBeers(mockPubFinder).replace("\\s".toRegex(), "")).isEqualTo(beers)
            }
        }

        context("for pub api response with no pubs") {
            val mockPubFinder = Response(OK)
                .body("""{}""")
                .header("Content-Type", "application/json")

            it("should return empty response when no pubs returned from pub api") {
                assertThat(obtainListOfBeers(mockPubFinder)).isEqualTo("{}")
            }
        }
    }
})