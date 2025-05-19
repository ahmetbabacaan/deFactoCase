package com.babacan.defactocase.presentation.movie_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.babacan.defactocase.R

@Composable
internal fun ErrorScreen(
    message: String,
    modifier: Modifier = Modifier,
    onRetryClicked: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )

        Button(
            modifier = Modifier.padding(15.dp),
            onClick = onRetryClicked
        ) {
            Text(text = stringResource(id = R.string.error))
        }
    }

}