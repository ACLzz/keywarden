package view

import app.Config
import fragment.ActionBar
import fragment.CollectionsListFragment
import fragment.PasswordsTableFragment
import fragment.SearchFragment
import javafx.scene.layout.Priority
import tornadofx.*

class MainView : View("Keywarden") {
    private val bw = Config.w * Config.k
    private val bh = Config.h * Config.k
    private val cw = 280.0

    override val root = hbox {
        prefWidth = bw
        prefHeight = bh
        visibleProperty().onChange {
            currentWindow?.sizeToScene()
        }
        val passwordsTableFragment = PasswordsTableFragment()

        vbox {
            minWidth = cw
            prefHeight = Config.h
            this += SearchFragment()
            this += CollectionsListFragment(passwordsTableFragment::updatePasswords)
        }

        vbox {
            prefWidth = Config.w - cw
            gridpaneConstraints {
                vgrow = Priority.ALWAYS
                hgrow = Priority.ALWAYS
            }
            this += ActionBar()
            this += passwordsTableFragment
        }
    }
}