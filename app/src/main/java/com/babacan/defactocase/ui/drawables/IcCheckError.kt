package com.babacan.defactocase.ui.drawables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

public val Icons.IcCheckError: ImageVector
    get() {
        if (_icCheckError != null) {
            return _icCheckError!!
        }
        _icCheckError = Builder(
            name = "IcCheckError",
            defaultWidth = 20.0.dp,
            defaultHeight = 20.0.dp,
            viewportWidth = 20.0f,
            viewportHeight = 20.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFE92215)),
                strokeLineWidth = 1.5f, strokeLineCap = Round, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.5f, 7.5f)
                lineTo(7.5f, 12.5f)
                moveTo(12.5f, 12.5f)
                lineTo(7.5f, 7.5f)
                moveTo(17.5f, 10.0f)
                curveTo(17.5f, 14.142f, 14.142f, 17.5f, 10.0f, 17.5f)
                curveTo(5.858f, 17.5f, 2.5f, 14.142f, 2.5f, 10.0f)
                curveTo(2.5f, 5.858f, 5.858f, 2.5f, 10.0f, 2.5f)
                curveTo(14.142f, 2.5f, 17.5f, 5.858f, 17.5f, 10.0f)
                close()
            }
        }
            .build()
        return _icCheckError!!
    }

private var _icCheckError: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.IcCheckError, contentDescription = null)
    }
}
