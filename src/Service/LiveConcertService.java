package Service;
import Domain.Artist;
import Domain.LiveConcert;
import Domain.Listener;
import Repository.IRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Exceptions.EntityNotFoundException;
import Exceptions.ValidationException;

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
        if (concertRepository == null) {
            throw new ValidationException("Concert repository cannot be null.");
        }
        this.concertRepository = concertRepository;
    }

    /**
     * Retrieves a concert by its title from the repository.
     *
     * @param concertTitle The title of the concert to retrieve.
     * @return The concert with the specified title, or null if no concert is found.
     * @throws EntityNotFoundException if no concert with the specified title is found.
     */
    public LiveConcert getConcertByTitle(String concertTitle) {
        if (concertTitle == null || concertTitle.trim().isEmpty()) {
            throw new ValidationException("Concert title cannot be null or empty.");
        }

        LiveConcert concert = concertRepository.getAll().values().stream()
                .filter(c -> c.getTitle().equalsIgnoreCase(concertTitle))
                .findFirst()
                .orElse(null);

        if (concert == null) {
            throw new EntityNotFoundException("Concert with title " + concertTitle + " not found.");
        }

        return concert;
    }

    /**
     * Starts a concert by changing its status to started.
     *
     * @param concertTitle The title of the concert to start.
     * @throws EntityNotFoundException if the concert is not found.
     */
    public void startConcert(String concertTitle) {
        if (concertTitle == null || concertTitle.trim().isEmpty()) {
            throw new ValidationException("Concert title cannot be null or empty.");
        }

        LiveConcert concert = getAvailableConcerts()
                .stream()
                .filter(c -> c.getTitle().equalsIgnoreCase(concertTitle))
                .findFirst()
                .orElse(null);

        if (concert == null) {
            throw new EntityNotFoundException("Concert not found.");
        }

        concert.setStarted(true);
        System.out.println("The Concert " + concert.getTitle() + " has started.");
    }

    /**
     * Ends a concert by changing its status to ended.
     *
     * @param concertTitle The title of the concert to end.
     * @throws EntityNotFoundException if the concert is not found.
     */
    public void endConcert(String concertTitle) {
        if (concertTitle == null || concertTitle.trim().isEmpty()) {
            throw new ValidationException("Concert title cannot be null or empty.");
        }

        LiveConcert concert = getAvailableConcerts()
                .stream()
                .filter(c -> c.getTitle().equalsIgnoreCase(concertTitle))
                .findFirst()
                .orElse(null);

        if (concert == null) {
            throw new EntityNotFoundException("Concert not found.");
        }

        concert.setEnded(true);
        System.out.println("The Concert " + concert.getTitle() + " has ended.");
    }

    /**
     * Checks if tickets are available for a specific concert.
     *
     * @param concertTitle The title of the concert to check ticket availability.
     * @return true if tickets are available, false otherwise.
     * @throws EntityNotFoundException if the concert is not found.
     */
    public boolean checkTicketAvailability(String concertTitle) {
        if (concertTitle == null || concertTitle.trim().isEmpty()) {
            throw new ValidationException("Concert title cannot be null or empty.");
        }

        LiveConcert concert = getAvailableConcerts()
                .stream()
                .filter(c -> c.getTitle().equalsIgnoreCase(concertTitle))
                .findFirst()
                .orElse(null);

        if (concert == null) {
            throw new EntityNotFoundException("Concert not found.");
        }

        return concert.getAvailableSeats() > 0;
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
     *
     * @param concertId The ID of the concert to which the listener is being added.
     * @param listener The listener to be added as an attendee.
     * @return {@code true} if the listener was successfully added to the appropriate access list (early or regular),
     *         {@code false} if the listener is already on the respective list or if there are any issues (e.g., concert or listener not found).
     * @throws EntityNotFoundException if the concert or listener is not found.
     */
    public boolean addAttendee(int concertId, Listener listener) {
        if (listener == null) {
            throw new EntityNotFoundException("Listener not found.");
        }

        LiveConcert concert = concertRepository.get(concertId);
        if (concert == null) {
            throw new EntityNotFoundException("Concert not found.");
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
     *
     * @param concertTitle The title of the concert for which the attendees are being displayed.
     * @throws EntityNotFoundException if the concert is not found.
     */
    public void showAttendees(String concertTitle) {
        LiveConcert concert = getConcertByTitle(concertTitle);

        List<Listener> earlyAccessList = concert.getEarlyAccessList();
        if (earlyAccessList != null && !earlyAccessList.isEmpty()) {
            System.out.println("Early access attendees for the concert " + concert.getTitle() + ":");
            earlyAccessList.forEach(attendee -> System.out.println("- " + attendee.getName()));
        } else {
            System.out.println("No early access attendees for this concert.");
        }

        List<Listener> regularList = concert.getRegularAccesList();
        if (regularList != null && !regularList.isEmpty()) {
            System.out.println("Regular access attendees for the concert " + concert.getTitle() + ":");
            regularList.forEach(attendee -> System.out.println("- " + attendee.getName()));
        } else {
            System.out.println("No regular access attendees for this concert.");
        }
    }

    /**
     * Calculates the VIP score for a listener attending a concert.
     *
     * @param listener The listener for whom to calculate the score.
     * @param concertId The ID of the concert.
     * @return The calculated VIP score.
     * @throws EntityNotFoundException if the concert is not found.
     */
    public double calculateConcertVIPScore(Listener listener, int concertId) {
        LiveConcert concert = concertRepository.get(concertId);
        if (concert == null) {
            throw new EntityNotFoundException("Concert not found.");
        }

        double score = listener.hasPremiumSubscription() ? 30 : 10;

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

    /**
     * Adds a new concert to the repository.
     *
     * @param concert The concert to be added.
     */
    public void addConcert(LiveConcert concert) {
        if (concert == null) {
            throw new ValidationException("Concert cannot be null.");
        }
        concertRepository.create(concert);
    }
}
