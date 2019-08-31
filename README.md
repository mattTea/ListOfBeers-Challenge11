List of Beers - Challenge 11
============================

[Original challenge instructions](https://coding-challenges.jl-engineering.net/challenges/challenge-11/) 

The objective of this challenge is to create a function called `obtainListOfBeers` to convert a json response
listing each pub within an area into json listing different types of beer available in the same area. 

Parse the json returned from pub api to provide a list of beer records for an area.

Beer records should contain the following...

- **Name**: A string showing the name of the beer.
- **PubName**: A string showing the name of the pub serving the beer (called Name on the input json).
- **PubService**: A string copied from the input json.
- **RegularBeer**: Boolean set true if the beer is listed as a `RegularBeer` and false if the beer is listed as a `GuestBeer` on the input json.

A beer may be in more than one pub, in which case add a separate record to the list for each pub the beer is in.

Some pubs will be duplicated in the original json response,
in this case only include the pub with the highest CreateTS (Id and Branch combined form the unique key for each pub).

Sample json can be obtained from https://pubcrawlapi.appspot.com/pubcache/?uId=mike&lng=-0.141499&lat=51.496466&deg=0.003.

`lng`, `lat` and `deg` can be changed in the query param to find pubs in the pubcache in any part of the UK
(`lng` and `lat` are geolocation co-ordinates and `deg` gives a range in degrees with 1 degree equal to approx 70 miles).
