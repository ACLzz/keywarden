package fragment

import app.SearchFragmentStyle
import app.SearchIcon
import javafx.geometry.Pos
import tornadofx.*

class SearchFragment : Fragment() {
    override val root = hbox {
        addClass(SearchFragmentStyle.style)
        textfield {
            promptText = "Search"
            fitToParentWidth()
        }
        pane {
            val w = 10.0
            minWidth = w
            maxWidth = w
        }
        vbox {
            alignment = Pos.CENTER_RIGHT
            this += SearchIcon()
        }
    }
}