package com.babacan.defactocase.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.babacan.defactocase.R
import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.presentation.components.LoadingContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    navigateBack: () -> Unit,
    navigateSettings: () -> Unit,
    navigateFavoriteList: () -> Unit,
    navigateToMovieDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()
    viewModel.setEvent(HomeEvent.Init)
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateBack -> navigateBack()
                is HomeEffect.NavigateToMovieDetail -> navigateToMovieDetail(effect.movieId)
                HomeEffect.NavigateToFavoriteList -> navigateFavoriteList()
                HomeEffect.NavigateToSettings -> navigateSettings()
            }
        }
    }

    HomeScreen(
        viewState = viewState,
        onViewEvent = viewModel::setEvent
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewState: HomeState,
    onViewEvent: (HomeEvent) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding(),
                    )
            ) {
                Row(
                    Modifier
                        .align(Alignment.End)
                        .padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { onViewEvent(HomeEvent.OnFavoriteClicked) },
                        tint = MaterialTheme.colorScheme.onBackground,
                    )

                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { onViewEvent(HomeEvent.OnSettingsClicked) },
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }

                Column {
                    SearchBar(
                        isLoading = viewState.isLoading,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        searchText = viewState.searchText,
                        onValueChanged = {
                            onViewEvent(HomeEvent.OnSearchTextChanged(it))
                        },
                        onDoneClicked = {
                            onViewEvent(HomeEvent.OnSearchDoneClicked(it))
                        }
                    )
                    LazyRow(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        items(viewState.searchResultList.size) { index ->
                            AssistChip(
                                onClick = {
                                    onViewEvent(HomeEvent.OnSearchTextChanged(viewState.searchResultList[index]))
                                },
                                label = { Text(viewState.searchResultList[index]) },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }


                    if (viewState.pagingDataFlow.collectAsLazyPagingItems().itemCount > 0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Button(
                                onClick = {
                                    onViewEvent(HomeEvent.OnFilterClicked)
                                }, modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                            ) {
                                Text(stringResource(id = R.string.filter))
                            }

                            Button(
                                onClick = {
                                    onViewEvent(HomeEvent.OnSortClicked)
                                }, modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                            ) {
                                Text(stringResource(id = R.string.sort_by))
                            }
                        }
                    }
                }
            }

        }
    ) { innerPadding ->

        if (viewState.showAddToFavoriteDialog.first && viewState.showAddToFavoriteDialog.second != null) {
            val favorites = viewState.favoriteItemList.map { it.listName }.distinct()
            val listNames = if (favorites.isEmpty()) {
                persistentListOf(stringResource(id = R.string.favorites))
            } else {
                if (favorites.contains(stringResource(id = R.string.favorites))) {
                    favorites.toImmutableList()
                } else {
                    listOf(stringResource(id = R.string.favorites), *favorites.toTypedArray()).toImmutableList()
                }
            }
            SelectOrCreateFavoriteListDialog(
                movie = viewState.showAddToFavoriteDialog.second!!,
                favoriteList = listNames,
                onDismiss = {
                    onViewEvent(HomeEvent.OnDismissAddToFavoriteDialog)
                },
                onCreateList = {
                    onViewEvent(HomeEvent.OnMovieAddToFavorite(it))
                }
            )
        }

        if (viewState.showSortBottomSheet) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )
            SortBottomSheet(
                onSortByYear = {
                    onViewEvent(HomeEvent.OnSortByYear)
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                },
                onSortByTitle = {
                    onViewEvent(HomeEvent.OnSortByTitle)
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                },
                onDismiss = {
                    onViewEvent(HomeEvent.OnDismissSortBottomSheet)
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                },
                sheetState = sheetState,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            val pagingData = viewState.pagingDataFlow.collectAsLazyPagingItems()
            val isLoading =
                pagingData.loadState.refresh is LoadState.Loading && viewState.searchText.isNotBlank()
            val isEmpty =
                pagingData.loadState.refresh is LoadState.NotLoading && pagingData.itemCount == 0 && viewState.searchText.isNotBlank()


            if (viewState.searchText.isBlank() || isEmpty) {
                val text = if (isEmpty) {
                    stringResource(id = R.string.search_no_results)
                } else {
                    stringResource(id = R.string.search_welcome)
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )

                }
            }

            LoadingContent(isLoading) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                    columns = GridCells.Fixed(2),
                ) {
                    items(pagingData.itemCount) { index ->
                        val movie = pagingData[index] ?: return@items
                        val isFavorite =
                            viewState.favoriteItemList.map { it.imdbID }.contains(movie.imdbID)
                        MovieItem(
                            movie = movie,
                            isFavorite = isFavorite,
                            onClick = { onViewEvent(HomeEvent.OnMovieDetailClicked(it.imdbID)) },
                            onFavoriteClick = {
                                onViewEvent(HomeEvent.OnMovieFavoriteClicked(movie))
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onValueChanged: (String) -> Unit,
    onDoneClicked: (String) -> Unit = {},
    isLoading: Boolean
) {
    OutlinedTextField(
        value = searchText,
        singleLine = true,
        onValueChange = onValueChanged,
        keyboardActions = KeyboardActions(
            onDone = { onDoneClicked(searchText) }
        ),
        label = { Text(text = stringResource(id = R.string.search_hint)) },
        leadingIcon = {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    color = Color.Gray,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
            }
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .clickable { onValueChanged("") }
                        .size(25.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(
                bottom = 16.dp
            )
    )
}

@Composable
fun SelectOrCreateFavoriteListDialog(
    movie: Movie,
    favoriteList: ImmutableList<String>,
    onDismiss: () -> Unit,
    onCreateList: (Movie) -> Unit,
) {
    val textState = remember { mutableStateOf("") }
    val selectedList = remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDismiss,
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

            TextField(
                value = textState.value,
                onValueChange = {
                    textState.value = it
                    if (it.isNotEmpty()) {
                        selectedList.value = ""
                    }
                },
                label = { Text(text = stringResource(id = R.string.add_favorite_list)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                    )
                }
            )

            LazyColumn {
                items(favoriteList.size) { index ->
                    SelectableFavoriteList(
                        listName = favoriteList[index],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        isChecked = selectedList.value == favoriteList[index],
                        onCheckBoxSelected = {
                            if (textState.value.isNotEmpty()) {
                                textState.value = ""
                            }
                            if (selectedList.value == it) {
                                selectedList.value = ""
                            } else {
                                selectedList.value = it
                            }
                        }
                    )
                }
            }

            val buttonText = if (textState.value.isNotEmpty()) {
                stringResource(id = R.string.add_favorite_list_message)
            } else {
                stringResource(id = R.string.select_favorite_list)
            }
            Text(
                text = buttonText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clip(
                        MaterialTheme.shapes.large
                    )
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        if (textState.value.isNotEmpty()) {
                            onCreateList(movie.copy(listName = textState.value))
                        } else {
                            onCreateList(movie.copy(listName = selectedList.value))
                        }
                        onDismiss()
                    },
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun SelectableFavoriteList(
    modifier: Modifier = Modifier,
    listName: String,
    isChecked: Boolean = false,
    onCheckBoxSelected: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onCheckBoxSelected(listName)
            },
            modifier = Modifier.size(25.dp)
        )
        Text(
            text = listName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    modifier: Modifier = Modifier,
    onSortByYear: () -> Unit,
    onSortByTitle: () -> Unit,
    onDismiss: () -> Unit = {},
    sheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.sort_by),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(id = R.string.sort_by_year),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(8.dp)
                    .clickable { onSortByYear() }
            )
            Text(
                text = stringResource(id = R.string.sort_by_title),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(8.dp)
                    .clickable { onSortByTitle() }
            )
        }
    }
}


@Preview
@Composable
private fun SelectOrCreateFavoriteListDialogPreview() {
    SelectOrCreateFavoriteListDialog(
        movie = Movie(
            imdbID = "1",
            title = "Movie Title",
            poster = "https://example.com/poster.jpg",
            year = "2023"
        ),
        favoriteList = persistentListOf("List 1", "List 2", "List 3"),
        onDismiss = { },
        onCreateList = { }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        viewState = HomeState(
            isLoading = false,
            searchText = "Harry Potter",
            showAddToFavoriteDialog = Pair(false, null),
            favoriteItemList = persistentListOf()
        ),
        onViewEvent = { }
    )
}
