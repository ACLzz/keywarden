package controller

import fragment.PopUpFragment
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.StackPane
import model.Password
import tornadofx.Controller
import tornadofx.asObservable

class MainController : Controller() {
    val collectionsProperty = SimpleListProperty<String>()
    val selectedCollectionProperty = SimpleStringProperty()
    val usernameProperty = SimpleStringProperty()
    var selectedPasswordProperty = SimpleObjectProperty<Password>()
    var currentRoot = StackPane()

    lateinit var popNotify: (String, Boolean) -> Unit
    fun buildNotify(text: String, warning: Boolean) = find<PopUpFragment>(PopUpFragment::text to text, PopUpFragment::warning to warning)

    fun initCollections() {
        val (colls, err) = Client.Collections.fetchCollections()
        if (err != null) {
            popNotify(err, true)
        } else {
            collectionsProperty.set(colls.sorted().asObservable())
            if (collectionsProperty.value.isNotEmpty()) {
                selectedCollectionProperty.set(collectionsProperty.value[0])
            }
        }
    }

    fun getUsername() {
        val (username, err) = Client.Auth.readUser()
        err?.let {
            popNotify(err, true)
            return
        }
        usernameProperty.set(username)
    }
}