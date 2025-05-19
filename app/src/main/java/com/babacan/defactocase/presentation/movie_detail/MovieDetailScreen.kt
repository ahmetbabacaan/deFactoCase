package com.babacan.defactocase.presentation.movie_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.babacan.defactocase.R
import com.babacan.defactocase.presentation.components.LoadingContent
import com.babacan.defactocase.presentation.components.PosterImage

@Composable
fun MovieDetailRoute(
    navigateBack: () -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel(),
    movieId: String,
) {
    val viewState by viewModel.state.collectAsState()
    if (viewState.movieId == null) {
        viewModel.setEvent(MovieDetailEvent.SetMovieId(movieId))
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                MovieDetailEffect.NavigateBack -> navigateBack()
            }
        }
    }

    MovieDetailScreen(
        viewState = viewState,
        onViewEvent = viewModel::setEvent
    )

}

@Composable
fun MovieDetailScreen(
    viewState: MovieDetailState,
    onViewEvent: (MovieDetailEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Image(
                modifier = Modifier
                    .padding(top = 10.dp, start = 16.dp)
                    .clickable { onViewEvent(MovieDetailEvent.OnBackClicked) },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            )
        },
    ) { innerPadding ->
        LoadingContent(viewState.isLoading) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(15.dp)
            ) {

                if (viewState.movieDetail != null) {

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ) {

                        val (poster, title) = createRefs()

                        PosterImage(
                            posterUrl = viewState.movieDetail.poster,
                            posterHeight = 200.dp,
                            modifier = Modifier
                                .constrainAs(poster) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                },
                            crossFade = false
                        )

                        Text(
                            text = viewState.movieDetail.title ?: "-",
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .constrainAs(title) {
                                    start.linkTo(poster.end, margin = 16.dp)
                                    top.linkTo(poster.top)
                                    end.linkTo(parent.end, margin = 16.dp)
                                    width =
                                        androidx.constraintlayout.compose.Dimension.fillToConstraints
                                }
                        )

                    }

                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            modifier = Modifier,
                            text = viewState.movieDetail.year ?: "-",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )

                    }


                    if (viewState.isError) {

                        ErrorScreen(
                            onRetryClicked = {
                                onViewEvent(MovieDetailEvent.OnRetryClicked)
                            },
                            message = viewState.errorMessage ?: stringResource(id = R.string.error),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 30.dp)
                        )
                    }

                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp),
                        text = viewState.movieDetail.genre ?: "-",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 15.dp),
                    ) {

                        RatingText(
                            rating = viewState.movieDetail.imdbRating,
                            iconRes = R.drawable.imdb
                        )

                        RatingText(
                            modifier = Modifier.padding(start = 40.dp),
                            rating = viewState.movieDetail.metascore,
                            iconRes = R.drawable.metacritic
                        )
                    }

                    Text(
                        modifier = Modifier
                            .padding(top = 15.dp),
                        text = stringResource(
                            R.string.votes,
                            viewState.movieDetail.imdbVotes ?: "-"
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 25.dp),
                        text = viewState.movieDetail.plot ?: "",
                        fontSize = 16.sp
                    )

                    CrewText(
                        modifier = Modifier
                            .padding(top = 25.dp),
                        movieDetails = viewState.movieDetail
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MovieDetailScreenPreview() {
    MovieDetailScreen(
        viewState = MovieDetailState(),
        onViewEvent = { }
    )
}
