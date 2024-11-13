package Domain;
/**
 * The Playable interface defines basic playback functionality
 * for media items such as songs, videos, or other multimedia objects.
 * It includes methods to play, pause, and stop playback.
 */
public interface Playable {
    /**
     * Starts or resumes the playback of the media.
     */
    void play();
    /**
     * Pauses the playback of the media.
     */
    void pause();
    /**
     * Stops the playback of the media and resets it to the beginning.
     */
    void stop();
}
