## Problem Breakdown

### Convert json response of list of pubs to json listing the beers available in an area. 

1. Call the `pub` api for an area (using `lng`, `lat` and `deg`)
    - Test an api call returns a response 
  
2. Call to `obtainListOfBeers` returns a String

3. Call to `obtainListOfBeers` returns a beer record
    - Write functional (integration) test that calls real api <- **TODO**
    - Mock the response from `pubcrawlapi` so the response is controlled in unit test
        - What I need to do..
          - Inject a 'pubFinder' into `obtainListOfBeers()`
          - Create a real `pubFinder()` that calls the pub api

          - I can then create a `fakePubFinder` and inject that in test
          - The `fakePubFinder` will then return the `fakePubApiResponse`
          - Then `obtainListOfBeers(fakePubFinder)` will return the `Beers` json

 
3. Deserialise pub api response using something like `body.auto<Pub>.toLens()`
    - Test additional fields on `Beer()` record that is created from the response
  
3. Create `Beer()` record with the fields needed - `Name`, `PubName`, `PubService`, `RegularBeer`


### Format listing

4. A beer may be in more than one pub, in which case add a separate record to the list for each pub the beer is in.

5. Some pubs will be duplicated in the original json response, in this case only include the pub with the highest CreateTS (Id and Branch combined form the unique key for each pub).
