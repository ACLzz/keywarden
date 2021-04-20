package app

import tornadofx.*

abstract class Icon : Fragment() {
    override val root = pane {
        addClass(IconStyle.ico)
    }
}

class EditIcon : Icon() {
    init {
        root.apply {
            svgpath("M17 3a2.828 2.828 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5L17 3z").apply {
                fill = IconStyle.fillColor
                stroke = IconStyle.strokeColor
            }
        }
    }
}

class PlusIcon : Icon() {
    init {
        root.apply {
            line(12.0, 5.0, 12.0, 19.0)
            line(5.0, 12.0, 19.0, 12.0)
        }
    }
}

class SearchIcon : Icon() {
    init {
        root.apply {
            circle(11.0,11.0,8.0).apply {
                fill = IconStyle.fillColor
                stroke = IconStyle.strokeColor
            }
            line(21.0,21.0,16.65,16.65)
        }
    }
}

class TrashIcon : Icon() {
    init {
        root.apply {
            polyline(3,6,5,6,21,6)
            svgpath("M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2").apply {
                fill = IconStyle.fillColor
                stroke = IconStyle.strokeColor
            }
            line(10,11,10,17)
            line(14,11,14,17)
        }
    }
}

class UserIcon : Icon() {
    init {
        root.apply {
            svgpath("M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2")
            circle(12,7,4)
        }
    }
}