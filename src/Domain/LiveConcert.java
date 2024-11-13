package Domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * The LiveConcert class represents a live concert event. It contains information about
 * the concert's title, date, artist, ticket availability, accessibility, and attendance.
 * It provides methods for managing concert attributes, ticket availability, user access,
 * and the start and end of the concert event.
 */
public class LiveConcert {
    private String title;
    private int id;
    private Date date;
    private Artist artist;
    private int ticketCount;
    private boolean isTicketRequired;
    private boolean isPremiumOnly;
    private boolean isAvailablePostLive;
    private String eventType;
    private List<Listener> attendees;  // Publicul care a participat la concert
    private boolean started;
    private boolean ended;
    /**
     * Constructs a LiveConcert instance with specified parameters.
     *
     * @param title             The title of the concert
     * @param date              The date of the concert
     * @param artist            The artist performing in the concert
     * @param ticketCount       The number of tickets available for the concert
     * @param isTicketRequired  Indicates if a ticket is required for the concert
     * @param isPremiumOnly     Indicates if the concert is only accessible to premium users
     * @param isAvailablePostLive Indicates if the concert is available post-live
     * @param eventType         The type of the concert event (e.g., live show, private event)
     */
    public LiveConcert(String title, Date date, Artist artist, int ticketCount, boolean isTicketRequired, boolean isPremiumOnly, boolean isAvailablePostLive, String eventType) {
        this.title = title;
        this.date = date;
        this.artist = artist;
        this.ticketCount = ticketCount;
        this.isTicketRequired = isTicketRequired;
        this.isPremiumOnly = isPremiumOnly;
        this.isAvailablePostLive = isAvailablePostLive;
        this.eventType = eventType;
        this.attendees=new ArrayList<>();

    }
    /** Getter and setter methods for accessing and modifying concert attributes **/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Listener> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Listener> attendees) {
        this.attendees = attendees;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
    /**
     * Checks if tickets are available for the concert.
     *
     * @return True if tickets are available, false otherwise
     */
    public boolean checkTicketAvailability() {
        return ticketCount > 0;
    }
    /**
     * Verifies if the user has access to the concert.
     *
     * @param user The user attempting to access the concert
     * @return True if the user has access, false if they lack the required access level
     */
    public boolean checkUserAccess(User user) {
        if (isPremiumOnly && !(user instanceof PremiumUser)) {
            return false;
        }
        return true;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public void setTicketRequired(boolean ticketRequired) {
        this.isTicketRequired = ticketRequired;
    }

    public void setPremiumOnly(boolean premiumOnly) {
        this.isPremiumOnly = premiumOnly;
    }

    public void setAvailablePostLive(boolean availablePostLive) {
        this.isAvailablePostLive = availablePostLive;
    }

    public boolean isTicketRequired() {
        return isTicketRequired;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public Artist getArtist() {
        return artist;
    }

    public String getEventType() {
        return eventType;
    }

    /**
     * Starts the concert, updating the status and displaying a message.
     */
    public void startConcert() {
        System.out.println("Concert " + title + " started.");
    }
    /**
     * Ends the concert, updating the status and displaying a message.
     */
    public void endConcert() {
        System.out.println("Concert " + title + " ended.");
    }
    /**
     * Checks if the concert replay is available post-live.
     *
     * @return True if replay is available, false otherwise
     */
    public boolean replayAvailable() {
        return isAvailablePostLive;
    }


    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }
    /**
     * Provides a string representation of the concert, including key attributes.
     *
     * @return A string summarizing the concert details
     */
    @Override
    public String toString() {
        return "LiveConcert{" +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", artist=" + artist +
                ", ticketCount=" + ticketCount +
                ", isTicketRequired=" + isTicketRequired +
                ", isPremiumOnly=" + isPremiumOnly +
                ", isAvailablePostLive=" + isAvailablePostLive +
                ", eventType='" + eventType + '\'' +
                ", attendees=" + attendees +
                '}';
    }


}
