package list.of.beers

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ObtainListOfBeersTest : Spek({

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

            it("should return a list of beer records") {

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

            it("should return 2 separate beer records if a beer is available in 2 pubs") {
                val secondFakePubFinderResponse = """{
                  "pubs": [
                    {"name": "Phoenix", "regularBeers": ["Youngs", "Old Speckled Hen"], "guestBeers": ["Doom Bar"], "pubService": "phoenixPSS"},
                    {"name": "Beer House", "regularBeers": ["Old Speckled Hen"], "pubService": "beerHousePubServiceString"}
                  ]
                }"""

                val secondMockPubFinder = Response(OK)
                    .body(secondFakePubFinderResponse)
                    .header("Content-Type", "application/json")

                assertThat(obtainListOfBeers(secondMockPubFinder)).contains(""""name":"Old Speckled Hen","pubName":"Phoenix"""")
                assertThat(obtainListOfBeers(secondMockPubFinder)).contains(""""name":"Old Speckled Hen","pubName":"Beer House"""")
            }

            context("for pub api response with duplicate pubs") {

                val duplicatedPubsPubFinderResponse = """{
                      "pubs": [
                        {"name": "Phoenix", "regularBeers": ["Youngs"], "guestBeers": ["Doom Bar"], "pubService": "phoenixPSS", "id": "16185", "branch": "WLD", "createTS": "2019-05-16 19:31:20"},
                        {"name": "Phoenix", "regularBeers": ["Youngs"], "guestBeers": ["Doom Bar"], "pubService": "phoenixPSS", "id": "16185", "branch": "WLD", "createTS": "2019-08-03 19:31:20"},
                        {"name": "Beer House", "regularBeers": ["Old Speckled Hen"], "pubService": "beerHousePubServiceString", "id": "12345", "branch": "Branch", "createTS": "2019-08-03 19:31:20"}
                      ]
                    }"""

                val duplicatedPubsMockPubFinder = Response(OK)
                    .body(duplicatedPubsPubFinderResponse)
                    .header("Content-Type", "application/json")

                it("should return only one of the duplicate pubs") {
                    val deserializedBeers = jacksonObjectMapper()
                        .readValue(
                            obtainListOfBeers(duplicatedPubsMockPubFinder),
                            Beers::class.java
                        )
                    assertThat(deserializedBeers.beers.count { it.name == "Doom Bar" && it.pubName == "Phoenix" }).isEqualTo(1)
                }
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