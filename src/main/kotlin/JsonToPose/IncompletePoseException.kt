package JsonToPose

import com.beust.klaxon.JsonObject

/**
 * Created by jeffreyhow on 7/18/17.
 * Custom Exception for when a Pose.Pose is missing necessary information.
 */
class IncompletePoseException(jsonObj: JsonObject, poseDetailType: String):
        RuntimeException("JsonToPose.IncompletePoseException: pose does not contain required data - $poseDetailType\n$jsonObj")