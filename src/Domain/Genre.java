package Domain;
import java.util.ArrayList;
import java.util.List;

public class Genre {
    private String name;

    public  Genre(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs(){
        return null;
    }

}
