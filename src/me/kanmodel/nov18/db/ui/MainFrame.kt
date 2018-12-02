package me.kanmodel.nov18.db.ui

import java.awt.Font
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.JTabbedPane
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

class MainFrame : JFrame(frameTitle) {
    init {
        initUI()
        initGlobalFont(Font("幼圆", Font.PLAIN, 19))
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
        val adminPanel = AdminPanel(this)

        tabbedPane.add(hikotokoPanel, "欢迎")
        tabbedPane.add(tablePanel, "一言库")
        tabbedPane.add(null, "出自统计")
        tabbedPane.add(adminPanel, "控制台")
        tabbedPane.addChangeListener {
//            println("当前选中的选项卡index: ${tabbedPane.selectedIndex}")
            when (tabbedPane.selectedIndex) {
                2 -> tabbedPane.setComponentAt(2, CountTablePanel())
            }
        }

        add(tabbedPane)

        isResizable = false
    }

    companion object {
        val frameTitle = "hitokoto - 一言库"

        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater {
                MainFrame().isVisible = true
            }
        }
    }

    /**
     * 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
     */
    private fun initGlobalFont(font: Font) {
        val fontRes = FontUIResource(font)
        val keys = UIManager.getDefaults().keys()
        while (keys.hasMoreElements()) {
            val key = keys.nextElement()
            val value = UIManager.get(key)
            if (value is FontUIResource) {
                UIManager.put(key, fontRes)
            }
        }
    }
}