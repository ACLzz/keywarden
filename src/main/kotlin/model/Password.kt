package model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

const val placeholder = "********"

class Password(
    title: String,
    collection: String,
    login: String = placeholder,
    email: String = placeholder,
    password: String = placeholder,
    id: Int,
) {
    val titleProperty = SimpleStringProperty(title)
    var title by titleProperty

    val collectionProperty = SimpleStringProperty(collection)
    var collection by collectionProperty

    val loginProperty = SimpleStringProperty(login)
    var login by loginProperty

    val emailProperty = SimpleStringProperty(email)
    var email by emailProperty

    val passwordProperty = SimpleStringProperty(password)
    var password by passwordProperty

    val idProperty = SimpleIntegerProperty(id)
    var id by idProperty
}