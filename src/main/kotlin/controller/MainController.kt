package controller

import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import model.Password
import tornadofx.Controller

class MainController : Controller() {
    val collectionsProperty = SimpleListProperty<String>()
    val selectedCollectionProperty = SimpleStringProperty()
    var selectedItemProperty = SimpleObjectProperty<Password>()
}