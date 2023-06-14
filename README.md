Next TO-DO:

Tables

-Artists

ArtistID (Primary Key)
Name
SpotifyProfile (boolean)
PopularityScore
ExternalUrl

-Genres

GenreID (Primary Key)
GenreName

-ArtistGenres

ArtistID (Foreign Key referencing Artists.ArtistID)
GenreID (Foreign Key referencing Genres.GenreID)

-Tracks

TrackID (Primary Key)
TrackName
ArtistID (Foreign Key referencing Artists.ArtistID)

-Events

EventID (Primary Key)
EventName
Date
Location
Price
Link
ImageUrl

- EventArtists

EventID (Foreign Key referencing Events.EventID)
ArtistID (Foreign Key referencing Artists.ArtistID)