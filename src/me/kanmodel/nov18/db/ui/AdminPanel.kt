package me.kanmodel.nov18.db.ui

import me.kanmodel.nov18.db.spider.HitokotoGet
import javax.swing.*
import kotlin.concurrent.thread

class AdminPanel : JPanel() {
    private val switchBtn = JButton("ÅÀ³æ¿ª¹Ø")

    init {
        switchBtn.addActionListener {
            thread(start = true) {
                if (!HitokotoGet.flag) {
                    HitokotoGet.flag = true
                    HitokotoGet().spider()
                } else {
                    HitokotoGet.flag = false
                }
            }
        }
        add(switchBtn)
    }
}
