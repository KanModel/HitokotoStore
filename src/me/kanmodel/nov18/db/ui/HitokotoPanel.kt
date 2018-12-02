package me.kanmodel.nov18.db.ui

import me.kanmodel.nov18.db.database.DataQuery
import java.awt.Dimension
import java.awt.Font
import java.awt.GridLayout
import java.awt.Image
import java.awt.event.ActionListener
import javax.swing.*
import java.awt.event.ItemEvent

internal class HitokotoPanel : JPanel(GridLayout(1, 1)) {
    private val randomTextPane = RandomTextPane()

    init {
        for (i in 0..1) {
            imageIcons.add(ImageIcon("image\\" + i.toString() + ".jpg"))
            imageIcons[i].image = imageIcons[i].image.getScaledInstance(1000, 800, Image.SCALE_DEFAULT)
        }

        val typeList = arrayOf("����", "����", "����", "��Ϸ", "С˵", "ԭ��", "��������", "����")
        val searchBox = JComboBox(typeList)
        searchBox.maximumSize = Dimension(80, 20)
        searchBox.addItemListener { e ->
            // ֻ����ѡ�е�״̬
            if (e.stateChange == ItemEvent.SELECTED) {
                println("ѡ��: " + searchBox.selectedIndex + " = " + searchBox.selectedItem)
            }
        }

        randomTextPane.font = Font("��Բ", Font.PLAIN, 30)
        randomTextPane.isEditable = false
        add(randomTextPane)

        Timer(randomInterval, ActionListener {
            val hitokoto = DataQuery.queryByRandom()
            if (hitokoto != null) {
                randomTextPane.text = "��${hitokoto.hitokoto}��" +
                        "\n\r                             -��${hitokoto.from}��"
            }
        }).start()

        Timer(randomImageInterval, ActionListener {
            indexIcon++
        }).start()
    }

    companion object {
        var randomInterval = 2000
        var randomImageInterval = 10000
        var indexIcon = 1
        val imageIcons = ArrayList<ImageIcon>()

    }
}
