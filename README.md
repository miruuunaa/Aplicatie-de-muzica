# Musik Anwendung/Music Aplication

Die Anwendung namens "MTIFY Music" bietet die Möglichkeit um die beliebtesten Musikgenres aud persönlichen Tops anzuhören mit ein höhsten Qualität.
Bietet die Posibilität an Live-Konzerten teilzunehmen.
Die Vorteile eines Premium-Kontos sind:(trebuie refacut)
-die Kommentare in einer Live-Konzert sind mehr zugänglich für die Artisten;
-exklusiver Zugriff auf die neuen Musikalben/Musikstücke;
-Offline-Abhören;
-Live-Verse-Funktion.

Liste der Funktionalitäten:
1. sortSongsByTitle(int albumId)
        Sortiert alle Songs eines bestimmten Albums nach ihrem Titel alphabetisch.
        Parameter: albumId – die ID des Albums.
        Rückgabewert: Eine sortierte Liste von Songs.
        Fehler: Löst eine Ausnahme aus, wenn das Album nicht gefunden wird.
   
2. filterAlbumsByGenre(int artistId, String genreName)
        Filtert die Alben eines bestimmten Künstlers nach Genre.
        Parameter: artistId – die ID des Künstlers, genreName – der Name des Genres.
        Rückgabewert: Eine Liste von Alben, die dem angegebenen Genre entsprechen, oder eine leere Liste, wenn kein Künstler gefunden wird.
   
3. filterSongsByMinimumDuration(int artistId, float minDuration)
        Filtert Songs eines Künstlers basierend auf einer minimalen Dauer.
        Parameter: artistId – die ID des Künstlers, minDuration – die Mindestlänge eines Songs in Minuten.
        Rückgabewert: Eine Liste von Songs, die die Daueranforderung erfüllen. Gibt eine leere Liste zurück, wenn der Künstler nicht gefunden wird.
   
4. sortAlbumsByReleaseDate(int artistId)
        Sortiert die Alben eines Künstlers nach dem Veröffentlichungsdatum.
        Parameter: artistId – die ID des Künstlers.
        Rückgabewert: Eine sortierte Liste von Alben.
        Fehler: Löst eine Ausnahme aus, wenn der Künstler nicht gefunden wird.
   
5. getArtistsWithMostSongsAndAlbums()
        Berechnet die Gesamtanzahl von Songs und Alben für jeden Künstler und sortiert die Künstler basierend auf der Gesamtzahl.
        Rückgabewert: Eine Liste von Künstlern, sortiert nach der Gesamtzahl von Songs und Alben.
   
6. calculateConcertVIPScore(Listener listener, int concertId)
        Berechnet eine VIP-Punktzahl für einen Zuhörer basierend auf den Eigenschaften eines Live-Konzerts.
        Parameter: listener – der Zuhörer, concertId – die ID des Konzerts.
        Rückgabewert: Eine berechnete Punktzahl, die die VIP-Eignung des Zuhörers bewertet.
        Fehler: Löst eine Ausnahme aus, wenn das Konzert nicht gefunden wird.
   
7. recommend_songs(History userHistory)
        Empfiehlt dem Benutzer Songs basierend auf seiner Hörhistorie und seinen Lieblingskünstlern.
        Parameter: userHistory – die Hörhistorie des Benutzers.
        Rückgabewert: Eine Liste von Songs, die dem Geschmack des Benutzers entsprechen, aber nicht in seiner Historie enthalten sind.
   
8. recommend_artists(History userHistory)
        Empfiehlt Künstler basierend auf den bevorzugten Genres des Benutzers.
        Parameter: userHistory – die Hörhistorie des Benutzers.
        Rückgabewert: Eine Liste von Künstlern, die zum bevorzugten Genre des Benutzers gehören.
   
9. get_top_genres(History userHistory)
        Identifiziert die Top-Genres basierend auf der Hörhistorie des Benutzers.
        Parameter: userHistory – die Hörhistorie des Benutzers.
        Rückgabewert: Eine Liste der Top 5 Genres nach Häufigkeit, sortiert in absteigender Reihenfolge.

