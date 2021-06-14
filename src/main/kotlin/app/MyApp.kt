package app

import tornadofx.*
import tornadofx.launch
import view.AuthView

class MyApp: App(AuthView::class, Styles::class, PasswordsTable::class, ActionBarStyle::class, IconStyle::class) {
    init {
        reloadStylesheetsOnFocus()
    }
}

fun main(args: Array<String>) {
    launch<MyApp>()
}