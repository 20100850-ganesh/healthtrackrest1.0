package ie.setu.domain

import org.joda.time.DateTime

data class Medicine(
    var id:Int,
    var description:String,
    var medicine:String,
    var day:String,
    var intake: DateTime,
    var userId : Int
)