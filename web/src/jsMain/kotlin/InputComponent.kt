import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.TextInput

@Composable
fun InputComponent(onSubmit: (String) -> Unit) {
    val text = remember { mutableStateOf("") }

    Div(
        attrs = {
            style {
                width(100.percent)
                display(DisplayStyle.Flex)
                flexFlow(FlexDirection.Row, FlexWrap.Nowrap)
                alignItems(AlignItems.Center)
                property("padding", "0px 0px 0px 16px")
            }
        }
    ) {
        Div(
            attrs = {
                style {
                    height(48.px)
                    property("flex", "1 1 auto")
                    property("white-space", "nowrap")
                    property("text-overflow", "ellipsis")
                    property("overflow", "hidden")
                    display(DisplayStyle.Flex)
                    alignItems(AlignItems.Center)
                }
//                            onClick { onClicked(item.id) }
            }
        ) {
            TextInput(value = text.value) {
                onInput { event ->
                    text.value = event.value
                    console.log("event ->  ${event.value}")
                }
            }
        }

        ImageButton(
            onClick = {
                onSubmit(text.value)
            },
            iconName = "delete",
            attrs = {
                style {
                    property("flex", "0 1 auto")
                    marginLeft(8.px)
                }
            }
        )
    }
}