InMemory-Repo:
1. create-Methode
Fügt eine neue Entität zum Repository hinzu und weist ihr eine eindeutige id zu.
Die Methode setzt das id-Feld der Entität dynamisch mithilfe von Reflection.
Fehlerbehandlung: Wirft eine RuntimeException, wenn das id-Feld fehlt oder nicht zugänglich ist.

2. get-Methode
Ruft eine Entität anhand ihrer eindeutigen id ab.
Gibt null zurück, wenn keine Entität mit der angegebenen id existiert.

3. read-Methode
Ruft eine Entität anhand ihrer id ab und überprüft deren Existenz.
Wirft eine IllegalArgumentException, wenn die Entität nicht gefunden wird.

4. update-Methode
Ändert eine bestehende Entität im Repository.
Greift mithilfe von Reflection auf das id-Feld zu und ersetzt die Entität mit derselben id.
Fehlerbehandlung:
Wirft eine RuntimeException, wenn das id-Feld fehlt oder nicht zugänglich ist.
Wirft eine IllegalArgumentException, wenn keine Entität mit der angegebenen id existiert.

5. delete-Methode
Entfernt eine Entität aus dem Repository anhand ihrer id.
Gibt eine Erfolgsmeldung aus, wenn die Entität gelöscht wurde.
Wirft eine IllegalArgumentException, wenn die Entität nicht gefunden wird.

6. getAll-Methode
Ruft alle im Repository gespeicherten Entitäten ab.
Gibt eine Map zurück, in der die Schlüssel die ids und die Werte die Entitäten sind.

File-Repo:
1. create-Methode
Zweck: Fügt ein neues Entity (Objekt) zum Repository hinzu und speichert es in einer Datei.
Vorgehensweise:
Weist dem Entity eine eindeutige id zu, indem der currentId-Zähler erhöht wird.
Verwendet Reflektion, um die setId-Methode des Entities aufzurufen und die id zu setzen.
Speichert das Entity im data-Map, wobei die id als Schlüssel verwendet wird.
Ruft saveDataToFile() auf, um das Repository in der Datei zu speichern.
Fehlerbehandlung: Wirft eine IllegalArgumentException, wenn das Entity keine setId-Methode hat.

2. update-Methode
Zweck: Aktualisiert ein bereits vorhandenes Entity im Repository.
Vorgehensweise:
Extrahiert die id des Entities mithilfe der getId-Methode (mit Reflektion).
Überprüft, ob ein Entity mit der angegebenen id im Repository vorhanden ist.
Aktualisiert das Entity im data-Map.
Ruft saveDataToFile() auf, um das aktualisierte Repository zu speichern.
Fehlerbehandlung:
Wirft eine IllegalArgumentException, wenn kein Entity mit der angegebenen id gefunden wird.
Wirft eine IllegalStateException, wenn das Entity keine getId-Methode hat.

3. delete-Methode
Zweck: Entfernt ein Entity aus dem Repository anhand seiner id.
Vorgehensweise:
Überprüft, ob ein Entity mit der angegebenen id im Repository vorhanden ist.
Entfernt das Entity aus dem data-Map.
Ruft saveDataToFile() auf, um die Änderungen zu speichern.
Fehlerbehandlung: Wirft eine IllegalArgumentException, wenn kein Entity mit der angegebenen id gefunden wird.

4. read-Methode
Zweck: Ruft ein Entity anhand seiner id ab.
Vorgehensweise:
Sucht das Entity im data-Map anhand der id.
Gibt das zugehörige Entity zurück oder null, wenn es nicht gefunden wird.

Database-Repo:
1. create Methode
Zweck: Fügt eine neue Entität zum Repository hinzu und speichert sie in der Datenbank.
Schritte:
Erstellt eine INSERT SQL-Abfrage, um die Entität hinzuzufügen.
Verwendet PreparedStatement, um die Werte (z.B.(Album) Titel, Veröffentlichungsdatum, Künstler, Genre) in die Abfrage einzufügen.
Führt die Abfrage aus, um die Entität in der Datenbank zu speichern.
Fehlerbehandlung: Wirft eine RuntimeException, wenn ein SQL-Fehler auftritt.

