package me.kanmodel.nov18.db.ui

import java.awt.Color
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel
import java.awt.Component
import javax.swing.JLabel
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer


/**
 * 表格监听器
 * @author: KanModel
 * Date: 2018-10-13
 * Time: 10:28
 */
class UpdateTableListener(private val tableModel: TableModel, private val jTable: JTable) : TableModelListener {

    /**
     * description: 当表格内容改变时触发
     * @author: KanModel
     */
    override fun tableChanged(e: TableModelEvent?) {
        val firstRow = e!!.firstRow

        val column = e.column//获取改变的列

        val type = e.type
        if (type == TableModelEvent.UPDATE && firstRow >= 0) {

            val id = tableModel.getValueAt(firstRow, 0) as Int
            val hitokoto = tableModel.getValueAt(firstRow, 1) as String
            println("$column Type: $type $id $hitokoto")
//            changeHighLight(jTable, firstRow, column)
            refreshHighLight(firstRow, column)
        }
    }

    private fun refreshHighLight(targetRow: Int = -1, column: Int = -1) {
        if (targetRow != -1 && column != -1) {
            updateList.add(Pair(targetRow, column))
        } else {
            updateList.clear()
        }

        for (i in updateList) {
            changeHighLight(targetRow = i.first, targetColumn = i.second)
        }
    }

    private fun changeHighLight(table: JTable = jTable, targetRow: Int, targetColumn: Int) {
        try {
            val tcr = object : DefaultTableCellRenderer() {
                override fun getTableCellRendererComponent(
                    table: JTable?,
                    value: Any, isSelected: Boolean, hasFocus: Boolean,
                    row: Int, column: Int
                ): Component {
//                    background = if (row == targetRow) {
                    background = if (Pair(row, targetColumn) in updateList) {
                        Color(206, 231, 255) //设置偶数行底色
                    } else {
                        Color.white //设置奇数行底色
                    }
                    //如果需要设置某一个Cell颜色，需要加上column过滤条件即可
                    return super.getTableCellRendererComponent(
                        table, value,
                        isSelected, hasFocus, row, column
                    )
                }
            }
            if (targetColumn == 0) {
                tcr.horizontalAlignment = JLabel.RIGHT
            }
//            for (i in 0 until table.columnCount) {
//                table.getColumn(table.getColumnName(i)).cellRenderer = tcr
//            }
            table.getColumn(table.getColumnName(targetColumn)).cellRenderer = tcr
            table.repaint()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    companion object {
        fun clearHighLight(jTable: JTable) {
            updateList.clear()
            try {
                val tcr = object : DefaultTableCellRenderer() {
                    override fun getTableCellRendererComponent(
                        table: JTable?,
                        value: Any, isSelected: Boolean, hasFocus: Boolean,
                        row: Int, column: Int
                    ): Component {
                        background = Color.white
                        return super.getTableCellRendererComponent(
                            table, value,
                            isSelected, hasFocus, row, column
                        )
                    }
                }
                for (i in 0 until jTable.columnCount) {
                    jTable.getColumn(jTable.getColumnName(i)).cellRenderer = tcr
                }
                jTable.repaint()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        val updateList = ArrayList<Pair<Int, Int>>()
    }
}