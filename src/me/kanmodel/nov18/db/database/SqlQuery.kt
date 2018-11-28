package me.kanmodel.nov18.db.database

import java.sql.DriverManager
import java.sql.ResultSet

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: KanModel
 * Date: 2018-11-12-12:20
 */
/**
 * @description: SQL查询类
 * @author: KanModel
 * @create: 2018-11-12 12:20
 */
class SqlQuery {

    companion object {


        @JvmStatic
        fun main(args: Array<String>) {
            println(isExists(111))
            queryByFrom("海贼王")
        }

        fun isExists(id: Int): Boolean {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()

            val sql = "SELECT * FROM ${DBInfo.DB}.${DBInfo.MT} where `id`=$id;"

            val rs = stmt.executeQuery(sql)

            // 展开结果集数据库
            val exists = rs.next()
            // 完成后关闭
            rs.close()
            stmt.close()
            conn.close()
            return exists
        }

        fun getFromID(from: String): Int {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()

            val sql = "SELECT * FROM ${DBInfo.DB}.${DBInfo.FT} where ${DBInfo.FT}.`from`='$from';"

            val rs = stmt.executeQuery(sql)

            // 展开结果集数据库
            val exists = rs.next()
            try {
                return if (exists) {
                    rs.getInt("fromID")
                } else {
                    -1
                }
            } finally {
                // 完成后关闭
                rs.close()
                stmt.close()
                conn.close()
            }
        }

        fun getCreatorID(creator: String): Int {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()

            val sql = "SELECT * FROM ${DBInfo.DB}.${DBInfo.CT} where `creator`='$creator';"

            val rs = stmt.executeQuery(sql)

            // 展开结果集数据库
            val exists = rs.next()

            try {
                return if (exists) {
                    rs.getInt("creatorID")
                } else {
                    -1
                }
            } finally {
                // 完成后关闭
                rs.close()
                stmt.close()
                conn.close()
            }
        }

        fun queryByFrom(from: String = ""): ResultSet? {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()

            val sql = if (from == "") {
                //language=SQL
                "SELECT * FROM ${DBInfo.DB}.${DBInfo.MT},${DBInfo.DB}.${DBInfo.FT} " +
                        "where ${DBInfo.MT}.fromID = ${DBInfo.FT}.fromID group by id;"
            } else {
                //language=SQL
                "SELECT * FROM ${DBInfo.DB}.${DBInfo.MT},${DBInfo.DB}.${DBInfo.FT} " +
                        "where `from`='$from' and ${DBInfo.MT}.fromID = ${DBInfo.FT}.fromID group by id;"
            }

            return stmt.executeQuery(sql)
        }

        fun queryByID(id: Int = 0): ResultSet? {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()


            val sql = if (id == 0) {
                //language=SQL
                "SELECT * FROM ${DBInfo.DB}.${DBInfo.MT},${DBInfo.DB}.${DBInfo.FT} " +
                        "where ${DBInfo.MT}.fromID = ${DBInfo.FT}.fromID group by id;"
            } else {
                //language=SQL
                "SELECT * FROM ${DBInfo.DB}.${DBInfo.MT},${DBInfo.DB}.${DBInfo.FT} " +
                        "where `id`='$id' and ${DBInfo.MT}.fromID = ${DBInfo.FT}.fromID group by id;"
            }

            return stmt.executeQuery(sql)
        }

        //        ID: String = ""
        fun updateById(ID: String = "") {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()

            val sql = if (ID == "") {
                //language=SQL
                "SELECT * FROM ${DBInfo.DB}.${DBInfo.MT};"
            } else {
                //language=SQL
                "SELECT * FROM ${DBInfo.DB}.${DBInfo.MT},${DBInfo.DB}.${DBInfo.FT} where `id`='$ID' and ${DBInfo.MT}.fromID = ${DBInfo.FT}.fromID;"
//                "SELECT * FROM ${DBInfo.DB}.${DBInfo.MT} where `id`='$ID';"
            }
            val rs = stmt.executeQuery(sql)
            if (rs.next()) {


            }
        }

        fun deleteById(id: String = "") {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()
            stmt.execute("delete from ${DBInfo.DB}.${DBInfo.MT} where `id` = $id;")
            stmt.close()
            conn.close()
        }
    }
}