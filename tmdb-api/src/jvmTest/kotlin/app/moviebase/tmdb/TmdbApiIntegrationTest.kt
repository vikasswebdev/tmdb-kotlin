package app.moviebase.tmdb

import app.moviebase.tmdb.model.TmdbMovieStatus
import app.moviebase.tmdb.remote.TmdbException
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

// TODO: Enable test when move into integration test folder + own source set
@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TmdbApiIntegrationTest {

    val tmdb3 = buildTmdb3()

    @Test
    fun `it should fetch movie from TMDB`() = runBlocking {
        val movie = tmdb3.movies.getDetails(429203)

        println("movie: $movie")
        assertThat(movie).isNotNull()
        assertThat(movie.id).isEqualTo(429203)
        assertThat(movie.status).isEqualTo(TmdbMovieStatus.RELEASED)
    }

    @Test
    fun `it throws a TmdbException when the resource is not found on TMDB`() {
        runBlocking {
            assertThrows<TmdbException> {
                tmdb3.movies.getDetails(429203254)
            }
        }
    }

    @Test
    fun `it throws a TmdbException when the API Key is wrong`() {
        runBlocking {
            val invalidTmdb = buildTmdb3("00000000000000000")
            assertThrows<TmdbException> {
                invalidTmdb.movies.getDetails(429203)
            }
        }
    }
}
