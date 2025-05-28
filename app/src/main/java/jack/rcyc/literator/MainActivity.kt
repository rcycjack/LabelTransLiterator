package jack.rcyc.literator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jack.rcyc.labeltransliterator.LabelTransform
import jack.rcyc.literator.ui.theme.LableTransliteratorTheme

class MainActivity : ComponentActivity() {

    private val labelTransform = LabelTransform()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LableTransliteratorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        labelTransform
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, labelTransform: LabelTransform) {
    Text(
        text = "支付宝 : ${labelTransform.getRepresentLetter("支付宝")}",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LableTransliteratorTheme {
        Greeting("Android", labelTransform = LabelTransform())
    }
}