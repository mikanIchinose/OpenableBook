package com.example.openablebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.openablebook.ui.theme.OpenableBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenableBookTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun Sheet(
    rotation: Float,
    front: @Composable () -> Unit,
    back: @Composable () -> Unit
) {
}

@Composable
fun Front(
    progress: Float,
    cameraDistance: Float
) {
    fun rotation() = progress * 180
    fun isFlipped() = progress > 0.5f
    if (isFlipped()) {
        var width by remember { mutableFloatStateOf(0f) }
        Box(
            modifier = Modifier
                .graphicsLayer {
//                    translationX = (- width * (1-progress))/2 - (width/2)
                    translationX = - (1 - progress /2) * width
                    transformOrigin = TransformOrigin(1f, 0.5f)
                    rotationY = -(rotation() - 180)
                    this.cameraDistance = cameraDistance
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.picturebook_2),
                contentDescription = null,
                modifier = Modifier.onGloballyPositioned {
                    width = it.size.width.toFloat()
                }
            )
        }

    } else {
        var width by remember { mutableFloatStateOf(0f) }
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX=(width / 2) * progress
                    transformOrigin = TransformOrigin(0f, 0.5f)
                    rotationY = -rotation()
                    this.cameraDistance = cameraDistance
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.picturebook_1),
                contentDescription = null,
                modifier = Modifier.onGloballyPositioned {
                    width = it.size.width.toFloat()
                }
            )
        }
    }
}

@Composable
fun InsideRight(
    progress: Float
) {
    var width by remember { mutableFloatStateOf(0f) }
    Box(
        modifier = Modifier.graphicsLayer {
            translationX = (width / 2) * progress
        }
    )
    {
        Image(
            painter = painterResource(id = R.drawable.picturebook_3),
            contentDescription = null,
            modifier = Modifier.onGloballyPositioned {
                width = it.size.width.toFloat()
            }
        )
    }
}

@Composable
fun Book() {
    var isOpened by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (isOpened) 1f else 0f,
        animationSpec = tween(2000, easing = EaseInOut),
        label = "progress"
    )
    Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 32.dp).clickable { isOpened = !isOpened }
        ) {
            InsideRight(animatedProgress)
            Front(animatedProgress, 70f)
        }
}

@Preview(showBackground = true, device = "spec:parent=pixel_4a,orientation=landscape")
@Composable
fun OpenableBookPreview() {
    OpenableBookTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Book()
        }
    }
}
