package com.babacan.defactocase.presentation.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babacan.defactocase.domain.model.Movie
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.babacan.defactocase.presentation.components.PosterImage

@Composable
fun MovieItem(
    movie: Movie,
    isFavorite: Boolean = false,
    isShowMoveIcon: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: (Movie) -> Unit = {},
    onFavoriteClick: (Movie) -> Unit = {},
    onMoveFavoriteClick: (Movie) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
            .padding(4.dp)
            .clickable { onClick(movie) },
    ) {
        PosterImage(
            posterUrl = movie.poster,
            posterHeight = 94.dp,
            crossFade = true,
        )

        Column {
            Text(
                text = movie.title ?: "-",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                modifier = Modifier
                    .width(80.dp)
                    .padding(start = 4.dp)
            )

            Text(
                text = movie.year ?: "-",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 4.dp)
            )
        }

        Column {
            IconButton(
                onClick = { onFavoriteClick(movie) },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = if (isFavorite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (isShowMoveIcon) {
                IconButton(
                    onClick = { onMoveFavoriteClick(movie) },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

    }
}


@Preview
@Composable
fun Preview() {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            MovieItem(
                modifier = Modifier,
                movie = Movie(
                    imdbID = "1",
                    poster = "https://m.media-amazon.com/images/M/MV5BMmFiZGZjMmEtMTA0Ni00MzA2LTljMTYtZGI2MGJmZWYzZTQ2XkEyXkFqcGc@._V1_SX300.jpg",
                    title = "this is a really long title title title",
                    year = "2024"
                )
            )
        }
    }
}