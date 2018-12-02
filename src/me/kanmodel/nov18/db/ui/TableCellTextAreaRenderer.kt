package me.kanmodel.nov18.db.ui

import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import javax.swing.JTable
import javax.swing.JTextArea
import javax.swing.JTextPane
import javax.swing.table.TableCellRenderer

open class TableCellTextAreaRenderer : JTextArea(), TableCellRenderer {
    init {
        lineWrap = true //设置为换行
        wrapStyleWord = true //设置使用单词边界来换行
    }

    override fun getTableCellRendererComponent(table: JTable, value: Any?,
                                               isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component {
        // 计算当下行的最佳高度
        var maxPreferredHeight = 0
        for (i in 0 until table.columnCount) {
            if (i==3) {
                text = table.getValueAt(row, i).toString() + " "
                table.setRowHeight(row, minimumSize.height)
            }
        }

        font = Font("幼圆", Font.PLAIN, 19)
        text = value?.toString() ?: ""
        return this
    }
}