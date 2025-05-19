package com.babacan.defactocase.presentation.favorite_list_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.babacan.defactocase.R
import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.presentation.home.MovieItem
import com.babacan.defactocase.presentation.home.SelectableFavoriteList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun FavoriteListDetailRoute(
    navigateBack: () -> Unit,
    listName: String,
    viewModel: FavoriteListDetailViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()

    viewModel.setEvent(FavoriteListDetailEvent.SetListName(listName))

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                FavoriteListDetailEffect.NavigateBack -> navigateBack()
            }
        }
    }

    FavoriteListDetailScreen(
        viewState = viewState,
        onViewEvent = viewModel::setEvent
    )

}

@Composable
fun FavoriteListDetailScreen(
    viewState: FavoriteListDetailState,
    onViewEvent: (FavoriteListDetailEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Image(
                modifier = Modifier
                    .padding(top = 10.dp, start = 16.dp)
                    .clickable { onViewEvent(FavoriteListDetailEvent.OnBackClicked) },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            )
        },
    ) { innerPadding ->

        if (viewState.showMoveFavoriteDialog.first) {
            MoveFavoriteDialog(
                movie = viewState.showMoveFavoriteDialog,
                onDismissRequest = {
                    onViewEvent(
                        FavoriteListDetailEvent.OnDismissMoveFavoriteDialog
                    )
                },
                favoriteList = viewState.favoriteList,
                onConfirmClick = { listName ->
                    onViewEvent(
                        FavoriteListDetailEvent.OnMovieMoveFavoriteConfirmed(
                            listName,
                            viewState.showMoveFavoriteDialog.second,
                        )
                    )
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 16.dp),
        ) {

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                columns = GridCells.Fixed(2),
            ) {
                items(viewState.movies.size) { index ->
                    val movie = viewState.movies[index]
                    val isFavorite =
                        viewState.movies.map { it.imdbID }.map { it }.contains(movie.imdbID)
                    MovieItem(
                        movie = movie,
                        isFavorite = isFavorite,
                        isShowMoveIcon = true,
                        onClick = { onViewEvent(FavoriteListDetailEvent.OnMovieDetailClicked(it.imdbID)) },
                        onFavoriteClick = {
                            onViewEvent(FavoriteListDetailEvent.OnMovieFavoriteClicked(movie))
                        },
                        onMoveFavoriteClick = {
                            onViewEvent(FavoriteListDetailEvent.OnMovieMoveFavoriteClicked(movie))
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun MoveFavoriteDialog(
    movie: Pair<Boolean, Movie?>,
    favoriteList: ImmutableList<String> = persistentListOf(),
    onDismissRequest: () -> Unit,
    onConfirmClick: (String) -> Unit
) {
    val selectedList = remember { mutableStateOf(movie.second?.listName) }
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            LazyColumn {
                items(favoriteList.size) { index ->
                    SelectableFavoriteList(
                        listName = favoriteList[index],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        isChecked = selectedList.value == favoriteList[index],
                        onCheckBoxSelected = {
                            if (selectedList.value == it) {
                                selectedList.value = ""
                            } else {
                                selectedList.value = it
                            }
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Text(stringResource(id = R.string.cancel))
                }

                Button(
                    onClick = { onConfirmClick(selectedList.value.orEmpty()) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    enabled = selectedList.value != movie.second?.listName
                ) {
                    Text(stringResource(id = R.string.edit_favorite_list))
                }
            }
        }
    }
}

@Preview
@Composable
private fun MoveFavoriteDialogPreview() {
    MoveFavoriteDialog(
        movie = Pair(
            true,
            Movie(
                1,
                "Harry potter ve ölüm yadigarları",
                "2023",
                "denmem",
                "https://example.com/poster1.jpg"
            )
        ),
        favoriteList = persistentListOf("Favoriler", "Favoriler 2", "Favoriler 3"),
        onDismissRequest = { },
        onConfirmClick = { }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FavoriteListDetailScreenPreview() {
    FavoriteListDetailScreen(
        viewState = FavoriteListDetailState(
            movies = persistentListOf(
                Movie(
                    imdbID = "1",
                    title = "Movie 1",
                    year = "2023",
                    poster = "https://example.com/poster1.jpg"
                ),
                Movie(
                    imdbID = "2",
                    title = "Movie 2",
                    year = "2023",
                    poster = "https://example.com/poster2.jpg"
                )
            )
        ),
        onViewEvent = { }
    )
}
