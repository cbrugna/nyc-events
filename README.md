- Create an ApiController that handles the API endpoints and annotate with @RestController
    - ApiController will expose the API endpoints that we want to use
- Inject the DatabaseDAO class into the ApiController class with @Autowired -- DatabaseDAO will be responsible for handling all interaction with the databse

- Remember, @Scheduled can periodically execute a class, so I will need to do that in the future when the web application is close to being done to make sure the database is up to date
    - We can possibly do this with a DataUpdateService with @Scheudled
    - Consider doing this before everything is integrated so that we don't have to worry about that anymore

-----

Potential structure (and notes):

Controller Class: Create a new controller class (e.g., ApiController) that handles the API endpoints. Annotate the class with @RestController to indicate that it is responsible for handling web API requests.

DatabaseDAO Integration: Inject the DatabaseDAO into the ApiController class, either by constructor injection or using the @Autowired annotation. The DatabaseDAO will be responsible for interacting with the SQL database.

API Endpoints: Within the ApiController, define methods for the API endpoints you want to expose. These methods should handle the HTTP requests and use the DatabaseDAO to fetch data from the database. You can return the retrieved data in a suitable format (e.g., JSON) as the response.

@RestController
@RequestMapping("/api")
public class ApiController {
    private final DatabaseDAO databaseDAO;

    public ApiController(DatabaseDAO databaseDAO) {
        this.databaseDAO = databaseDAO;
    }

    @GetMapping("/artists")
    public List<Artist> getAllArtists() {
        List<Artist> artists = databaseDAO.getAllArtists();
        return artists;
    }

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        List<Event> events = databaseDAO.getAllEvents();
        return events;
    }

    // Other API endpoints for specific queries or operations

    // ...
}



Dice Scraper: Instead of directly running the DiceScraper class from Main.java, you can create a separate service class (e.g., DiceScraperService) that encapsulates the scraping functionality. The DiceScraperService can call the DiceScraper class and retrieve the scraped data as Artist and Event objects.

Spotify API: Similarly, create a service class (e.g., SpotifyAPIService) that interacts with the SpotifyAPI class to fetch additional data from the Spotify API. The SpotifyAPIService can transform the data into appropriate objects (e.g., Artist) or enrich the existing Artist and Event objects with Spotify-related information.

Database Operations: Instead of directly calling the DatabaseDAO operations from Main.java, move the database operations to a separate service class (e.g., DatabaseService). The DatabaseService can utilize the appropriate DAOs (e.g., ArtistDAO, EventDAO) to save the scraped and enriched data into the database.

The updated structure would look like:

Main.java: Responsible for starting the Spring Boot application and initializing the Spring context.
Controllers: Handle API endpoints and delegate the processing to appropriate service classes.
Service Layer:
DiceScraperService: Calls DiceScraper and retrieves scraped data.
SpotifyAPIService: Interacts with SpotifyAPI and retrieves Spotify-related data.
DatabaseService: Utilizes DAOs to save scraped and enriched data into the database.
DAOs: Interact with the database using Spring Data JPA, perform CRUD operations, and define custom queries if needed.
Model Classes: Represent the entities and their relationships.
