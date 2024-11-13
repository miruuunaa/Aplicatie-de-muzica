package Service;
import Domain.LiveConcert;
import Domain.Listener;
import Repository.IRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The LiveConcertService class provides methods for managing live concerts in the system.
 * It allows for adding, retrieving, starting, ending concerts, checking ticket availability,
 * adding attendees to concerts, and showing the list of attendees.
 */
public class LiveConcertService {
    private final IRepository<LiveConcert> concertRepository;


    /**
     * Constructor that initializes the LiveConcertService with the given concert repository.
     *
     * @param concertRepository The repository used to store and manage live concert data.
     */
    public LiveConcertService(IRepository<LiveConcert> concertRepository) {
        this.concertRepository = concertRepository;
    }


    /**
     * Retrieves a concert by its title from the repository.
     *
     * @param concertTitle The title of the concert to retrieve.
     * @return The concert with the specified title, or null if no concert is found.
     */
    public LiveConcert getConcertByTitle(String concertTitle) {
        return concertRepository.getAll().values().stream()
                .filter(concert -> concert.getTitle().equalsIgnoreCase(concertTitle))
                .findFirst()
                .orElse(null);
    }

    /**
     * Starts a concert by changing its status to started.
     *
     * @param concertTitle The title of the concert to start.
     */
    public void startConcert(String concertTitle) {
        LiveConcert concert = getAvailableConcerts()
                .stream()
                .filter(c -> c.getTitle().equalsIgnoreCase(concertTitle))
                .findFirst()
                .orElse(null);

        if (concert != null) {

            concert.setStarted(true);
            System.out.println("The Concert " + concert.getTitle() + " has started.");
        } else {
            System.out.println("Concert not found.");
        }
    }

    /**
     * Ends a concert by changing its status to ended.
     *
     * @param concertTitle The title of the concert to end.
     */
    public void endConcert(String concertTitle) {
        LiveConcert concert = getAvailableConcerts()
                .stream()
                .filter(c -> c.getTitle().equalsIgnoreCase(concertTitle))
                .findFirst()
                .orElse(null);

        if (concert != null) {

            concert.setEnded(true);
            System.out.println("The Concert " + concert.getTitle() + " has ended.");
        } else {
            System.out.println("Concert not found.");
        }
    }

    /**
     * Checks if tickets are available for a specific concert.
     *
     * @param concertTitle The title of the concert to check ticket availability.
     * @return true if tickets are available, false otherwise.
     */
    public boolean checkTicketAvailability(String concertTitle) {
        LiveConcert concert = getAvailableConcerts()
                .stream()
                .filter(c -> c.getTitle().equalsIgnoreCase(concertTitle))
                .findFirst()
                .orElse(null);
        if (concert != null) {
            return concert.checkTicketAvailability();
        } else {
            System.out.println("Concert not found.");
            return false;
        }
    }

    /**
     * Checks if a listener has access to a specific concert.
     *
     * @param listener The listener to check for access.
     * @param concert The concert to check access for.
     * @return true if the listener has access, false otherwise.
     */
    public boolean checkUserAccess(Listener listener, LiveConcert concert) {
        return concert.checkUserAccess(listener);
    }

    /**
     * Retrieves the list of available concerts.
     *
     * @return A list of all available concerts.
     */
    public List<LiveConcert> getAvailableConcerts() {
        return new ArrayList<>(concertRepository.getAll().values());
    }

    /**
     * Adds a listener as an attendee to a concert.
     *
     * @param concertId The ID of the concert.
     * @param listener The listener to be added as an attendee.
     * @return true if the listener was successfully added, false otherwise.
     */
    public boolean addAttendee(int concertId, Listener listener) {
        LiveConcert concert = concertRepository.get(concertId);

        if (concert == null) {
            System.out.println("Concert not found.");
            return false;
        }

        if (listener == null) {
            System.out.println("Listener not found.");
            return false;
        }

        if (concert.getAttendees().contains(listener)) {
            System.out.println("Listener is already an attendee.");
            return false;
        }

        concert.getAttendees().add(listener);
        System.out.println(listener.getName() + " has been added to the concert attendees.");
        return true;
    }

    /**
     * Displays the list of attendees for a given concert by its title.
     *
     * @param concertTitle The title of the concert.
     */
    public void showAttendees(String concertTitle) {
        LiveConcert concert = getConcertByTitle(concertTitle);
        if (concert != null) {
            List<Listener> attendees = concert.getAttendees();
            if (attendees != null && !attendees.isEmpty()) {
                System.out.println("Attendees for the concert " + concert.getTitle() + ":");
                for (Listener attendee : attendees) {
                    System.out.println("- " + attendee.getName());
                }
            } else {
                System.out.println("No attendees for this concert.");
            }
        } else {
            System.out.println("Concert not found.");
        }
    }

}
