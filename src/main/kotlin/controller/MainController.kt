package controller

import fragment.PopUpFragment
import fragment.PromptFragment
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.stage.StageStyle
import model.Password
import tornadofx.Controller
import tornadofx.asObservable

class MainController : Controller() {
    val collectionsProperty = SimpleListProperty<String>()
    val selectedCollectionProperty = SimpleStringProperty()
    val usernameProperty = SimpleStringProperty()
    var selectedPasswordProperty = SimpleObjectProperty<Password>()

    lateinit var fetchAndUpdatePasswords: () -> Unit

    lateinit var popNotify: (String, Boolean) -> Unit
    fun buildNotify(text: String, warning: Boolean) = find<PopUpFragment>(PopUpFragment::text to text, PopUpFragment::warning to warning)

    fun popPrompt(text: String, choices: Array<String>, hasInput: Boolean, handler: (String, String) -> Unit) {
        find<PromptFragment>(
            PromptFragment::text to text,
            PromptFragment::choices to choices,
            PromptFragment::hasInput to hasInput,
            PromptFragment::handler to handler,
        ).openModal(stageStyle = StageStyle.UTILITY)
    }

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

    fun deleteSelectedPassword() {
        popPrompt("Do you really want to delete ${selectedPasswordProperty.value.title}?", arrayOf("Yes", "No"), false
        ) { choice, _ ->
            if (choice == "Yes") {
                Client.Passwords.deletePassword(
                    selectedPasswordProperty.value.id,
                    selectedPasswordProperty.value.collection
                )
                fetchAndUpdatePasswords()
            }
        }
    }
}