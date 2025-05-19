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

public val Icons.IcEyesOff: ImageVector
    get() {
        if (_iceyesoff != null) {
            return _iceyesoff!!
        }
        _iceyesoff = Builder(
            name = "Iceyesoff",
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
                moveTo(2.0f, 2.0f)
                lineTo(22.0f, 22.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 2.0f, strokeLineCap = strokeCapRound, strokeLineJoin =
                strokeJoinRound, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(6.713f, 6.723f)
                curveTo(3.665f, 8.795f, 2.0f, 12.0f, 2.0f, 12.0f)
                curveTo(2.0f, 12.0f, 5.636f, 19.0f, 12.0f, 19.0f)
                curveTo(14.05f, 19.0f, 15.817f, 18.273f, 17.271f, 17.288f)
                moveTo(11.0f, 5.058f)
                curveTo(11.325f, 5.02f, 11.659f, 5.0f, 12.0f, 5.0f)
                curveTo(18.364f, 5.0f, 22.0f, 12.0f, 22.0f, 12.0f)
                curveTo(22.0f, 12.0f, 21.308f, 13.332f, 20.0f, 14.833f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 2.0f, strokeLineCap = strokeCapRound, strokeLineJoin =
                strokeJoinRound, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(14.0f, 14.236f)
                curveTo(13.469f, 14.711f, 12.768f, 15.0f, 12.0f, 15.0f)
                curveTo(10.343f, 15.0f, 9.0f, 13.657f, 9.0f, 12.0f)
                curveTo(9.0f, 11.176f, 9.332f, 10.43f, 9.869f, 9.888f)
            }
        }
            .build()
        return _iceyesoff!!
    }

private var _iceyesoff: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.IcEyesOff, contentDescription = null)
    }
}
