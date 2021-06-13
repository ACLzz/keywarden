package controller

import javafx.beans.value.ObservableValue
import javafx.collections.ObservableArray
import tornadofx.*
import model.Password

val a = mapOf("a" to "a")

class MainController : Controller() {
    fun fetchCollections(): List<String> {
        return listOf("title1", "title2", "title3")
    }

    fun fetchPasswords(collection: String) = listOf(
            Password("github", id=1),
            Password("reddit", id=2),
            Password("vk", id=3),
            Password("blizzard", id=4)
    )

    fun getPassword(id: Int) = listOf("login", "email", "password")
}