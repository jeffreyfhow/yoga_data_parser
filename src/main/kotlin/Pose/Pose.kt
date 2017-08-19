package Pose

/**
 * Created by jeffreyhow on 7/12/17.
 * Pose.Pose data class to hold all info related to a particular yoga pose.
 */
data class Pose (
        val id: String,
        val engName: String,
        val position: String,
        val classification: String,
        val difficulty: String,
        val sanName: String? = null,
        val altNames: List<String> = emptyList(),
        val translations: List<Translation> = emptyList(),
        val img_file: String? = null,
        var grade: Grade = Grade.NULL
){
    /**
     * The following functions are used to create a custom formatted string representation for the Pose.Pose.
     */
    fun idString() = "  \"id\": \"$id\",\n"
    fun engString() = "  \"english\": \"$engName\",\n"
    fun posString() = "  \"position\": \"$position\",\n"
    fun clsString() = "  \"classification\": \"$classification\",\n"
    fun difString() = "  \"difficulty\": \"$difficulty\",\n"
    fun sanString() = if(sanName != null) "  \"sanskrit\": \"$sanName\",\n" else ""
    fun grdString() = "  \"grade\": \"$grade\"\n"
    fun imgString() = if(img_file != null) "  \"img_file\": \"$img_file\",\n" else ""
    fun altString(): String {
        if (altNames.count() < 1) return ""
        var altString = "  \"alt_names\": [\n"
        for(i in altNames.indices){
            altString += "    \"${altNames[i]}\""
            if (i < (altNames.count() - 1)) altString += ","
            altString += "\n"
        }
        altString += "  ],\n"
        return altString
    }
    fun trnString(): String {
        if(translations.count() < 1) return ""
        var transString = "  \"translations\": [\n"
        for(i in translations.indices){
            transString += "    \"${translations[i].toString()}\""
            if (i < (translations.count() - 1)) transString += ","
            transString += "\n"
        }
        transString += "  ],\n"
        return transString
    }

    override fun toString(): String {
        var result = "{\n${idString()}${engString()}${sanString()}${altString()}${trnString()}"
            result += "${difString()}${posString()}${clsString()}${imgString()}${grdString()}}"
        return result
    }
}