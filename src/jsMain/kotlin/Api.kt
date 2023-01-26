import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun getShoppingList(): List<ShoppingListItem> {
    console.log("suspend fun get")
    return jsonClient.get(ShoppingListItem.path).body()
}

suspend fun addShoppingListItem(shoppingListItem: ShoppingListItem) {
    console.log("suspend fun add $shoppingListItem")
    jsonClient.post(ShoppingListItem.path) {
        contentType(ContentType.Application.Json)
        setBody(shoppingListItem)
    }
}

suspend fun toggleShoppingListItem(shoppingListItem: ShoppingListItem) {
    console.log("suspend fun toggle $shoppingListItem")

    jsonClient.patch(ShoppingListItem.path + "/${shoppingListItem.id}")
}

suspend fun deleteShoppingListItem(shoppingListItem: ShoppingListItem) {
    console.log("suspend fun delete $shoppingListItem")
    jsonClient.delete(ShoppingListItem.path + "/${shoppingListItem.id}")
}