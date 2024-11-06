# Musik Anwendung/Music Aplication

Die Anwendung namens "MTIFY Music" bietet die Möglichkeit um die beliebtesten Musikgenres aud persönlichen Tops anzuhören mit ein höhsten Qualität.
Bietet die Posibilität an Live-Konzerten teilzunehmen.
Die Vorteile eines Premium-Kontos sind:
-die Kommentare in einer Live-Konzert sind mehr zugänglich für die Artisten;
-exklusiver Zugriff auf die neuen Musikalben/Musikstücke;
-Offline-Abhören;
-Live-Verse-Funktion.

1. User (Abstrakte Klasse)
Die User-Klasse ist eine Basisklasse und repräsentiert einen generischen Benutzer im System. Sie enthält Attribute wie name und email zur Speicherung von Benutzerdaten und hat die Methoden login() und logout() zur Verwaltung der Benutzersitzungen.

2. Listener (Erbt von User)
Die Listener-Klasse repräsentiert einen Standardbenutzer, der Musik hört. Zusätzlich zu den geerbten Attributen und Methoden enthält sie eine Liste von Wiedergabelisten (playlists) und eine Methode create_playlist(), um neue Wiedergabelisten zu erstellen.

3. Artist (Erbt von User)
Die Artist-Klasse erweitert die User-Klasse und repräsentiert Musiker, die Inhalte produzieren. Sie enthält das Attribut albums, um Alben zu speichern, und Methoden wie upload_song() und create_album(), um neue Inhalte hinzuzufügen.

4. Song
Die Song-Klasse repräsentiert einen einzelnen Titel mit Attributen wie title, duration, artist und album. Sie implementiert die Playable-Schnittstelle und enthält die Methoden play(), pause() und stop() zur Steuerung der Wiedergabe.

5. Album
Die Album-Klasse repräsentiert ein Musikalbum mit Attributen wie title, release_date, artist und einer Liste von songs. Sie hat eine Methode add_song(), um neue Titel zum Album hinzuzufügen.

6. Playlist
Die Playlist-Klasse repräsentiert eine benutzerdefinierte Wiedergabeliste. Sie enthält Attribute wie name, user und songs. Als Playable-Objekt kann sie die Methoden play(), pause() und stop() verwenden. Zusätzlich gibt es Methoden zum Hinzufügen oder Entfernen von Titeln.

7. Genre
Die Genre-Klasse kategorisiert Songs nach Genres. Sie hat ein Attribut name und eine Methode get_songs(), um eine Liste von Songs im jeweiligen Genre zurückzugeben.

8. Follow
Die Follow-Klasse stellt eine Verbindung zwischen einem Listener und einem Artist her, sodass Benutzer ihre Lieblingsmusiker folgen können. Sie stellt die Methode get_followers() zur Verfügung, um die Liste der Follower eines Künstlers abzurufen.

9. Subscription
Die Subscription-Klasse repräsentiert ein Premium-Abonnement mit Attributen wie type und price. Sie enthält die Methoden upgrade() und cancel(), um Abonnementänderungen zu verwalten.

10. History
Die History-Klasse verfolgt den Wiedergabeverlauf eines Listener und speichert eine Liste von gespielten songs. Sie stellt die Methode add_song_to_history() zur Verfügung, um den Verlauf des Benutzers zu aktualisieren.

11. Playable (Schnittstelle)
Die Playable-Schnittstelle definiert die Methoden play(), pause() und stop(). Klassen wie Song und Playlist implementieren diese Schnittstelle und erlauben so die Steuerung der Wiedergabe.

12. LiveConcert
Die LiveConcert-Klasse repräsentiert ein virtuelles Live-Konzert. Sie umfasst Attribute wie title, date, artist und ticket_count sowie Flags, die angeben, ob Tickets oder Premium-Zugänge erforderlich sind. Methoden wie check_ticket_availability(), check_user_access(), authenticate_for_concert(), start_concert() und end_concert() sowie replay_available() ermöglichen eine Verwaltung und Zugangssteuerung zum Konzert.

13. RecommendationService
Die RecommendationService-Klasse bietet personalisierte Musikempfehlungen basierend auf der Wiedergabehistorie und den bevorzugten Genres eines Nutzers. Sie enthält Methoden wie get_top_genres(), recommend_artists() und recommend_songs().
#-----------------------------------------------------------------------------------------------------------------------------------
The application called “MTIFY Music” offers the possibility to listen to the most popular music genres at the highest quality.
Offers the possibility to participate in live concerts.
The advantages of a premium account are:
-The comments in a live concert are more accessible to the artists;
-exclusive access to new music albums/tracks;
-offline listening;
-Live verse function.

1. User (Abstract Class)
The User class serves as a base class, representing a generic user within the system. It contains attributes like name and email for storing user details. It has methods login() and logout() to manage user sessions.

2. Listener (Inherits from User)
The Listener class represents a regular user who listens to music. In addition to inheriting attributes and methods from User, it contains a list of playlists (playlists) and has a create_playlist() method for creating new playlists.

3. Artist (Inherits from User)
The Artist class extends User, representing musicians who produce content. It includes an albums attribute to store albums and provides methods like upload_song() and create_album() to add music content.

4. Song
The Song class represents an individual track with attributes such as title, duration, artist, and album. It implements the Playable interface with methods play(), pause(), and stop() for managing song playback.

5. Album
The Album class represents a music album, with attributes like title, release_date, artist, and a list of songs. It has an add_song() method to add new songs to the album.

6. Playlist
The Playlist class represents a user-generated playlist of songs. It includes attributes like name, user, and songs. As a Playable object, it can play(), pause(), and stop() playback of the entire playlist. It also has methods to add or remove songs.

7. Genre
The Genre class categorizes songs by genre. It has a name attribute and a get_songs() method that returns a list of songs within the genre.

8. Follow
The Follow class establishes a relationship between a Listener and an Artist, enabling fans to follow their favorite musicians. It provides a get_followers() method to retrieve the list of listeners following the artist.

9. Subscription
The Subscription class represents a premium account option, with attributes like type and price. It has methods upgrade() and cancel() for managing subscription changes.

10. History
The History class tracks the playback history of a Listener, storing a list of songs that have been played. It provides an add_song_to_history() method to update the user’s listening history.

11. Playable (Interface)
The Playable interface defines methods play(), pause(), and stop(). Classes like Song and Playlist implement this interface, allowing these types to be played, paused, or stopped.

12. LiveConcert
The LiveConcert class represents a virtual live concert event. It includes attributes like title, date, artist, and ticket_count, along with flags indicating if tickets or premium access are required. Methods include check_ticket_availability(), check_user_access(), authenticate_for_concert(), start_concert(), and end_concert(), and it can offer replay_available() for post-event viewing.

13. RecommendationService
The RecommendationService class provides personalized music suggestions for a listener based on their history and preferred genres. It has methods like get_top_genres(), recommend_artists(), and recommend_songs().
