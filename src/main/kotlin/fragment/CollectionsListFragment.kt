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

class CollectionsListFragment(updatePasswords: KFunction1<List<Password>, Unit>, collections: SimpleListProperty<String>) : Fragment() {
    var currentCollection: String = ""

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

                    val (passwords, err) = Client.fetchPasswords(selectedItem.toString())
                    if (err != null) {
                        find<PopUpFragment>(mapOf(PopUpFragment::text to err, PopUpFragment::warning to true)).openModal(stageStyle = StageStyle.UNDECORATED)
                    } else {
                        updatePasswords(passwords)
                    }
                }
            }

            isEditable = true
            setCellFactory(TextFieldListCell.forListView())
        }
        if (collections.isNotEmpty()) {
            currentCollection = collections[0]
        }
    }
}