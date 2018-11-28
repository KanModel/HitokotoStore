package me.kanmodel.nov18.db.database

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

            val rs = SqlQuery.queryByID(id)
            if (rs != null) {
                if (rs.next()) {
                    var id = rs.getInt("id")
                    var hitokoto = rs.getString("hitokoto")
                    var from = rs.getString("from")
                    vec.add(id)
                    vec.add(hitokoto)
                    vec.add(from)
                    dataVector.add(vec)
                    while (rs.next()) {
                        val vec = Vector<Any>()
                        id = rs.getInt("id")
                        hitokoto = rs.getString("hitokoto")
                        from = rs.getString("from")
                        vec.add(id)
                        vec.add(hitokoto)
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

            val rs = SqlQuery.queryByFrom(from)
            if (rs != null) {
                if (rs.next()) {
                    var id = rs.getInt("id")
                    var hitokoto = rs.getString("hitokoto")
                    var from = rs.getString("from")
                    vec.add(id)
                    vec.add(hitokoto)
                    vec.add(from)
                    dataVector.add(vec)
                    while (rs.next()) {
                        val vec = Vector<Any>()
                        id = rs.getInt("id")
                        hitokoto = rs.getString("hitokoto")
                        from = rs.getString("from")
                        vec.add(id)
                        vec.add(hitokoto)
                        vec.add(from)
                        dataVector.add(vec)
                    }
                    rs.close()
                }
            }
            return dataVector
        }
    }
}