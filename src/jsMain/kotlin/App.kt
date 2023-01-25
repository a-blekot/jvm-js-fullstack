import react.*
import kotlinx.coroutines.*
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul

private val scope = MainScope()

val App = FC<Props> {
    var shoppingList by useState(emptyList<ShoppingListItem>())

    useEffectOnce {
        scope.launch {
            shoppingList = getShoppingList()
        }
    }

    h1 {
        +"Full-Stack Shopping List"
    }

    ul {
        shoppingList.sortedByDescending(ShoppingListItem::priority).forEach { item ->

            li {
                key = item.toString()
                onClick = {
                    scope.launch {
                        toggleShoppingListItem(item)
                        shoppingList = getShoppingList()
                    }
                }
                onDoubleClick = {
                    if (item.done) {
                        scope.launch {
                            deleteShoppingListItem(item)
                            shoppingList = getShoppingList()
                        }
                    }
                }

                val doneSign = if (item.done) "✓ " else ""
                +"$doneSign[${item.priority}] ${item.desc}"
            }
        }
    }
    inputComponent {
        onSubmit = { input ->
            val cartItem = ShoppingListItem(
                desc = input.replace("!", "").trim(),
                priority = input.count { it == '!' },
                done = false,
            )
            scope.launch {
                addShoppingListItem(cartItem)
                shoppingList = getShoppingList()
            }
        }
    }
}
