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
        initGlobalFont(Font("��Բ", Font.PLAIN, 19))
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
        val adminPanel = AdminPanel(this)

        tabbedPane.add(hikotokoPanel, "��ӭ")
        tabbedPane.add(tablePanel, "һ�Կ�")
        tabbedPane.add(null, "����ͳ��")
        tabbedPane.add(adminPanel, "����̨")
        tabbedPane.addChangeListener {
//            println("��ǰѡ�е�ѡ�index: ${tabbedPane.selectedIndex}")
            when (tabbedPane.selectedIndex) {
                2 -> tabbedPane.setComponentAt(2, CountTablePanel())
            }
        }

        add(tabbedPane)

        isResizable = false
    }

    companion object {
        val frameTitle = "hitokoto - һ�Կ�"

        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater {
                MainFrame().isVisible = true
            }
        }
    }

    /**
     * ͳһ�������壬����������֮�������ɸ����������ӽ��涼����Ҫ�ٴ���������
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