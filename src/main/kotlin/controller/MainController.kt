package controller

import javafx.beans.value.ObservableValue
import javafx.collections.ObservableArray
import tornadofx.*
import model.Password

val a = mapOf("a" to "a")

class MainController : Controller() {
    fun getCollections(): List<String> {
        return listOf("title1", "title2", "title3")
    }

    fun getPasswords(collection: String) = listOf(
        Password("github", "login1", "email1", "password1", 1),
        Password("reddit", "login2", "email2", "password2", 2),
        Password("vk", "login3", "email3", "password3", 3),
        Password("blizzard", "login4", "email4", "password4", 4)
    )
}