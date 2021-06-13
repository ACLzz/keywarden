package fragment

import app.EditIcon
import app.Icon
import app.PlusIcon
import app.TrashIcon
import model.Password
import tornadofx.*

class ActionBar : Fragment() {
    override val root = hbox {
        minHeight = 60.0
        maxHeight = 60.0
        this += ActionButton("Add", PlusIcon())
        this += ActionButton("Edit", EditIcon())
        this += ActionButton("Remove", TrashIcon())
    }
}

class ActionButton(text: String, icon: Icon) : Fragment() {
    override val root = hbox {
        label(text)
        this += icon
    }
}