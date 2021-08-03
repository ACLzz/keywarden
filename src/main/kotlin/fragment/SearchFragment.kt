package fragment

import app.SearchFragmentStyle
import app.SearchIcon
import controller.Client
import controller.MainController
import javafx.geometry.Pos
import javafx.scene.input.KeyCode
import model.Password
import tornadofx.*
import kotlin.reflect.KFunction1

class SearchFragment(updatePasswords: KFunction1<List<Password>, Unit>) : Fragment() {
    private var searchRequest = ""
    private val mainController: MainController by inject()

    override val root = hbox {
        addClass(SearchFragmentStyle.style)

        fun search() {
            val foundPasswords = mutableListOf<Password>()
            for (collection in mainController.collectionsProperty) {
                val (passwords, err) = Client.Collections.fetchPasswords(collection)
                if (err != null) {
                    mainController.popNotify(err, true)
                    return
                }
                for (password in passwords) {
                    if (password.title.contains(searchRequest, ignoreCase = true)) {
                        foundPasswords.add(password)
                    }
                }
            }

            updatePasswords(foundPasswords)
        }

        textfield {
            promptText = "Search"
            fitToParentWidth()

            setOnKeyTyped {
                searchRequest = this.text
            }

            setOnKeyPressed {
                if (it.code == KeyCode.ENTER) {
                    search()
                }
            }
        }
        pane {
            val w = 10.0
            minWidth = w
            maxWidth = w
        }
        vbox {
            alignment = Pos.CENTER_RIGHT
            this += SearchIcon().root.apply {
                setOnMouseClicked {
                    search()
                }
            }
        }
    }
}