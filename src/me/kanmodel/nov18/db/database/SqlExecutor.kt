package me.kanmodel.nov18.db.database

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException
import me.kanmodel.nov18.db.spider.Hitokoto
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: KanModel
 * Date: 2018-11-12-12:20
 */
/**
 * @description: SQL执行类
 * @author: KanModel
 * @create: 2018-11-12 12:20
 */
class SqlExecutor {

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

        fun insertHitokoto(hitokoto: Hitokoto) {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()
            if (isExists(hitokoto.id)) {
                println("ID: ${hitokoto.id} 已存在")
            } else {
                hitokoto.hitokoto = hitokoto.hitokoto.replace("'", "''")
                hitokoto.from = hitokoto.from.replace("'", "''")
                hitokoto.creator = hitokoto.creator.replace("'", "''")

                try {
                    var fromID = getFromID(hitokoto.from)
                    var creatorID = getCreatorID(hitokoto.creator)
                    if (fromID == -1) {
                        val addFrom = "INSERT INTO ${DBInfo.DB}.${DBInfo.FT} (`from`)VALUES ('${hitokoto.from}')"
                        stmt.execute(addFrom)
                        fromID = getFromID(hitokoto.from)
                    }
                    if (creatorID == -1) {
                        val addCreator =
                            "INSERT INTO ${DBInfo.DB}.${DBInfo.CT} (`creator`)VALUES ('${hitokoto.creator}')"
                        stmt.execute(addCreator)
                        creatorID = getCreatorID(hitokoto.creator)
                    }
                    try {
                        println("新加一言: ${hitokoto.id}: [${hitokoto.hitokoto}]-[${hitokoto.from}]")
                        val addHitokoto =
                            "INSERT INTO ${DBInfo.DB}.${DBInfo.MT} VALUES ( ${hitokoto.id}, '${hitokoto.hitokoto}', " +
                                    "'${hitokoto.type}', $fromID,$creatorID,'${hitokoto.created_at}')"
                        stmt.execute(addHitokoto)//插入数据
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }
                } catch (e: MySQLSyntaxErrorException) {
                    e.printStackTrace()
                    println("新加一言失败: ${hitokoto.id}: [${hitokoto.hitokoto}]-[${hitokoto.from}]")
                    System.exit(0)
                }
            }
            stmt.close()
            conn.close()
        }

        fun queryRandom(count: Int = 1): ResultSet? {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()

            //language=SQL
            val sql = "SELECT * FROM ${DBInfo.DB}.${DBInfo.MT},${DBInfo.DB}.${DBInfo.FT},${DBInfo.DB}.${DBInfo.CT} " +
                    "where ${DBInfo.MT}.fromID = ${DBInfo.FT}.fromID AND ${DBInfo.MT}.creatorID = ${DBInfo.CT}.creatorID " +
                    "ORDER BY rand() LIMIT $count;"

            return stmt.executeQuery(sql)
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

        fun queryByContent(content: String = ""): ResultSet? {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()

            val sql = if (content == "") {
                //language=SQL
                "SELECT * FROM ${DBInfo.DB}.${DBInfo.MT},${DBInfo.DB}.${DBInfo.FT} " +
                        "where ${DBInfo.MT}.fromID = ${DBInfo.FT}.fromID group by id;"
            } else {
                //language=SQL
                "SELECT * FROM ${DBInfo.DB}.${DBInfo.MT},${DBInfo.DB}.${DBInfo.FT} " +
                        "where hitokoto regexp '$content' and ${DBInfo.MT}.fromID = ${DBInfo.FT}.fromID group by id;"
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

        fun queryFromCount(): ResultSet? {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()

            val sql =
            //language=SQL
                "SELECT * FROM ${DBInfo.DB}.from_count order by `count` desc ;"

            return stmt.executeQuery(sql)
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

        fun updateContentById(id: String, content: String) {
            Class.forName(DBInfo.JDBC_DRIVER)
            val conn = DriverManager.getConnection(
                DBInfo.DB_URL,
                DBInfo.USER,
                DBInfo.PASS
            )//数据库连接
            val stmt = conn.createStatement()
            stmt.execute("update ${DBInfo.DB}.${DBInfo.MT} set hitokoto = '$content' where `id` = $id;")
            stmt.close()
            conn.close()
        }
    }
}