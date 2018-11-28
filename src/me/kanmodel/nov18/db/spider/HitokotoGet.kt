package me.kanmodel.nov18.db.spider

import com.google.gson.Gson
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException
import me.kanmodel.nov18.db.database.DBInfo
import me.kanmodel.nov18.db.database.SqlQuery
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.sql.DriverManager
import java.sql.SQLException
import javax.xml.bind.JAXBElement
import kotlin.concurrent.thread


/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: KanModel
 * Date: 2018-10-28-21:37
 */
/**
 * @description: 爬虫主体
 * @author: KanModel
 * @create: 2018-10-28 21:37
 */
class HitokotoGet {
    companion object {
        var flag = false

        @JvmStatic
        fun main(args: Array<String>) {
            println("Spider start:")
            flag = true
            thread(start = true) {
                HitokotoGet().spider()
            }
            Thread.sleep(2000)
            println("Spider end!")
            flag = false
        }

    }

    fun spider() {
        Class.forName(DBInfo.JDBC_DRIVER)
        val conn = DriverManager.getConnection(
            DBInfo.DB_URL,
            DBInfo.USER,
            DBInfo.PASS
        )//数据库连接
        val stmt = conn.createStatement()

        while (flag) {
            val url = URL("https://v1.hitokoto.cn/")
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.connect()

            val br = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
            var line: String? = br.readLine()
            val sb = StringBuilder()
            while (line != null) {
                sb.append(line)
                line = br.readLine()
            }

            val hitokoto = Gson().fromJson(sb.toString(), Hitokoto::class.java)
            if (SqlQuery.isExists(hitokoto.id)) {
                println("ID: ${hitokoto.id} 已存在")
            } else {
                hitokoto.hitokoto = hitokoto.hitokoto.replace("'", "''")
                hitokoto.from = hitokoto.from.replace("'", "''")
                hitokoto.creator = hitokoto.creator.replace("'", "''")

                try {
                    var fromID = SqlQuery.getFromID(hitokoto.from)
                    var creatorID = SqlQuery.getCreatorID(hitokoto.creator)
                    if (fromID == -1) {
                        val addFrom = "INSERT INTO ${DBInfo.DB}.${DBInfo.FT} (`from`)VALUES ('${hitokoto.from}')"
                        stmt.execute(addFrom)
                        fromID = SqlQuery.getFromID(hitokoto.from)
                        if (fromID == -1) {
                            println("f GG")
                            continue
                        }
                    }
                    if (creatorID == -1) {
                        val addCreator =
                            "INSERT INTO ${DBInfo.DB}.${DBInfo.CT} (`creator`)VALUES ('${hitokoto.creator}')"
                        stmt.execute(addCreator)
                        creatorID = SqlQuery.getCreatorID(hitokoto.creator)
                        if (creatorID == -1) {
                            println("c GG")
                            continue
                        }
                    }
                    try {
                        println("新加一言: ${hitokoto.id}: [${hitokoto.hitokoto}]-[${hitokoto.from}]")
                        val addHitokoto =
                            "INSERT INTO `${DBInfo.DB}`.`${DBInfo.MT}` VALUES ( ${hitokoto.id}, '${hitokoto.hitokoto}', " +
                                    "'${hitokoto.type}', $fromID,$creatorID,'${hitokoto.created_at}')"
                        stmt.execute(addHitokoto)//插入数据
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }
                } catch (e: MySQLSyntaxErrorException) {
                    e.printStackTrace()
                    println("新加一言: ${hitokoto.id}: [${hitokoto.hitokoto}]-[${hitokoto.from}]")
                    System.exit(0)
                }
            }
            br.close()
            connection.disconnect()
        }
    }

    data class Hitokoto(
        val id: Int,
        var hitokoto: String,
        val type: String,
        var from: String,
        var creator: String,
        val created_at: String
    )
}
