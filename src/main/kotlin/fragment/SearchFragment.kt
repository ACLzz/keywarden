package fragment

import app.SearchIcon
import tornadofx.*

class SearchFragment : Fragment() {
    override val root = hbox {
        textfield()
        this += SearchIcon()
    }
}