package app

import tornadofx.App
import tornadofx.launch
import view.AuthView

class MyApp: App(AuthView::class, Styles::class, PasswordsTable::class, ActionBarStyle::class, IconStyle::class)

fun main(args: Array<String>) {
    launch<MyApp>()
}