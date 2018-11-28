package me.kanmodel.nov18.db.ui

import javax.swing.*
import java.awt.event.ItemEvent

internal class HitokotoPanel : JPanel() {
    init {
        val typeList = arrayOf("所有", "动漫", "author")
        val searchBox = JComboBox(typeList)
        searchBox.addItemListener { e ->
            // 只处理选中的状态
            if (e.stateChange == ItemEvent.SELECTED) {
                println("选中: " + searchBox.selectedIndex + " = " + searchBox.selectedItem)
            }
        }
        val searchPane = JPanel()
        searchPane.add(searchBox)
        add(searchPane)
    }
}
