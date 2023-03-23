package com.mitron.asterapp

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerList(
    isLoading:Boolean
) {
    LazyColumn(modifier=Modifier.fillMaxSize().padding(16.dp)) {
        items(20) {
            if (isLoading) {
                Card(shape= RoundedCornerShape(8.dp),backgroundColor = Color.White){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth().size(20.dp)
                                .weight(6f).shimmerEffect().padding(),
                        )
                        IconButton(
                            modifier = Modifier
                                .weight(1f)
                                .alpha(ContentAlpha.medium),
                            onClick = {}) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowDown,
                                contentDescription = "Drop-Down Arrow",
                                tint = Color.DarkGray
                            )
                        }
                    }
                }
                Spacer(modifier=Modifier.height(16.dp))
            }
        }
    }
}

fun Modifier.shimmerEffect():Modifier=composed {

    var size by remember{
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2*size.width.toFloat(),
        targetValue = 2*size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush= Brush.linearGradient(
            colors= listOf(
                Color(0xFFE0DEDE),
                Color(0xFF5F5959),
                Color(0xFFE0DEDE)
            ),
            start= Offset(startOffsetX,0f),
            end=Offset(startOffsetX+size.width.toFloat(),size.height.toFloat())
        )
    )

        .onGloballyPositioned {
            size = it.size
        }
}