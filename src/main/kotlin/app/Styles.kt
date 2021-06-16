package app

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

val whiteColor = c("#C7C7C7")
val foregroundColor = c("#312F1F")
val secondForegroundColor = c("#363636")
val mainBackgroundColor = c("#2A2A2A")
val accentText = c("#98936E")
val accentForegroundColor = c("#444444")
val shadowColor = c("#151515ff")


class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val button by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 12.px
            fontWeight = FontWeight.BOLD
            textFill = whiteColor
        }

        button {
            backgroundColor += foregroundColor
            textFill = whiteColor
            borderColor += box(foregroundColor)
        }
    }
}

class ActionBarStyle : Stylesheet() {
    companion object {
        val bar by cssclass()
        val actionLabel by cssclass()
        val username by cssclass()
    }

    init {
        bar {
            backgroundColor += foregroundColor
        }
        actionLabel {
            textFill = accentText
            fontSize = 12.pt
            labelPadding = box(all=5.px)
        }
        username {
            textFill = whiteColor
            fontSize = 14.pt
        }
    }
}

class CollectionsListStyle : Stylesheet() {
    companion object {}

    init {
        listView {
            listCell {
                backgroundColor += foregroundColor
                and(even) {
                    backgroundColor += accentForegroundColor
                }
                and(selected) {
                    backgroundColor += secondForegroundColor
                }

                textFill = whiteColor
            }

            padding = box(all=0.px)

            and(focused) {
                backgroundInsets = multi(box(0.px))
                backgroundColor += Color.TRANSPARENT
            }
        }
    }
}

class PasswordsTableStyle : Stylesheet() {
    init {
        tableView {
            backgroundColor += mainBackgroundColor
            columnHeader {
                backgroundColor += accentForegroundColor
                label {
                    textFill = whiteColor
                }
            }
            tableColumn {
                backgroundColor += secondForegroundColor
                and(even) {
                    backgroundColor += accentForegroundColor
                }
                and(selected) {
                    backgroundColor += foregroundColor
                }
                textFill = whiteColor
            }
            padding = box(all=0.px)
            and(focused) {
                backgroundInsets = multi(box(0.px))
                backgroundColor += Color.TRANSPARENT
            }
            tableCell {
                and(lastVisible) {
                    borderWidth = multi(box(0.px))
                }
                borderWidth = multi(box(
                    top=0.px,
                    left = 0.px,
                    right=1.px,
                    bottom= 0.px))
                borderColor += box(Color.DARKGREY)
                label {
                    padding = box(0.px,0.px,0.px,left=2.px)
                }
            }

            scrollBar {
                minWidth = 0.px
                minHeight = 0.px
                maxWidth = 0.px
                maxHeight = 0.px
                prefWidth = 0.px
                prefHeight = 0.px
            }
        }
    }
}

class SearchFragmentStyle : Stylesheet() {
    companion object {
        val style by cssclass()
    }
    init {
        style {
            padding = box(all=15.px)
        }
    }
}

class IconStyle : Stylesheet() {
    companion object {
        val ico by cssclass()
        val fillColor = c("#00000000")
        val strokeColor = whiteColor
        val strokeWidth = 2.0
        val size = 24.0
    }

    init {
        ico {
            stroke = strokeColor
            strokeWidth = Dimension(Companion.strokeWidth, Dimension.LinearUnits.px)
            minHeight = Dimension(Companion.size, Dimension.LinearUnits.px)
            minWidth = Dimension(Companion.size, Dimension.LinearUnits.px)
        }
        line {
            stroke = whiteColor
            strokeWidth = Dimension(Companion.strokeWidth, Dimension.LinearUnits.px)
        }
    }
}