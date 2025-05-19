package com.babacan.defactocase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.babacan.defactocase.data.room.DeFactoDAO
import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.presentation.favorite_list.FavoriteListEvent
import com.babacan.defactocase.presentation.favorite_list.FavoriteListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteListViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var deFactoDAO: DeFactoDAO
    private lateinit var viewModel: FavoriteListViewModel

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)
        deFactoDAO = mock(DeFactoDAO::class.java)
        doReturn(listOf<Movie>()).`when`(deFactoDAO).getAllFavoriteMovies()
        viewModel = FavoriteListViewModel(deFactoDAO)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `OnDeleteConfirmed triggers DAO delete and updates state`() = runTest {
        `when`(deFactoDAO.getAllFavoriteMovies()).thenReturn(listOf())
        viewModel.handleEvents(FavoriteListEvent.OnDeleteConfirmed("List1"))
        advanceUntilIdle()
        verify(deFactoDAO).deleteAllFavoriteMoviesByListName("List1")
    }

    @Test
    fun `OnEditConfirmed triggers DAO update and updates state`() = runTest {
        doReturn(listOf<Movie>()).`when`(deFactoDAO).getAllFavoriteMovies()
        doReturn(Unit).`when`(deFactoDAO).updateFavoriteListName("NewList", "OldList")
        viewModel.handleEvents(FavoriteListEvent.OnEditConfirmed("NewList", "OldList"))
        advanceUntilIdle()
        verify(deFactoDAO).updateFavoriteListName("NewList", "OldList")
    }
}