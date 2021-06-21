package controller

import app.Config
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpDelete
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.fuel.json.FuelJson
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import model.Password
import model.placeholder
import org.json.JSONObject
import java.lang.Exception
import javax.json.Json

open class ClientModule {
    lateinit var fetchRequest: ((String, String, p3: List<Pair<String, String>>?) -> FuelJson)
}

object Client {
    private val tokenProperty = SimpleStringProperty("")
    private var token by tokenProperty
    fun settoken(t: String) { token = t }

    val Auth = AuthModule().apply { fetchRequest = ::fetchRequest }
    val Collections = CollectionsModule().apply { fetchRequest = ::fetchRequest }
    val Passwords = PasswordsModule().apply { fetchRequest = ::fetchRequest }

    private fun fetchRequest(path: String, _request: String, data: List<Pair<String, String>>? = null): FuelJson {
        val url = Config.url + path
        val request = when (_request) {
            "GET" -> url.httpGet()
            "POST" -> url.httpPost()
            "PUT" -> url.httpPut()
            "DELETE" -> url.httpDelete()
            else -> url.httpGet()
        }
        if (data != null) {
            val jsonData = with (JsonBuilder()) {
                for (p in data) {
                    add(p.first, p.second)
                }
                build()
            }.toString()
            request.jsonBody(jsonData)
        }
        request.appendHeader(Pair("Authorization", token))

        val (_, response, result) = request.responseJson()
        if (result is Result.Failure) {
            var errStr: String
            if (response.statusCode == -1) {
                errStr = "{\"error\": \"No connection to server\"}"
            } else {
                errStr = String(response.data)
            }
            errStr = errStr.replace("\n", "")
            return FuelJson(errStr)
        }
        return result.get()
    }
}