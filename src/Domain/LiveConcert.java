package Domain;
import java.util.Date;

public class LiveConcert {
    private String title;
    private Date date;
    private Artist artist;
    private int ticket_count;
    private boolean is_ticket_required;
    private boolean is_premium_only;
    private boolean is_available_post_live;
    private String event_type;

    public LiveConcert(String title, Date date, Artist artist, int ticket_count, boolean is_ticket_required, boolean is_premium_only, boolean is_available_post_live, String event_type) {
        this.title = title;
        this.date = date;
        this.artist = artist;
        this.ticket_count = ticket_count;
        this.is_ticket_required = is_ticket_required;
        this.is_premium_only = is_premium_only;
        this.is_available_post_live = is_available_post_live;
        this.event_type = event_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public int getTicket_count() {
        return ticket_count;
    }

    public void setTicket_count(int ticket_count) {
        this.ticket_count = ticket_count;
    }

    public boolean isIs_ticket_required() {
        return is_ticket_required;
    }

    public void setIs_ticket_required(boolean is_ticket_required) {
        this.is_ticket_required = is_ticket_required;
    }

    public boolean isIs_premium_only() {
        return is_premium_only;
    }

    public void setIs_premium_only(boolean is_premium_only) {
        this.is_premium_only = is_premium_only;
    }

    public boolean isIs_available_post_live() {
        return is_available_post_live;
    }

    public void setIs_available_post_live(boolean is_available_post_live) {
        this.is_available_post_live = is_available_post_live;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public boolean check_ticket_availability() {
        return ticket_count > 0;
    }

    public boolean check_user_access(User user) {

        return true;
    }

    public boolean authenticate_for_concert(User user) {

        return true;
    }

    public void start_concert() {

    }

    public void end_concert() {

    }

    public boolean replay_available() {
        return is_available_post_live;
    }
}
