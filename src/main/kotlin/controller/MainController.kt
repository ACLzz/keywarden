package controller

import fragment.popNotify
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import model.Password
import tornadofx.Controller
import tornadofx.asObservable

class MainController : Controller() {
    val collectionsProperty = SimpleListProperty<String>()
    val selectedCollectionProperty = SimpleStringProperty()
    var selectedItemProperty = SimpleObjectProperty<Password>()

    fun initCollections() {
        val (colls, err) = Client.Collections.fetchCollections()
        if (err != null) {
            popNotify(scope, err, true)
        } else {
            collectionsProperty.set(colls.asObservable())
            if (collectionsProperty.value.isNotEmpty()) {
                selectedCollectionProperty.set(collectionsProperty.value[0])
            }
        }
    }
}