2. read Methode
Zweck: Ruft eine Entität aus der Datenbank anhand ihrer ID ab.
Schritte:
Erstellt eine SELECT SQL-Abfrage, um die Entität mit der angegebenen ID abzurufen.
Führt die Abfrage aus und extrahiert die Daten mithilfe eines ResultSet.
Gibt die entsprechende Entität zurück, wenn sie gefunden wird, oder null, wenn sie nicht vorhanden ist.
Fehlerbehandlung: Wirft eine RuntimeException, wenn ein SQL-Fehler auftritt.

3. update Methode
Zweck: Aktualisiert eine bestehende Entität im Repository.
Schritte:
Erstellt eine UPDATE SQL-Abfrage, um die Details der Entität zu ändern.
Verwendet PreparedStatement, um die neuen Werte (z.B. Titel, Veröffentlichungsdatum, Künstler, Genre) festzulegen.
Führt die Abfrage aus, um die Entität in der Datenbank zu aktualisieren.
Fehlerbehandlung: Wirft eine RuntimeException, wenn ein SQL-Fehler auftritt.

4. delete Methode
Zweck: Löscht eine Entität aus der Datenbank anhand ihrer ID.
Schritte:
Erstellt eine DELETE SQL-Abfrage, um die Entität mit der angegebenen ID zu entfernen.
Führt die Abfrage aus, um die Entität aus der Datenbank zu löschen.
Fehlerbehandlung: Wirft eine RuntimeException, wenn ein SQL-Fehler auftritt.

5. getAll Methode
Zweck: Ruft alle Entitäten aus der Datenbank ab.
Schritte:
Erstellt eine SELECT * SQL-Abfrage, um alle Entitäten abzurufen.
Führt die Abfrage aus und iteriert über das Ergebnis, um alle Entitäten zu extrahieren.
Gibt eine Liste aller Entitäten zurück.
Fehlerbehandlung: Wirft eine RuntimeException, wenn ein SQL-Fehler auftritt.


#-----------------------------------------------------------------

The application called “MTIFY Music” offers the possibility to listen to the most popular music genres at the highest quality.
Offers the possibility to participate in live concerts.
The advantages of a premium account are: (trebuie refacut)
-The comments in a live concert are more accessible to the artists;
-exclusive access to new music albums/tracks;
-offline listening;
-Live verse function.

List of functionalities:
1. sortSongsByTitle(int albumId)
      Sorts all songs in a specific album alphabetically by their title.
      Parameters: albumId – the ID of the album.
      Returns: A sorted list of songs.
      Error Handling: Throws an exception if the album is not found.
   
2. filterAlbumsByGenre(int artistId, String genreName)
      Filters the albums of a specific artist by genre.
      Parameters: artistId – the artist's ID, genreName – the name of the genre.
      Returns: A list of albums matching the genre, or an empty list if the artist is not found.
   
3. filterSongsByMinimumDuration(int artistId, float minDuration)
      Filters the songs of an artist based on a minimum duration.
      Parameters: artistId – the artist's ID, minDuration – the minimum length of a song in minutes.
      Returns: A list of songs meeting the duration requirement. Returns an empty list if the artist is not found.
   
4. sortAlbumsByReleaseDate(int artistId)
      Sorts an artist's albums by their release date.
      Parameters: artistId – the artist's ID.
      Returns: A sorted list of albums.
      Error Handling: Throws an exception if the artist is not found.
   
5. getArtistsWithMostSongsAndAlbums()
      Calculates the total number of songs and albums for each artist and sorts the artists based on the total count.
      Returns: A list of artists sorted by the total number of songs and albums.
   
6. calculateConcertVIPScore(Listener listener, int concertId)
      Calculates a VIP score for a listener based on the attributes of a live concert.
      Parameters: listener – the listener, concertId – the concert's ID.
      Returns: A calculated score assessing the listener's VIP eligibility.
      Error Handling: Throws an exception if the concert is not found.
   
7. recommend_songs(History userHistory)
      Recommends songs to the user based on their listening history and favorite artists.
      Parameters: userHistory – the user's listening history.
      Returns: A list of songs matching the user's preferences but not present in their history.
   
