package me.kanmodel.nov18.db.ui

import me.kanmodel.nov18.db.spider.HitokotoGet
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import java.awt.Image
import javax.swing.*
import kotlin.concurrent.thread

class AdminPanel(private val parentFrame: MainFrame) : JPanel() {
    private val switchBtn = JButton("���濪��")

    init {
        switchBtn.font = Font("��Բ", Font.PLAIN, 19)
        switchBtn.preferredSize = Dimension(200, 120)
        switchBtn.maximumSize = Dimension(300, 200)
        switchBtn.addActionListener {
            if (!HitokotoGet.flag) {
                HitokotoGet.flag = true
                thread(start = true) {
                    HitokotoGet().spider()
                }
                parentFrame.title = MainFrame.frameTitle + " ��ȡ����ing..."
            } else {
                HitokotoGet.flag = false
                parentFrame.title = MainFrame.frameTitle
            }
        }
        add(switchBtn)
    }
}
