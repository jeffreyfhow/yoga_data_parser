package JsonToPose

import com.beust.klaxon.JsonArray
import com.beust.klaxon.Parser
import com.beust.klaxon.JsonObject

/**
 * Created by jeffreyhow on 7/12/17.
 * Singleton used to parse json files using klaxon library (https://github.com/cbeust/klaxon/blob/master/README.md)
 */
object JsonParser {
    /**
     * Parses json file data
     */
    private fun parse(name: String) : Any? {
        val cls = Parser::class.java
        return cls.getResourceAsStream(name)?.let { inputStream ->
            return Parser().parse(inputStream)
        }
    }

    /**
     * Parses json file data to klaxon JsonObject
     */
    fun jsonFileToJsonObj(filepath: String) : JsonObject = parse(filepath) as JsonObject

    /**
     * Parses json file data to klaxon JsonArray
     */
    fun jsonFileToJsonArr(filepath: String) : JsonArray<JsonObject> = parse(filepath) as JsonArray<JsonObject>
}