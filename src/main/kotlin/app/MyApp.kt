package app

import tornadofx.*
import tornadofx.launch
import view.AuthView

class MyApp: App(AuthView::class, Styles::class, PasswordsTableStyle::class, CollectionsListStyle::class,
    ActionBarStyle::class, IconStyle::class, SearchFragmentStyle::class) {
    init {
        reloadStylesheetsOnFocus()
    }
}

fun main() {
    launch<MyApp>()
}