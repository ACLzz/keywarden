package fragment

import app.foregroundColor
import app.secondForegroundColor
import javafx.geometry.Pos
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import tornadofx.*

class PromptFragment : Fragment() {
    val text: String by param()
    val choices: Array<String> by param()
    val hasInput: Boolean by param()
    val handler: (String, String) -> Unit by param()
    private var promptText = ""

    override val root = vbox {
        style {
            alignment = Pos.BOTTOM_RIGHT
            paddingAll = 10.0
            backgroundColor += secondForegroundColor

            borderWidth = multi(box(3.px))
            borderColor += box(foregroundColor)
        }

        val fs = 12.pt

        label {
            text = this@PromptFragment.text

            style {
                fontSize = fs
                textFill = c("#C7C7C7")
                paddingAll = 7.0
            }
        }

        if (hasInput) {
            textfield {
                setOnKeyTyped {
                    this@PromptFragment.promptText = this.text
                }

                setOnKeyPressed {
                    if (it.code == KeyCode.ENTER) {
                        var choice = ""
                        if (choices.isNotEmpty()) {
                            choice = choices[0]
                        }

                        handler(choice, this@PromptFragment.promptText)
                        this@PromptFragment.close()
                    }
                }
            }
        }

        if (choices.isNotEmpty()) {
            hbox {
                style {
                    paddingTop = 15
                }

                region {
                    hgrow = Priority.ALWAYS
                }

                spacing = 20.0
                choices.forEach {
                    button {
                        style {
                            padding = box(5.px, 10.px, 5.px, 10.px)
                            fontSize = fs
                        }
                        text = it
                        action {
                            handler(it, this@PromptFragment.promptText)
                            this@PromptFragment.close()
                        }
                    }
                }
            }
        }
    }
}