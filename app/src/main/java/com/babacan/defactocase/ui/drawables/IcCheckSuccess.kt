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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Icons.IcCheckSuccess: ImageVector
    get() {
        if (_icCheckSuccess != null) {
            return _icCheckSuccess!!
        }
        _icCheckSuccess = Builder(
            name = "IcCheckSuccess",
            defaultWidth = 20.0.dp,
            defaultHeight = 20.0.dp,
            viewportWidth = 20.0f,
            viewportHeight = 20.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF1DB469)),
                strokeLineWidth = 1.5f, strokeLineCap = strokeCapRound, strokeLineJoin =
                strokeJoinRound, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(13.438f, 8.125f)
                lineTo(8.854f, 12.5f)
                lineTo(6.563f, 10.313f)
                moveTo(17.5f, 10.0f)
                curveTo(17.5f, 14.142f, 14.142f, 17.5f, 10.0f, 17.5f)
                curveTo(5.858f, 17.5f, 2.5f, 14.142f, 2.5f, 10.0f)
                curveTo(2.5f, 5.858f, 5.858f, 2.5f, 10.0f, 2.5f)
                curveTo(14.142f, 2.5f, 17.5f, 5.858f, 17.5f, 10.0f)
                close()
            }
        }
            .build()
        return _icCheckSuccess!!
    }

private var _icCheckSuccess: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.IcCheckSuccess, contentDescription = null)
    }
}
