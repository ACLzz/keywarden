package view

import app.Config
import app.secondForegroundColor
import app.shadowColor
import app.whiteColor
import controller.Client
import fragment.*
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.effect.BlurType
import javafx.scene.effect.DropShadow
import javafx.scene.layout.Priority
import javafx.stage.StageStyle
import model.Password
import tornadofx.*

class MainView : View("Keywarden") {
    private val bw = Config.w * Config.k
    private val bh = Config.h * Config.k
    private val cw = 280.0
    private val collectionsProperty = SimpleListProperty<String>()
    private var collections by collectionsProperty
    private val selectedCollectionProperty = SimpleStringProperty()
    private var selectedCollection by selectedCollectionProperty

    init {
        val (colls, err) = Client.Collections.fetchCollections()
        if (err != null) {
            popNotify(scope, err, true)
        } else {
            collections = colls.asObservable()
        }
    }

    override val root = hbox {
        textfield {
            maxHeight = 0.0
            prefHeight = 0.0
            minHeight = 0.0

            maxWidth = 0.0
            prefWidth = 0.0
            minWidth = 0.0

            requestFocus()
        }

        prefWidth = bw
        prefHeight = bh
        visibleProperty().onChange {
            currentWindow?.sizeToScene()
        }
        val passwordsTableFragment = PasswordsTableFragment(collectionsProperty, selectedCollectionProperty)

        vbox {
            toFront()
            style {
                backgroundColor += secondForegroundColor
            }

            minWidth = cw
            prefHeight = Config.h
            this += SearchFragment()

            label {
                text = "Collections"
                style {
                    textFill = whiteColor
                    fontSize = 14.pt
                    paddingLeft = 15.0
                }
            }
            this += CollectionsListFragment(passwordsTableFragment::updatePasswords, collectionsProperty, ::updateCurrentCollection)
        }

        vbox {
            prefWidth = Config.w - cw
            gridpaneConstraints {
                vgrow = Priority.ALWAYS
                hgrow = Priority.ALWAYS
            }
            this += ActionBar()
            this += passwordsTableFragment
            // TODO "region" for notifications
        }
    }

    private fun updateCurrentCollection(collection: String) {
        selectedCollection = collection
    }
}