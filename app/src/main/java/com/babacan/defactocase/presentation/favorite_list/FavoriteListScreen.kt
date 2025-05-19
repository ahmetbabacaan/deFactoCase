package com.babacan.defactocase.presentation.favorite_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.babacan.defactocase.R
import kotlinx.collections.immutable.persistentListOf

@Composable
fun FavoriteListRoute(
    navigateBack: () -> Unit,
    navigateToFavoriteListDetail: (String) -> Unit,
    viewModel: FavoriteListViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                FavoriteListEffect.NavigateBack -> navigateBack()
                is FavoriteListEffect.NavigateToFavoriteList -> {
                    navigateToFavoriteListDetail(effect.listName)
                }
            }
        }
    }

    FavoriteListScreen(
        viewState = viewState,
        onViewEvent = viewModel::setEvent
    )

}

@Composable
fun FavoriteListScreen(
    viewState: FavoriteListState,
    onViewEvent: (FavoriteListEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Image(
                modifier = Modifier
                    .padding(top = 10.dp, start = 16.dp)
                    .clickable { onViewEvent(FavoriteListEvent.OnBackClicked) },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            )
        },
    ) { innerPadding ->

        if (viewState.showDeleteDialog.first) {
            DeleteSubmitDialog(
                onDismissRequest = { onViewEvent(FavoriteListEvent.OnDeleteCanceled) },
                onDeleteConfirmed = {
                    onViewEvent(FavoriteListEvent.OnDeleteConfirmed(viewState.showDeleteDialog.second))
                },
                onDeleteCanceled = { onViewEvent(FavoriteListEvent.OnDeleteCanceled) }
            )
        }

        if (viewState.showEditDialog.first) {
            EditFavoriteListDialog(
                listName = viewState.showEditDialog.second,
                onDismissRequest = { onViewEvent(FavoriteListEvent.OnEditCanceled) },
                onEditConfirmed = {
                    onViewEvent(
                        FavoriteListEvent.OnEditConfirmed(
                            it,
                            viewState.showEditDialog.second.orEmpty()
                        )
                    )
                },
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 16.dp),
        ) {

            Text(
                text = stringResource(id = R.string.favorite),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp)
            )

            LazyColumn {
                items(viewState.favoriteItemList.size) { index ->
                    val item = viewState.favoriteItemList[index]

                    Box(
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        Text(
                            text = item,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    onViewEvent(FavoriteListEvent.OnFavoriteListClicked(item))
                                }
                                .padding(16.dp),
                        )

                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {
                                    onViewEvent(FavoriteListEvent.OnEditClicked(item))
                                }
                        )

                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 48.dp)
                                .clickable {
                                    onViewEvent(FavoriteListEvent.OnDeleteClicked(item))
                                }
                        )
                    }

                }
            }

        }
    }
}

@Composable
fun DeleteSubmitDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onDeleteCanceled: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(id = R.string.verify_deletion)) },
        text = { Text(stringResource(id = R.string.verify_deletion_message)) },
        confirmButton = {
            Button(onClick = onDeleteConfirmed, modifier = modifier.fillMaxWidth(0.4f)) {
                Text(stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            Button(onClick = onDeleteCanceled, modifier = modifier.fillMaxWidth(0.4f)) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}

@Composable
fun EditFavoriteListDialog(
    listName: String?,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onEditConfirmed: (String) -> Unit,
) {
    val text = remember { mutableStateOf(listName) }
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            TextField(
                value = text.value.orEmpty(),
                onValueChange = { text.value = it },
                placeholder = { Text(stringResource(id = R.string.favorite_list_name)) },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = onDismissRequest, modifier = modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Text(stringResource(id = R.string.cancel))
                }

                Button(
                    onClick = { onEditConfirmed(text.value.orEmpty()) }, modifier = modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Text(stringResource(id = R.string.edit_favorite_list))
                }
            }
        }
    }

}

@Preview
@Composable
private fun EditFavoriteListDialogPreview() {
    EditFavoriteListDialog(
        listName = stringResource(id = R.string.favorite_list),
        onDismissRequest = { },
        onEditConfirmed = { },
    )
}

@Preview
@Composable
private fun DeleteSubmitDialogPreview() {
    DeleteSubmitDialog(
        onDismissRequest = { },
        onDeleteConfirmed = { },
        onDeleteCanceled = { }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FavoriteListScreenPreview() {
    FavoriteListScreen(
        viewState = FavoriteListState(
            favoriteItemList = persistentListOf("Favoriler", "Kesin İzle", "Önemsiz")
        ),
        onViewEvent = { }
    )
}
