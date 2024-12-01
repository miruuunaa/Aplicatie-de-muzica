package Service;
import Domain.Artist;
import Domain.LiveConcert;
import Domain.Listener;
import Repository.IRepository;

import java.util.ArrayList;
import java.util.Date;
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
            return concert.getAvailableSeats() > 0;
        } else {
            System.out.println("Concert not found.");
            return false;
        }
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
     * Adds a listener as an attendee to a concert, either to the early access list (if they have a premium subscription)
     * or the regular access list (if they have a basic subscription).
     * @param concertId The ID of the concert to which the listener is being added.
     * @param listener The listener to be added as an attendee.
     * @return {@code true} if the listener was successfully added to the appropriate access list (early or regular),
     *         {@code false} if the listener is already on the respective list or if there are any issues (e.g., concert or listener not found).*/
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
        if (listener.hasPremiumSubscription()) {
            if (concert.getEarlyAccessList().contains(listener)) {
                System.out.println("Listener is already in the early access list.");
                return false;
            }
            concert.getEarlyAccessList().add(listener);
            System.out.println(listener.getName() + " has been added to the early access list.");
        } else {
            if (concert.getRegularAccesList().contains(listener)) {
                System.out.println("Listener is already in the regular access list.");
                return false;
            }
            concert.getRegularAccesList().add(listener);
            System.out.println(listener.getName() + " has been added to the regular access list.");
        }
        return true;
    }

    /**
     * Displays the list of attendees for a given concert by its title, categorized into early access and regular access attendees.
     * The method prints the names of listeners who have access to the concert based on their subscription type:
     * those with premium subscriptions are listed under early access, and those with basic subscriptions are listed under regular access.
     * If a list is empty, a message is displayed indicating no attendees in that category.
     * @param concertTitle The title of the concert for which the attendees are being displayed.*/
    public void showAttendees(String concertTitle) {
        LiveConcert concert = getConcertByTitle(concertTitle);

        if (concert != null) {
            List<Listener> earlyAccessList = concert.getEarlyAccessList();
            if (earlyAccessList != null && !earlyAccessList.isEmpty()) {
                System.out.println("Early access attendees for the concert " + concert.getTitle() + ":");
                for (Listener attendee : earlyAccessList) {
                    System.out.println("- " + attendee.getName());
                }
            } else {
                System.out.println("No early access attendees for this concert.");
            }
            List<Listener> regularList = concert.getRegularAccesList();
            if (regularList != null && !regularList.isEmpty()) {
                System.out.println("Regular access attendees for the concert " + concert.getTitle() + ":");
                for (Listener attendee : regularList) {
                    System.out.println("- " + attendee.getName());
                }
            } else {
                System.out.println("No regular access attendees for this concert.");
            }
        } else {
            System.out.println("Concert not found.");
        }
    }

    /**
     * Calculates the VIP score for a listener attending a concert.
     *
     * @param listener The listener for whom to calculate the score.
     * @param concertId The ID of the concert.
     * @return The calculated VIP score.
     * @throws IllegalArgumentException If the concert is not found.
     */
    public double calculateConcertVIPScore(Listener listener, int concertId) {
        LiveConcert concert = concertRepository.get(concertId);
        if (concert == null) {
            throw new IllegalArgumentException("Concert not found.");
        }
        double score = 0;
        if (listener.hasPremiumSubscription()) {
            score += 30;
        }else{
            score+=10;
        }

        if (concert.isArtistFamous()) {
            score += 50;
        }

        if (concert.getAvailableSeats() < 50) {
            score -= 20;
        }

        if (concert.getTicketsSold() >= 5) {
            score += 5;
        }

        return score;
    }

    public void addConcert(LiveConcert concert){
        concertRepository.create(concert);
    }

}
