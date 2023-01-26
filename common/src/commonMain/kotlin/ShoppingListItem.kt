import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListItem(
    val desc: String,
    val priority: Int,
    val done: Boolean = false,
) {
    val id: Int = desc.hashCode()

    companion object {
        const val path = "/shoppingList"
    }
}