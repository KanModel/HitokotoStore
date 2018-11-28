package me.kanmodel.nov18.db.spider

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: KanModel
 * Date: 2018-11-28-21:02
 */
data class Hitokoto(
    val id: Int,
    var hitokoto: String,
    val type: String,
    var from: String,
    var creator: String,
    val created_at: String
)