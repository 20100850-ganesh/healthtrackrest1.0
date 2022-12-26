package ie.setu.domain

import org.joda.time.DateTime

data class Diet(
    var id:Int,
    var description:String,
    var food:String,
    var calories:Int,
    var intake: DateTime,
    var userId : Int
)