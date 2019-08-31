## Problem Breakdown

### Convert json response of list of pubs to json listing the beers available in an area. 

1. Call the `pub` api for an area (using `lng`, `lat` and `deg`)
    - Test an api call returns a response 
  
2. Deserialise this response using something like `body.auto<Pub>.toLens()`
    - Test that a `Beer()` record is created from the response
  
3. Create `Beer()` record with the fields needed - `Name`, `PubName`, `PubService`, `RegularBeer`


### Format listing

4. A beer may be in more than one pub, in which case add a separate record to the list for each pub the beer is in.

5. Some pubs will be duplicated in the original json response, in this case only include the pub with the highest CreateTS (Id and Branch combined form the unique key for each pub).
