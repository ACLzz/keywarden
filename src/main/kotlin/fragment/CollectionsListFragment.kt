package fragment

import controller.ConfigController
import controller.MainController
import javafx.beans.Observable
import javafx.collections.ObservableArray
import javafx.scene.Parent
import tornadofx.*

class CollectionsListFragment : Fragment() {
    private val controller: MainController by inject()
    private val cfg: ConfigController by inject()

    override val root = vbox {
        label("Collections")
        listview(controller.getCollections().asObservable()) {
            prefHeight = cfg.h
        }
    }
}