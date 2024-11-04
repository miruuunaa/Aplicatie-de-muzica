package Domain;

import java.util.ArrayList;
import java.util.List;

public class Follow {
    private Listener user;
    private Artist artist;
    private static List<Follow> followList = new ArrayList<>();

    public Follow(Listener user, Artist artist) {
        this.user = user;
        this.artist = artist;
        followList.add(this);
    }

    public Listener getUser() {
        return user;
    }

    public void setUser(Listener user) {
        this.user = user;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public static List<User> get_followers(Artist artist) {
        List<User> followers = new ArrayList<>();
        for (Follow follow : followList) {
            if (follow.getArtist().equals(artist)) {
                followers.add(follow.getUser());
            }
        }
        return followers;
    }
}
