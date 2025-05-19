package ${PACKAGE_NAME}

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

@Composable
fun ${NAME}Route(
    navigateBack: () -> Unit,
    viewModel: ${NAME}ViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ${NAME}Effect.NavigateBack -> navigateBack()
            }
        }
    }

    ${NAME}Screen(
        viewState = viewState,
        onViewEvent = viewModel::setEvent
    )

}

@Composable
fun ${NAME}Screen(
    viewState: ${NAME}State,
    onViewEvent: (${NAME}Event) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ${NAME}ScreenPreview() {
    ${NAME}Screen(
        viewState = ${NAME}State(),
        onViewEvent = { }
    )
}
