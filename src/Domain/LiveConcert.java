package Domain;

import java.time.LocalDateTime;

public class LiveConcert {
    private String title;
    private LocalDateTime date;
    private Artist artist;
    private int ticketCount;
    private boolean isTicketRequired;
    private boolean isPremiumOnly;
    private boolean isAvailablePostLive;
    private String eventType;

    public LiveConcert(String title, LocalDateTime date, Artist artist, int ticketCount, boolean isTicketRequired,
                       boolean isPremiumOnly, boolean isAvailablePostLive, String eventType) {
        this.title = title;
        this.date = date;
        this.artist = artist;
        this.ticketCount = ticketCount;
        this.isTicketRequired = isTicketRequired;
        this.isPremiumOnly = isPremiumOnly;
        this.isAvailablePostLive = isAvailablePostLive;
        this.eventType = eventType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public boolean isTicketRequired() {
        return isTicketRequired;
    }

    public void setTicketRequired(boolean ticketRequired) {
        isTicketRequired = ticketRequired;
    }

    public boolean isPremiumOnly() {
        return isPremiumOnly;
    }

    public void setPremiumOnly(boolean premiumOnly) {
        isPremiumOnly = premiumOnly;
    }

    public boolean isAvailablePostLive() {
        return isAvailablePostLive;
    }

    public void setAvailablePostLive(boolean availablePostLive) {
        isAvailablePostLive = availablePostLive;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public boolean checkTicketAvailability() {
        return ticketCount > 0;
    }

    public boolean checkUserAccess(User user) {
        if (isPremiumOnly && !(user instanceof PremiumUser)) {
            return false;
        }
        if (isTicketRequired && ticketCount <= 0) {
            return false;
        }
        return true;
    }

    public boolean authenticateForConcert(User user) {
        boolean hasAccess = checkUserAccess(user);
        if (hasAccess) {
            System.out.println("User " + user.getName() + " authenticated for the concert.");
        } else {
            System.out.println("User " + user.getName() + " does not have access to the concert.");
        }
        return hasAccess;
    }

    public void startConcert() {
        System.out.println("Concert " + title + " is starting!");
    }

    public void endConcert() {
        System.out.println("Concert " + title + " has ended.");
    }

    public boolean replayAvailable() {
        return isAvailablePostLive;
    }
}
