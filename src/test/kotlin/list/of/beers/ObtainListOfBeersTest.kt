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
            println(Request(GET, url))

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
        it("should return a String") {
            assertThat(obtainListOfBeers("")).isInstanceOf(String::class.java)
        }

//        it("should return a list of beer records ") {
//            val fakePubFinderResponse = """{
//              "Pubs": [
//                {"Name": "Phoenix", "RegularBeers": ["Youngs"], "GuestBeers": ["Doom Bar"], "PubService": "phoenixPSS"},
//                {"Name": "Beer House", "RegularBeers": ["Old Speckled Hen"], "PubService": "beerHousePubServiceString"}
//              ]
//            }"""
//
//            val beers = """{
//              "Beers": [
//                {
//                  "Name": "Youngs",
//                  "PubName": "Phoenix",
//                  "PubService": "phoenixPSS",
//                  "RegularBeer": true
//                },
//                {
//                  "Name": "Doom Bar",
//                  "PubName": "14 Palace Street Victoria London SW1E 5JA",
//                  "PubService": "phoenixPSS",
//                  "RegularBeer": false
//                },
//                {
//                  "Name": "Old Speckled Hen",
//                  "PubName": "Beer House",
//                  "PubService": "beerHousePubServiceString",
//                  "RegularBeer": true
//                }
//              ]
//            }"""
//
//            assertThat(obtainListOfBeers(fakePubFinderResponse)).isEqualTo(beers)
//        }
    }
})