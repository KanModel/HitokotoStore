package me.kanmodel.nov18.db.ui

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

import javax.swing.JFrame
import javax.swing.JTabbedPane
import javax.swing.SwingUtilities

class MainFrame : JFrame("hitokoto - һ�Կ�") {
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

        // ����ʵ����
        val tabbedPane = JTabbedPane()
        // �������
        val hikotokoPanel = HitokotoPanel()
        val tablePanel = TablePanel()
        val adminPanel = AdminPanel()

        tabbedPane.add(hikotokoPanel, "��ӭ")
        tabbedPane.add(tablePanel, "һ�Կ�")
        tabbedPane.add(null, "����ͳ��")
        tabbedPane.add(adminPanel, "����̨")
        tabbedPane.addChangeListener {
            println("��ǰѡ�е�ѡ�index: ${tabbedPane.selectedIndex}")
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