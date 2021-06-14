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
import java.lang.Exception

class ClientController : Controller() {
    private val tokenProperty = SimpleStringProperty("")
    private var token by tokenProperty

    fun register(login: String, password: String): String {
        val result = fetchRequest("auth/", "POST",
            listOf(Pair("login", login), Pair("password", password)))

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            ""
        }
    }

    fun login(login: String, password: String): String {
        val result = fetchRequest("auth/login", "POST",
            listOf(Pair("login", login), Pair("password", password)))

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            token = result.obj().get("token").toString()
            ""
        }
    }

    fun fetchCollections() : String {
        return ""
    }

    fun fetchRequest(path: String, _request: String, data: List<Pair<String, String>>? = null): FuelJson {
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
        request.appendHeader(Pair("Authorization",  tokenProperty))

        val (req, response, result) = request.responseJson()
        if (result is Result.Failure) {
            var errStr = String(response.data)
            errStr = errStr.replace("\n", "")
            return FuelJson(errStr)
        }
        return result.get()
    }
}