package ie.setu.domain

import org.joda.time.DateTime

data class Sleep(
    var id:Int,
    var duration:Double,
    var phase:String,
    var day:String,
    var starttime: DateTime,
    var userId : Int
)