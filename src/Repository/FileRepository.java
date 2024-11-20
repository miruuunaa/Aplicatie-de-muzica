package Repository;

import Domain.Artist;
import Domain.Album;
import Domain.Song;
import Domain.Listener;
import Domain.Subscription;
import Domain.LiveConcert;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FileRepository<T> implements IRepository<T> {
    private final String filePath;
    private final Map<Integer, T> data = new HashMap<>();
    private int currentId = 1;

    public FileRepository(String filePath) {
        this.filePath = filePath;
        loadDataFromFile();
    }

    @Override
    public void create(T obj) {
        int id = currentId++;

        try {
            obj.getClass().getMethod("setId", int.class).invoke(obj, id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Object must have a setId() method.", e);
        }

        data.put(id, obj);
        saveDataToFile();
    }

    @Override
    public T get(int id) {
        return data.get(id);
    }

    @Override
    public void update(T obj) {
        int id = extractId(obj);
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("No object with ID " + id + " exists.");
        }
        data.put(id, obj);
        saveDataToFile();
    }

    @Override
    public void delete(int id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("No object with ID " + id + " exists.");
        }
        data.remove(id);
        saveDataToFile();
    }

    @Override
    public Map<Integer, T> getAll() {
        return new HashMap<>(data);
    }

    @Override
    public T read(int id) {
        return data.get(id);
    }

    private int extractId(T obj) {
        try {
            return (int) obj.getClass().getMethod("getId").invoke(obj);
        } catch (Exception e) {
            throw new IllegalStateException("Object must have a getId() method.", e);
        }
    }


    private void loadDataFromFile() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] dataFields = line.split(",");
                if (dataFields.length > 1) {
                    int id = Integer.parseInt(dataFields[0]);
                    String name = dataFields[1];

                    if (filePath.contains("artists.csv")) {
                        String email = (dataFields.length > 2) ? dataFields[2] : "";
                        Artist artist = new Artist(name, email);
                        artist.setId(id);
                        data.put(id, (T) artist);

                    } else if (filePath.contains("albums.csv")) {
                        String title = dataFields[1];

                        LocalDate releaseDate = null;
                        try {
                            releaseDate = LocalDate.parse(dataFields[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format for album: " + dataFields[2]);
                            continue;
                        }

                        if (dataFields.length > 3) {
                            int artistId = Integer.parseInt(dataFields[3]);
                            Artist artist = findArtistById(artistId);

                            Album album = new Album(title, releaseDate, artist);
                            album.setId(id);
                            data.put(id, (T) album);
                        }

                    } else if (filePath.contains("songs.csv")) {
                        String title = dataFields[1];

                        float duration = 0;
                        try {
                            duration = Float.parseFloat(dataFields[2]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid duration format for song: " + dataFields[2]);
                            continue;
                        }

                        if (dataFields.length > 3) {
                            int albumId = Integer.parseInt(dataFields[3]);
                            Album album = findAlbumById(albumId);

                            Song song = new Song(title, duration, album);
                            song.setId(id);
                            data.put(id, (T) song);
                        }

                    } else if (filePath.contains("listeners.csv")) {
                        String email = (dataFields.length > 2) ? dataFields[2] : "";
                        Listener listener = new Listener(name, email);
                        listener.setId(id);
                        data.put(id, (T) listener);

                    } else if (filePath.contains("subscriptions.csv")) {
                        String type = dataFields[1];
                        float price = 0;
                        try {
                            price = Float.parseFloat(dataFields[2]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid price format for subscription: " + dataFields[2]);
                            continue;
                        }
                        if (dataFields.length > 3) {
                            int listenerId = Integer.parseInt(dataFields[3]);
                            Listener listener = findListenerById(listenerId);

                            Subscription subscription = new Subscription(type, price, listener);
                            subscription.setId(id);
                            data.put(id, (T) subscription);
                        }

                    } else if (filePath.contains("liveconcerts.csv")) {
                        String title = dataFields[1];
                        Date date = sdf.parse(dataFields[2]);
                        if (dataFields.length > 3) {
                            int artistId = Integer.parseInt(dataFields[3]);
                            Artist artist = findArtistById(artistId);
                            int ticketCount = Integer.parseInt(dataFields[4]);
                            boolean isAvailablePostLive = Boolean.parseBoolean(dataFields[5]);
                            String eventType = dataFields[6];

                            LiveConcert liveConcert = new LiveConcert(title, date, artist, ticketCount, isAvailablePostLive, eventType);
                            liveConcert.setId(id);
                            data.put(id, (T) liveConcert);
                        }
                    }

                    if (id >= currentId) {
                        currentId = id + 1;
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    private void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T obj : data.values()) {
                if (obj instanceof Artist) {
                    Artist artist = (Artist) obj;
                    writer.write(artist.getId() + "," + artist.getName() + "," + artist.getEmail() + "\n");
                } else if (obj instanceof Album) {
                    Album album = (Album) obj;
                    writer.write(album.getId() + "," + album.getTitle() + "," + album.getGenre() + "\n");
                } else if (obj instanceof Song) {
                    Song song = (Song) obj;
                    writer.write(song.getId() + "," + song.getTitle() + "," + song.getDuration() + "\n");
                } else if (obj instanceof Listener) {
                    Listener listener = (Listener) obj;
                    writer.write(listener.getId() + "," + listener.getName() + "," + listener.getEmail() + "\n");
                } else if (obj instanceof Subscription) {
                    Subscription subscription = (Subscription) obj;
                    writer.write(subscription.getId() + "," + subscription.getUser() + "," + subscription.getType() + "\n");
                } else if (obj instanceof LiveConcert) {
                    LiveConcert liveConcert = (LiveConcert) obj;
                    writer.write(liveConcert.getId() + "," + liveConcert.getTitle() + "," + liveConcert.getDate() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Artist findArtistById(int artistId) {
        for (T obj : data.values()) {
            if (obj instanceof Artist) {
                Artist artist = (Artist) obj;
                if (artist.getId() == artistId) {
                    return artist;
                }
            }
        }
        return null;
    }

    private Album findAlbumById(int albumId) {
        for (T obj : data.values()) {
            if (obj instanceof Album) {
                Album album = (Album) obj;
                if (album.getId() == albumId) {
                    return album;
                }
            }
        }
        return null;
    }

    private Listener findListenerById(int listenerId) {
        for (T obj : data.values()) {
            if (obj instanceof Listener) {
                Listener listener = (Listener) obj;
                if (listener.getId() == listenerId) {
                    return listener;
                }
            }
        }
        return null;
    }
}
