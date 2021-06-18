package fragment

import app.Config
import app.Styles
import app.accentForegroundColor
import app.secondForegroundColor
import controller.Client
import javafx.beans.property.SimpleListProperty
import javafx.scene.control.cell.TextFieldListCell
import javafx.stage.StageStyle
import model.Password
import tornadofx.*
import java.awt.TextField
import kotlin.reflect.KFunction1

class CollectionsListFragment(updatePasswords: KFunction1<List<Password>, Unit>, collections: SimpleListProperty<String>,
                              updateSelectedCollection: (String) -> Unit) : Fragment() {

    init {
        if (collections.isNotEmpty()) {
            updateSelectedCollection(collections[0])
        }
    }

    override val root = vbox {
        style {
            backgroundColor += secondForegroundColor
            paddingTop = 10.0
        }

        listview(collections.asObservable()) {
            prefHeight = Config.h
            selectedItem.apply {
                setOnMouseClicked {
                    if (selectedItem == null) {
                        return@setOnMouseClicked
                    }

                    val (passwords, err) = Client.Passwords.fetchPasswords(selectedItem.toString())
                    if (err != null) {
                        popNotify(scope, err, true)
                    } else {
                        updatePasswords(passwords)
                        updateSelectedCollection(selectedItem.toString())
                    }
                }
            }

            isEditable = true
            setCellFactory(TextFieldListCell.forListView())
        }
    }
}