package com.babacan.defactocase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.babacan.defactocase.data.room.DeFactoDAO
import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.presentation.favorite_list_detail.FavoriteListDetailEvent
import com.babacan.defactocase.presentation.favorite_list_detail.FavoriteListDetailViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoriteListDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var deFactoDAO: DeFactoDAO

    private lateinit var viewModel: FavoriteListDetailViewModel

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)
        doReturn(listOf<Movie>()).`when`(deFactoDAO).getAllFavoriteMovies()
        viewModel = FavoriteListDetailViewModel(deFactoDAO)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `SetListName event updates state with correct movies`() = runTest {
        val listName = "Favorites"
        val movies = listOf(
            Movie(imdbID = "1", title = "Test1", year = "2020", poster = "", listName = listName),
            Movie(imdbID = "2", title = "Test2", year = "2021", poster = "", listName = "Other")
        )
        doReturn(movies).`when`(deFactoDAO).getAllFavoriteMovies()

        viewModel = FavoriteListDetailViewModel(deFactoDAO)

        viewModel.handleEvents(FavoriteListDetailEvent.SetListName(listName))
        advanceUntilIdle()

        val state = viewModel.getCurrentState()
        Assert.assertEquals(listName, state.listName)
        Assert.assertEquals(
            listOf(movies[0]).toImmutableList(),
            state.movies
        )
        Assert.assertTrue(state.favoriteList.contains(listName))
    }

}