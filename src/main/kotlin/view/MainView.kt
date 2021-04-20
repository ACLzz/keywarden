package view

import controller.ConfigController
import fragment.ActionBar
import fragment.CollectionsListFragment
import fragment.PasswordsTableFragment
import fragment.SearchFragment
import javafx.scene.layout.Priority
import tornadofx.*

class MainView : View("Keywarden") {
    private val cfg: ConfigController by inject()
    private val bw = cfg.w * cfg.k
    private val bh = cfg.h * cfg.k
    private val cw = 280.0

    override val root = hbox {
        prefWidth = bw
        prefHeight = bh
        visibleProperty().onChange {
            currentWindow?.sizeToScene()
        }
        vbox {
            minWidth = cw
            prefHeight = cfg.h
            this += SearchFragment()
            this += CollectionsListFragment()
        }

        vbox {
            prefWidth = cfg.w - cw
            gridpaneConstraints {
                vgrow = Priority.ALWAYS
                hgrow = Priority.ALWAYS
            }
            this += ActionBar()
            this += PasswordsTableFragment()
        }
    }
}