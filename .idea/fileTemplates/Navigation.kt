package ${PACKAGE_NAME}

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ${NAME}Route = "${NAME}Route"

fun NavController.navigateTo${NAME}(navOptions: NavOptions? = null) {
    this.navigate(${NAME}Route, navOptions)
}
#set( $lowercaseName = ${NAME.substring(0,1).toLowerCase()} + ${NAME.substring(1)} )
fun NavGraphBuilder.${lowercaseName}Screen(
    navigateBack: () -> Unit,
) {
    composable(
        route = ${NAME}Route,
    ) {
        ${NAME}Route(
            navigateBack = navigateBack,
        )
    }
}
