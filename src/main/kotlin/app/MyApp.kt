package app

import view.MainView
import tornadofx.App
import tornadofx.launch

class MyApp: App(MainView::class, Styles::class, PasswordsTable::class, ActionBarStyle::class, IconStyle::class)

fun main(args: Array<String>) {
    launch<MyApp>()
}