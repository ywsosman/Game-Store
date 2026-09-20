package org.example.interfaces;

import org.example.enums.Genre;

import java.util.List;

/**
 * Contract for collections that support searching by name and genre.
 *
 * @param <T> the type of elements being searched
 */
public interface Searchable<T> {

    /**
     * Searches for items whose name contains the given keyword (case-insensitive).
     *
     * @param name the keyword to search for
     * @return a list of matching items
     */
    List<T> searchByName(String name);

    /**
     * Searches for items that belong to the specified genre.
     *
     * @param genre the genre to filter by
     * @return a list of matching items
     */
    List<T> searchByGenre(Genre genre);
}
