package com.babacan.defactocase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.babacan.defactocase.data.room.DeFactoDAO
import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.presentation.home.HomeEvent
import com.babacan.defactocase.presentation.home.HomeViewModel
import com.babacan.defactocase.presentation.home.SearchPagingSourceFactory
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
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var deFactoDAO: DeFactoDAO

    @Mock
    private lateinit var pagingSourceFactory: SearchPagingSourceFactory

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)
        doReturn(listOf<Movie>()).`when`(deFactoDAO).getAllFavoriteMovies()
        viewModel = HomeViewModel(pagingSourceFactory, deFactoDAO)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateFavorites updates favoriteItemList`() = runTest {
        val movies = listOf(Movie(imdbID = "1", title = "Test", year = "2020", poster = ""))
        `when`(deFactoDAO.getAllFavoriteMovies()).thenReturn(movies)
        viewModel.handleEvents(HomeEvent.Init)
        advanceUntilIdle()
        Assert.assertEquals(movies, viewModel.getCurrentState().favoriteItemList)
    }
}