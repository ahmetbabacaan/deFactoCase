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

public val Icons.IcEyesOn: ImageVector
    get() {
        if (_icEyesOn != null) {
            return _icEyesOn!!
        }
        _icEyesOn = Builder(
            name = "IcEyesOn",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 2.0f, strokeLineCap = strokeCapRound, strokeLineJoin =
                strokeJoinRound, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(1.0f, 12.0f)
                curveTo(1.0f, 12.0f, 5.0f, 4.0f, 12.0f, 4.0f)
                curveTo(19.0f, 4.0f, 23.0f, 12.0f, 23.0f, 12.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 2.0f, strokeLineCap = strokeCapRound, strokeLineJoin =
                strokeJoinRound, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(1.0f, 12.0f)
                curveTo(1.0f, 12.0f, 5.0f, 20.0f, 12.0f, 20.0f)
                curveTo(19.0f, 20.0f, 23.0f, 12.0f, 23.0f, 12.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 2.0f, strokeLineCap = strokeCapRound, strokeLineJoin =
                strokeJoinRound, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.0f, 12.0f)
                moveToRelative(-3.0f, 0.0f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, true, 6.0f, 0.0f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, true, -6.0f, 0.0f)
            }
        }
            .build()
        return _icEyesOn!!
    }

private var _icEyesOn: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.IcEyesOn, contentDescription = null)
    }
}
