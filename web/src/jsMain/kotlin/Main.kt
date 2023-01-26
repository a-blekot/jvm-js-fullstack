import androidx.compose.runtime.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.compose.web.css.keywords.auto

val coroutineScope = MainScope()

fun main() {

    renderComposable(rootElementId = "root") {
        val shoppingList = remember { mutableStateOf(emptyList<ShoppingListItem>()) }

        LaunchedEffect(Any()) {
            shoppingList.value = getShoppingList()
        }

        Card(
            attrs = {
                style {
                    position(Position.Absolute)
                    height(800.px)
                    property("max-width", 940.px)
                    top(0.px)
                    bottom(0.px)
                    left(0.px)
                    right(0.px)
                    property("margin", auto)
                }
            }
        ) {
            Div(
                attrs = {
                    style {
                        width(100.percent)
                        height(100.percent)
                        display(DisplayStyle.Flex)
                        flexFlow(FlexDirection.Column, FlexWrap.Nowrap)
                    }
                }
            ) {
                Div(
                    attrs = {
                        style {
                            width(100.percent)
                            property("flex", "0 1 auto")
                        }
                    }
                ) {
                    Header {
                        Text("Full-Stack Shopping List")
                    }
                }

                Header {
                    Text("Full-Stack Shopping List")
                }

                Ul(
                    attrs = {
                        style {
                            width(100.percent)
                            margin(0.px)
                            property("flex", "1 1 auto")
                            property("overflow-y", "scroll")
                        }
                    }
                ) {
                    shoppingList.value.sortedByDescending(ShoppingListItem::priority).forEach { item ->
                        Li(
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
                            MaterialCheckbox(
                                checked = item.done,
                                onCheckedChange = {
                                    coroutineScope.launch {
                                        toggleShoppingListItem(item)
                                        shoppingList.value = getShoppingList()
                                    }
                                },
                                attrs = {
                                    style {
                                        property("flex", "0 1 auto")
                                        property("padding-top", 10.px) // Fix for the checkbox not being centered vertically
                                    }
                                }
                            )

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
                                Text(value = item.desc)
                            }

                            ImageButton(
                                onClick = {
                                    coroutineScope.launch {
                                        deleteShoppingListItem(item)
                                        shoppingList.value = getShoppingList()
                                    }
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
                }

                InputComponent { input ->
                    val cartItem = ShoppingListItem(
                        desc = input.replace("!", "").trim(),
                        priority = input.count { it == '!' },
                        done = false,
                    )

                    console.log("onSubmit $input")

                    coroutineScope.launch {
                        addShoppingListItem(cartItem)
                        shoppingList.value = getShoppingList()
                    }
                }
            }
        }
    }
}
