import com.mongodb.ConnectionString
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import java.io.File


val shoppingList = mutableListOf(
    ShoppingListItem("Cucumbers 🥒", 1),
    ShoppingListItem("Tomatoes 🍅", 2),
    ShoppingListItem("Orange Juice 🍊", 3),
    ShoppingListItem("Peaches!! 🍑", 4)
)


//ORG_GRADLE_PROJECT_isProduction TODO deployment!!!

val connectionString: ConnectionString? = System.getenv("JVM_JS_FULLSTACK_MONGODB_URI")?.let {
    ConnectionString("$it?retryWrites=false")
}
//JVM_JS_FULLSTACK_MONGODB_URI=mongodb://mongo:13LlOhFIFwXfgEEPg33X@containers-us-west-157.railway.app:7629/
//JVM_JS_FULLSTACK_MONGODB_URI=mongodb+srv://english_mongo_admin:mInxuiByBIP10u8f@english.jvcsywe.mongodb.net/?retryWrites=true&w=majority

val client = if (connectionString != null) KMongo.createClient(connectionString).coroutine else KMongo.createClient().coroutine
val database = client.getDatabase(connectionString?.database ?: "getShoppingList")
val collection = database.getCollection<ShoppingListItem>()

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 9090
    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }
        routing {
            static("/") {
                val staticDir = listOf(
                    "../web/build/distributions",
                    "web/build/distributions"
                ).map { File(it) }
                    .filter { it.exists() }
                    .firstOrNull()

                if (staticDir != null) {
                    // If staticDir is not null, it means we run "./gradlew server:run" task
                    staticRootFolder = staticDir
                    files(".")
                    default("index.html")
                } else {
                    // If staticDir is null, it means we already package all with "./gradlew installDist" task
                    resources("web-resources")
                    files(".")
                    defaultResource("web-resources/index.html")
                }
            }
            get("/hello") {
                call.respondText("Hello, API!")
            }
            route(ShoppingListItem.path) {
                get {
//                    call.respond(getShoppingList)
                    println("LOGGG: get")
                    call.respond(collection.find().toList())
                }
                post {
                    println("LOGGG: post")
                    collection.insertOne(call.receive())
//                    getShoppingList += call.receive<ShoppingListItem>()
                    call.respond(HttpStatusCode.OK)
                }
                patch("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid patch request")
                    val item = collection.findOne(ShoppingListItem::id eq id) ?: error("No item with that ID exists")
                    println("LOGGG: patch $item")

                    collection.updateOne(ShoppingListItem::id eq id, item.copy(done = !item.done))
                    call.respond(HttpStatusCode.OK)
                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    println("LOGGG: delete $id")
                    collection.deleteOne(ShoppingListItem::id eq id)
//                    getShoppingList.removeIf { it.id == id }
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }.start(wait = true)
}
