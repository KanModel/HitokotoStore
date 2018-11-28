package me.kanmodel.nov18.db.spider

import com.google.gson.Gson
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException
import me.kanmodel.nov18.db.database.DBInfo
import me.kanmodel.nov18.db.database.SqlExecutor
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.sql.DriverManager
import java.sql.SQLException
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
            SqlExecutor.insertHitokoto(hitokoto)
            br.close()
            connection.disconnect()
        }
    }

}
