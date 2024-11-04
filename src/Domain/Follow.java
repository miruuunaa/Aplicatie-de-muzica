package Domain;
import java.util.List;

public class Follow {
    private Listener user;
    private Artist artist;

    public Follow(Listener user, Artist artist) {
        this.user = user;
        this.artist = artist;
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

        }
}