8. recommend_artists(History userHistory)
      Recommends artists based on the user's preferred genres.
      Parameters: userHistory – the user's listening history.
      Returns: A list of artists belonging to the user's preferred genre.
   
9. get_top_genres(History userHistory)
      Identifies the top genres based on the user's listening history.
      Parameters: userHistory – the user's listening history.
      Returns: A list of the top 5 genres by frequency, sorted in descending order.

InMemory-Repo:
1. create Method
Adds a new entity to the repository and assigns it a unique id.
The method dynamically sets the id field of the entity using reflection.
Error Handling: If the id field does not exist or is inaccessible, a RuntimeException is thrown.

2. get Method
Retrieves an entity by its unique id.
Returns null if the entity with the specified id does not exist.

3. read Method
Fetches an entity by its id and validates its existence.
Throws an IllegalArgumentException if the entity is not found.

4. update Method
Modifies an existing entity in the repository.
Uses reflection to access the id field and replaces the entity with the same id.
Error Handling:
Throws a RuntimeException if the id field is missing or inaccessible.
Throws an IllegalArgumentException if the entity with the specified id is not found.

5. delete Method
Removes an entity from the repository by its id.
Prints a success message upon deletion.
Throws an IllegalArgumentException if the entity is not found.

6. getAll Method
Retrieves all entities in the repository.
Returns a map where keys are the entity ids and values are the entities themselves.
   
File-Repo:
1. create Method
Purpose: Adds a new entity to the repository and persists it in a file.
Steps:
Assigns a unique id to the entity using a counter (currentId).
Uses reflection to dynamically call the entity's setId method and assign the id.
Stores the entity in the data map using the assigned id as the key.
Calls saveDataToFile() to persist the updated repository to the file.
Error Handling: Throws an IllegalArgumentException if the entity lacks a setId method.

2. update Method
Purpose: Updates an existing entity in the repository.
Steps:
Extracts the id of the entity using the getId method (via reflection).
Checks if an entity with the given id exists in the repository.
Updates the entity in the data map.
Calls saveDataToFile() to save the updated repository.
Error Handling:
Throws an IllegalArgumentException if no entity with the given id exists.
Throws an IllegalStateException if the entity lacks a getId method.

3. delete Method
Purpose: Removes an entity from the repository based on its id.
Steps:
Checks if the repository contains an entity with the specified id.
Removes the entity from the data map.
Calls saveDataToFile() to persist the changes.
Error Handling: Throws an IllegalArgumentException if no entity with the given id exists.

4. read Method
Purpose: Retrieves an entity by its id.
Steps:
Looks up the id in the data map.
Returns the corresponding entity, or null if the entity is not found.

Database-Repo:
1. create Method
Purpose: Adds a new entity to the repository and persists it in the database.
Steps:
Constructs an INSERT SQL query to insert the entity's details.
Uses PreparedStatement to set the values (e.g., title, release date, artist, genre) into the query.
Executes the query to add the entity to the database.
Error Handling: Throws a RuntimeException if there is an SQL error.

2. read Method
Purpose: Retrieves an entity from the database based on its ID.
Steps:
Constructs a SELECT SQL query to fetch the entity with the given ID.
Executes the query and extracts the data using a ResultSet.
Returns the corresponding entity if found, or null if not.
Error Handling: Throws a RuntimeException if there is an SQL error.

3. update Method
Purpose: Updates an existing entity in the repository.
Steps:
Constructs an UPDATE SQL query to modify the entity's details.
Uses PreparedStatement to set the new values (e.g.(Album), title, release date, artist, genre).
Executes the query to update the entity in the database.
Error Handling: Throws a RuntimeException if there is an SQL error.

4. delete Method
Purpose: Deletes an entity from the database based on its ID.
Steps:
Constructs a DELETE SQL query to remove the entity with the given ID.
Executes the query to delete the entity from the database.
Error Handling: Throws a RuntimeException if there is an SQL error.

5. getAll Method
Purpose: Retrieves all entities from the database.
Steps:
Constructs a SELECT * SQL query to fetch all entities.
Executes the query and iterates over the result set to extract all entities.
Returns the list of all entities.
Error Handling: Throws a RuntimeException if there is an SQL error.

