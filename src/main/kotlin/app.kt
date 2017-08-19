import JsonToPose.JsonParser
import JsonToPose.JsonPoseAdapter
import Pose.Pose
import Pose.Grade
import com.beust.klaxon.string
import java.io.File

/*****************************************************************************************
 *                             Main Functionality
 *****************************************************************************************
 * Created by jeffreyhow on 7/12/17.
 * This program reformats custom yoga pose data for the YogaFlashCards app.
 * 1. Reads custom yoga pose json data and parses it into Pose.Pose objects
 * 2. Reads custom yoga-grade json data to help filter away any poses without grades of A, B or C
 * 3. Prints newly formatted data to a file in the output directory
 */
fun main(args: Array<String>) {
    val poses = getPosesFromFile("/yoga_data_test.json")
    val gradeMap = createGradeMapFromFile("/yoga_data_grades_test.json")
    val abcPoses: MutableList<Pose> = filterABCGradedPoses(poses, gradeMap)
    File("output/yoga_data_formatted_test.json").printWriter().use {
        it.print(JsonPoseAdapter.posesToString(abcPoses))
    }
}

/*****************************************************************************************
 *                               Helper Functions
 *****************************************************************************************/

/**
 * Converts custom json data from file to MutableList of Poses
 */
fun getPosesFromFile(filepath : String) = JsonPoseAdapter.getPoses(JsonParser.jsonFileToJsonObj(filepath))

/**
 * Converts custom yoga_grade json data to map of <yoga_name, grade> pairs
 */
fun createGradeMapFromFile(filepath: String) : MutableMap<String, Grade> {
    // 1. Parse custom yoga-grade data file
    val json_obj_grades = JsonParser.jsonFileToJsonArr(filepath)

    // 2. Using yoga-grade data, create a map of <yoga_name, grade> pairs
    val gradeMap: MutableMap<String, Grade> = mutableMapOf()
    json_obj_grades.forEach{
        val id = it.string("id")
        val gradeString = it.string("grade")
        if(id is String && gradeString is String){
            gradeMap[id] = Grade.valueOf(gradeString)
        }
    }
    return gradeMap
}

/**
 * Gets mutable list of poses with grades of A, B, C.
 */
fun filterABCGradedPoses(poseList: List<Pose>, gradeMap: MutableMap<String, Grade>): MutableList<Pose>{
    return poseList.filter{
        val g = gradeMap[it.id]
        if(g != null) {
            it.grade = g
            (g == Grade.A || g == Grade.B || g == Grade.C)
        } else {
            false
        }
    }.toMutableList()
}
