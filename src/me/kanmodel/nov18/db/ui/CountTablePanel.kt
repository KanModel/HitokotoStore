package me.kanmodel.nov18.db.ui

import me.kanmodel.nov18.db.database.DataQuery
import me.kanmodel.nov18.db.database.SqlExecutor

import javax.swing.*
import javax.swing.table.DefaultTableModel
import java.awt.*
import java.awt.event.ItemEvent
import java.util.Vector
import java.util.regex.Pattern
import javax.swing.table.DefaultTableCellRenderer

internal class CountTablePanel : JPanel() {

    private val hBox1: Box = Box.createHorizontalBox()
    private val tableModel = object : DefaultTableModel() {
        override fun getColumnClass(column: Int): Class<*> {
            val returnValue: Class<*>
            if (column in 0..(columnCount - 1)) {
                returnValue = getValueAt(0, column).javaClass
            } else {
                returnValue = Any::class.java
            }
            return returnValue
        }
    }
    private val tableStyle = object : DefaultTableCellRenderer() {
        init {
            horizontalAlignment = SwingConstants.CENTER
        }
    }
    private val table: JTable = JTable(tableModel)
    private var dataVector: Vector<Vector<Any>>? = null
    private var columnName = Vector<String>()//字段名

    init {
        val vBox = Box.createVerticalBox()
        initTable()
        vBox.add(Box.createVerticalStrut(20))
        vBox.add(hBox1)
        add(vBox)
    }

    private fun initTable() {
        table.setDefaultRenderer(Any::class.java, tableStyle)
        table.preferredScrollableViewportSize = Dimension(800, 600)
        table.autoResizeMode = JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
        tableModel.addTableModelListener(UpdateTableListener(tableModel, table))

        val jScrollPane = JScrollPane(table)

        columnName.add("出自")
        columnName.add("收录数量")
        dataVector = Vector()
        dataVector = DataQuery.queryCount()
        tableModel.setDataVector(dataVector, columnName)
        hBox1.add(jScrollPane)
    }
}

