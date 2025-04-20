package com.pickflo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.Movie;

@Repository
public interface HomeRecMovieRepository extends JpaRepository<Movie, Long>, SearchQuerydsl {
    
    @Query(value = """
            WITH MovieGenres AS (
                SELECT 
                    m.movie_id,
                    m.movie_title,
                    m.movie_img,
                    m.movie_release_date,
                    GROUP_CONCAT(g.genre_name ORDER BY g.genre_name SEPARATOR ', ') AS genres
                FROM movies m
                JOIN movies_genres mg ON m.movie_id = mg.movie_id
                JOIN genres g ON mg.genre_id = g.genre_id
                GROUP BY m.movie_id, m.movie_title, m.movie_img, m.movie_release_date
            ),
            
            UserMovieGenres AS (
                SELECT 
                    m.movie_id,
                    m.movie_title,
                    m.movie_img,  
                    GROUP_CONCAT(g.genre_name ORDER BY g.genre_name SEPARATOR ', ') AS genres
                FROM users_movies um
                JOIN movies m ON m.movie_id = um.movie_id
                JOIN movies_genres mg ON m.movie_id = mg.movie_id
                JOIN genres g ON mg.genre_id = g.genre_id
                WHERE um.user_id = :userId
                GROUP BY m.movie_id, m.movie_title, m.movie_img
            ),
            
            GenreOverlap AS (
                SELECT 
                    a.movie_id AS movie_id_1,
                    b.movie_id AS movie_id_2,
                    GROUP_CONCAT(g1.genre_name ORDER BY g1.genre_name SEPARATOR ', ') AS overlap_genres,
                    COUNT(DISTINCT g1.genre_name) AS overlap_count
                FROM UserMovieGenres a
                JOIN UserMovieGenres b ON a.movie_id < b.movie_id
                JOIN movies_genres mg1 ON a.movie_id = mg1.movie_id
                JOIN genres g1 ON mg1.genre_id = g1.genre_id
                JOIN movies_genres mg2 ON b.movie_id = mg2.movie_id
                JOIN genres g2 ON mg2.genre_id = g2.genre_id
                WHERE g1.genre_name = g2.genre_name
                GROUP BY a.movie_id, b.movie_id
            ),
            
            MatchingMovies AS (
                SELECT 
                    mg.movie_id,
                    mg.movie_title,
                    mg.movie_img,
                    mg.movie_release_date,
                    mg.genres,
                    LENGTH(mg.genres) - LENGTH(REPLACE(mg.genres, ',', '')) + 1 AS genre_count
                FROM MovieGenres mg
                JOIN GenreOverlap go ON mg.genres = go.overlap_genres
            )
            
            SELECT DISTINCT 
                m.movie_id,
                m.movie_title,
                m.movie_img,
                m.movie_release_date,
                m.genres,
                m.genre_count
            FROM MatchingMovies m
            ORDER BY m.genre_count DESC, m.movie_release_date DESC
            LIMIT :startRow, :pageSize
        """, 
        nativeQuery = true)
    List<Object[]> findMoviesByUserIdAndGenres(@Param("userId") Long userId, @Param("startRow") int startRow, @Param("pageSize") int pageSize);

    @Query(value = """
            SELECT DISTINCT m.movie_id, 
                            m.movie_title, 
                            m.movie_img, 
                            GROUP_CONCAT(DISTINCT p.person_name SEPARATOR ', ') AS people_names
            FROM movies m
            LEFT JOIN movies_people mp ON m.movie_id = mp.movie_id 
            LEFT JOIN people p ON mp.person_id = p.person_id 
            WHERE p.person_id IN (
                SELECT DISTINCT p.person_id
                FROM people p 
                JOIN movies_people mp ON p.person_id = mp.person_id 
                JOIN movies m ON m.movie_id = mp.movie_id 
                JOIN users_movies um ON um.movie_id = m.movie_id 
                WHERE um.user_id = :userId
            )
            GROUP BY m.movie_id
            ORDER BY COUNT(DISTINCT p.person_id) DESC
            LIMIT :startRow, :pageSize
            """, 
            nativeQuery = true)
    List<Object[]> findMoviesByUserIdAndPeople(@Param("userId") Long userId, @Param("startRow") int startRow, @Param("pageSize") int pageSize);
}
