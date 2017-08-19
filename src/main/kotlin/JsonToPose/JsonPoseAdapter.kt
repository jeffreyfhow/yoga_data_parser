package JsonToPose

import Pose.Pose
import Pose.Translation
import com.beust.klaxon.string
import com.beust.klaxon.lookup
import com.beust.klaxon.JsonObject
import com.beust.klaxon.array

/**
 * Created by jeffreyhow on 7/18/17.
 * Custom Exception for when a Pose is missing necessary information.
 *  Adapts Json Data to Pose Data and Vice Versa
 */
object JsonPoseAdapter {
    /**
     * Converts a json formatted pose into a Pose.Pose object
     */
    private fun jsonObjToPose(jsonObj: JsonObject): Pose? {
        val reqPropNames = listOf("id", "english", "difficulty", "position", "classification")
        var reqPropMap: MutableMap<String, String?> = mutableMapOf()

        // Required Properties
        reqPropMap["id"] = jsonObj.string("id")
        reqPropMap["english"] = jsonObj.string("english")

        //Optional Properties
        val sanskritName = jsonObj.string("sanskrit")
        val img_file = jsonObj.string("img_src")?.toLowerCase()
        var translations: MutableList<Translation> = mutableListOf()
        val jsonTranslations = jsonObj.lookup<String>("translations.translation")
        jsonTranslations.forEach {
            it?.let{ // Warning is a lie
                val (san, eng) = it.split("=")
                translations.add(Translation(san.trim(), eng.trim()))
            }
        }

        var altNames: MutableList<String> = mutableListOf()

        // Deeper Details
        val details = jsonObj.array<JsonObject>("details")
        details?.forEach {
            val label: String = it.string("label") ?: return@forEach
            val value: String = it.string("value") ?: return@forEach
            when {
                label.contains("Difficulty") -> reqPropMap["difficulty"] = value
                label.contains("Category") -> {
                    val (pos, cls) = value.split("/")
                    reqPropMap["position"] = pos.trim()
                    reqPropMap["classification"] = cls.trim()
                }
                label.contains("Alt. Name") -> {
                    val names = value.split("/")
                    names.forEach { altNames.add(it.trim()) }
                }
            }
        }

        try {
            reqPropNames.forEach {
                if(reqPropMap[it] == null) throw IncompletePoseException(jsonObj, it)
            }
            return Pose(
                    reqPropMap["id"]!!,
                    reqPropMap["english"]!!,
                    reqPropMap["position"]!!,
                    reqPropMap["classification"]!!,
                    reqPropMap["difficulty"]!!,
                    sanskritName, altNames, translations, img_file)
        } catch (e: IncompletePoseException){
            println("****************************************")
            println(e.message)
            println("****************************************")
            return null
        }
    }

    /**
     * Gets list of Poses from old custom formatted JsonObject
     */
    fun getPoses(jObj: JsonObject) = jObj.lookup<JsonObject>("poses").mapNotNull{ jsonObjToPose(it) as Pose }.toMutableList()

    /**
     * Converts list of poses to new custom format
     */
    fun posesToString(poseList: List<Pose>): String {
        var res: String = "[\n"
        for(i in poseList.indices){
            res += poseList[i]
            if(i < (poseList.count() - 1)) res += ","
            res += "\n"
        }
        res += "]"
        return res
    }

}

