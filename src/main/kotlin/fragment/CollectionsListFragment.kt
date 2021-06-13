package fragment

import controller.ConfigController
import controller.MainController
import model.Password
import tornadofx.*
import kotlin.reflect.KFunction1

class CollectionsListFragment(updatePasswords: KFunction1<List<Password>, Unit>) : Fragment() {
    private val controller: MainController by inject()
    private val cfg: ConfigController by inject()
    var collections = controller.fetchCollections()
    lateinit var currentCollection: String

    override val root = vbox {
        label("Collections")
        listview(collections.asObservable()) {
            prefHeight = cfg.h
            selectedItem.apply {
                setOnMouseClicked {
                    updatePasswords(controller.fetchPasswords(selectedItem.toString()))
                }
            }
        }
        currentCollection = collections[0]
    }
}