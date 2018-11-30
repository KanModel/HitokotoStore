package me.kanmodel.nov18.db.ui

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

import javax.swing.JFrame
import javax.swing.JTabbedPane
import javax.swing.SwingUtilities

class MainFrame : JFrame("hitokoto - 一言库") {
    init {
        initUI()
    }

    private fun initUI() {
        setBounds(200, 200, 1000, 800)
        setLocationRelativeTo(null)
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                super.windowClosing(e)
                System.exit(0)
            }
        })
        init()
    }

    private fun init() {

        // 对象实例化
        val tabbedPane = JTabbedPane()
        // 对象化面板
        val hikotokoPanel = HitokotoPanel()
        val tablePanel = TablePanel()
        val adminPanel = AdminPanel()

        tabbedPane.add(hikotokoPanel, "欢迎")
        tabbedPane.add(tablePanel, "一言库")
        tabbedPane.add(null, "出自统计")
        tabbedPane.add(adminPanel, "控制台")
        tabbedPane.addChangeListener {
            println("当前选中的选项卡index: ${tabbedPane.selectedIndex}")
            when (tabbedPane.selectedIndex) {
                2 -> tabbedPane.setComponentAt(2, CountTablePanel())
            }
        }

        add(tabbedPane)

        isResizable = false
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater {
                MainFrame().isVisible = true
            }
        }
    }
}