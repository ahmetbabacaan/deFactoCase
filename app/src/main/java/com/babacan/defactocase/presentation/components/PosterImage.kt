package com.babacan.defactocase.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.babacan.defactocase.R

@Composable
fun PosterImage(
    posterUrl: String?,
    posterHeight: Dp,
    modifier: Modifier = Modifier,
    crossFade: Boolean
) {
    var placeholderShown by remember { mutableStateOf(true) }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(posterUrl)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .crossfade(crossFade)
            .build(),
        onSuccess = { placeholderShown = false },
        contentScale = ContentScale.Crop,
        contentDescription = "Movie poster",
        modifier = modifier.then(
            Modifier
                .conditional(placeholderShown) {
                    border(4.dp, Color.Gray)
                }
                .height(posterHeight)
                .aspectRatio(120f / 160f)
        )
    )
}