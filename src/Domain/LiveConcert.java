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
    private boolean isAvailablePostLive;
    private String eventType;
    private List<Listener> earlyAccessList; // Premium user
    private List<Listener> regularAccesList; //Basic user
    private boolean started;
    private boolean ended;

    /**
     * Constructs a LiveConcert instance with specified parameters.
     *
     * @param title             The title of the concert
     * @param date              The date of the concert
     * @param artist            The artist performing in the concert
     * @param ticketCount       The number of tickets available for the concert
     * @param isAvailablePostLive   Indicates if the concert is available post-live
     * @param eventType         The type of the concert event (e.g., live show, private event)
     */
    public LiveConcert(String title, Date date, Artist artist, int ticketCount,boolean isAvailablePostLive,String eventType) {
        this.title = title;
        this.date = date;
        this.artist = artist;
        this.ticketCount = ticketCount;
        this.isAvailablePostLive=isAvailablePostLive;
        this.eventType = eventType;
        this.earlyAccessList = new ArrayList<>();
        this.regularAccesList = new ArrayList<>();

    }
    /** Getter and setter methods for accessing and modifying concert attributes **/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Listener> getEarlyAccessList() {
        return earlyAccessList;
    }

    public List<Listener> getRegularAccesList() {
        return regularAccesList;
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

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public void setAvailablePostLive(boolean availablePostLive) {
        this.isAvailablePostLive = availablePostLive;
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

    public int getAvailableSeats() {
        return ticketCount - (earlyAccessList.size() + regularAccesList.size());
    }

    public int getTicketsSold() {
        return earlyAccessList.size() + regularAccesList.size();
    }

    public boolean isArtistFamous() {
        return artist.getFollowers().size() > 6;
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
                ", eventType='" + eventType + '\'' +
                '}';
    }


}
