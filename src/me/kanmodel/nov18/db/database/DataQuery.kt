package me.kanmodel.nov18.db.database

import me.kanmodel.nov18.db.spider.Hitokoto
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: KanModel
 * Date: 2018-11-27-9:28
 */
/**
 * @description: 数据查询类，调用SQL查询获取数据
 * @author: KanModel
 * @create: 2018-11-27 09:28
 */
class DataQuery {
    companion object {
        fun searchById(id: Int = 0): Vector<Vector<Any>> {
            val vec = Vector<Any>()
            val dataVector = Vector<Vector<Any>>()
            val rs = SqlExecutor.queryByID(id)
            if (rs != null) {
                if (rs.next()) {
                    var id = rs.getInt("id")
                    var hitokoto = rs.getString("hitokoto")
                    var from = rs.getString("from")
                    var type = getType(rs.getString("type"))
                    vec.add(id)
                    vec.add(hitokoto)
                    vec.add(type)
                    vec.add(from)
                    dataVector.add(vec)
                    while (rs.next()) {
                        val vec = Vector<Any>()
                        id = rs.getInt("id")
                        hitokoto = rs.getString("hitokoto")
                        from = rs.getString("from")
                        type = getType(rs.getString("type"))
                        vec.add(id)
                        vec.add(hitokoto)
                        vec.add(type)
                        vec.add(from)
                        dataVector.add(vec)
                    }
                    rs.close()
                }
            }
            return dataVector
        }

        fun searchByFrom(from: String = ""): Vector<Vector<Any>> {
            val vec = Vector<Any>()
            val dataVector = Vector<Vector<Any>>()

            val rs = SqlExecutor.queryByFrom(from)
            if (rs != null) {
                if (rs.next()) {
                    var id = rs.getInt("id")
                    var hitokoto = rs.getString("hitokoto")
                    var from = rs.getString("from")
                    var type = getType(rs.getString("type"))
                    vec.add(id)
                    vec.add(hitokoto)
                    vec.add(type)
                    vec.add(from)
                    dataVector.add(vec)
                    while (rs.next()) {
                        val vec = Vector<Any>()
                        id = rs.getInt("id")
                        hitokoto = rs.getString("hitokoto")
                        from = rs.getString("from")
                        type = getType(rs.getString("type"))
                        vec.add(id)
                        vec.add(hitokoto)
                        vec.add(type)
                        vec.add(from)
                        dataVector.add(vec)
                    }
                    rs.close()
                }
            }
            return dataVector
        }

        fun searchByContent(content: String = ""): Vector<Vector<Any>> {
            val vec = Vector<Any>()
            val dataVector = Vector<Vector<Any>>()

            val rs = SqlExecutor.queryByContent(content)
            if (rs != null) {
                if (rs.next()) {
                    var id = rs.getInt("id")
                    var hitokoto = rs.getString("hitokoto")
                    var from = rs.getString("from")
                    var type = getType(rs.getString("type"))
                    vec.add(id)
                    vec.add(hitokoto)
                    vec.add(type)
                    vec.add(from)
                    dataVector.add(vec)
                    while (rs.next()) {
                        val vec = Vector<Any>()
                        id = rs.getInt("id")
                        hitokoto = rs.getString("hitokoto")
                        from = rs.getString("from")
                        type = getType(rs.getString("type"))
                        vec.add(id)
                        vec.add(hitokoto)
                        vec.add(type)
                        vec.add(from)
                        dataVector.add(vec)
                    }
                    rs.close()
                }
            }
            return dataVector
        }

        fun queryByRandom(): Hitokoto? {
            val rs = SqlExecutor.queryRandom()
            return if (rs!!.next()) {
                Hitokoto(
                    rs.getInt("id"),
                    rs.getString("hitokoto"),
                    getType(rs.getString("type")),
                    rs.getString("from"),
                    rs.getString("creator"),
                    rs.getString("created_at")
                )
            } else {
                null
            }
        }

        fun queryCount(): Vector<Vector<Any>> {
            val vec = Vector<Any>()
            val dataVector = Vector<Vector<Any>>()

            val rs = SqlExecutor.queryFromCount()
            if (rs != null) {
                if (rs.next()) {
                    vec.add(rs.getString("from"))
                    vec.add(rs.getString("count"))
                    dataVector.add(vec)
                    while (rs.next()) {
                        val vec = Vector<Any>()
                        vec.add(rs.getString("from"))
                        vec.add(rs.getString("count"))
                        dataVector.add(vec)
                    }
                    rs.close()
                }
            }
            return dataVector
        }

        fun getType(type: String): String {
            return when (type) {
                "a" -> "动漫"
                "b" -> "漫画"
                "c" -> "游戏"
                "d" -> "小说"
                "e" -> "原创"
                "f" -> "来自网络"
                "g" -> "其他"
                else -> {
                    "未知"
                }
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
//            queryByRandom()
            print(queryByRandom())
        }
    }
}