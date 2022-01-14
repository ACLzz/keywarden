package app

import controller.Client
import tornadofx.*
import tornadofx.launch
import view.AuthView

class MyApp: App(AuthView::class, Styles::class, PasswordsTableStyle::class, CollectionsListStyle::class,
    ActionBarStyle::class, IconStyle::class, SearchFragmentStyle::class) {
    init {
        reloadStylesheetsOnFocus()
    }

    override fun stop() {
        super.stop()
        Client.Auth.logout()
    }
}

fun main() {
    launch<MyApp>()
}