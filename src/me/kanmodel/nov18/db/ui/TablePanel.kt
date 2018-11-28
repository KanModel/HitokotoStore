package me.kanmodel.nov18.db.ui

import me.kanmodel.nov18.db.database.DataQuery
import me.kanmodel.nov18.db.database.SqlExecutor

import javax.swing.*
import javax.swing.table.DefaultTableModel
import java.awt.*
import java.awt.event.ItemEvent
import java.util.Vector
import java.util.regex.Pattern

internal class TablePanel : JPanel() {

    private val searchList = arrayOf("编号", "出自")
    private val searchBox = JComboBox(searchList)
    /**
     * 搜索类型
     */
    private var searchType = SEARCH_BY_ID
    private var searchInfoFiled: JTextField? = null

    private val hBox0: Box = Box.createHorizontalBox()
    private val hBox1: Box = Box.createHorizontalBox()
    private val searchBtn = JButton("搜索")
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
    private val table: JTable = JTable(tableModel)
    private var dataVector: Vector<Vector<Any>>? = null
    private var columnName: Vector<String>? = null

    init {
        val vBox = Box.createVerticalBox()
        initButton()
        initTextFiled()
        initSearchBox()
        initTable()
        vBox.add(hBox0)
        vBox.add(Box.createVerticalStrut(20))
        vBox.add(hBox1)

        add(vBox)
    }

    private fun initTable() {
        table
        table.preferredScrollableViewportSize = Dimension(800, 600)
        table.autoResizeMode = JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
        tableModel.addTableModelListener(UpdateTableListener(tableModel, table))

        val jScrollPane = JScrollPane(table)
        columnName = Vector()//字段名
        columnName!!.add("编号")
        columnName!!.add("Hitokoto")
        columnName!!.add("出自")
        dataVector = Vector()
        dataVector = DataQuery.searchByFrom("")
        tableModel.setDataVector(dataVector, columnName)
        setColumn(table, 0, 60)
        setColumn(table, 1, 550)

        hBox1.add(jScrollPane)
    }

    private fun initButton() {
        val addBtn = JButton("添加")
        addBtn.addActionListener {
            tableModel.insertRow(0, arrayOf<Any>(0, "", ""))
        }
        hBox0.add(addBtn)
        hBox0.add(Box.createHorizontalStrut(20))

        val deleteBtn = JButton("删除")
        deleteBtn.addActionListener {
            val row = table.selectedRow
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "请选择要删除的行")
            } else {
//                val id = table.getValueAt(row, 0).toString()
                deleteRow()
            }
        }
        hBox0.add(deleteBtn)
        hBox0.add(Box.createHorizontalStrut(20))


        val updateBtn = JButton("更新")
        updateBtn.addActionListener {
            for (i in UpdateTableListener.updateList) {

            }
        }
        hBox0.add(updateBtn)
        hBox0.add(Box.createHorizontalStrut(20))

        searchBtn.addActionListener {
            // 按照文本框内容查询
            val content = searchInfoFiled!!.text.trim { it <= ' ' }

            when (searchType) {
                SEARCH_BY_ID -> {
                    val pattern = Pattern.compile("[0-9]*")//使用正则表达式判断是否为数字
                    val isNum = pattern.matcher(content)
                    if (isNum.matches()) {
                        dataVector = DataQuery.searchById(if (content == "") 0 else Integer.valueOf(content))
                        UpdateTableListener.clearHighLight(table)
                    } else {
                        JOptionPane.showMessageDialog(this, "请输入正确数字ID", "警告", JOptionPane.WARNING_MESSAGE)
                    }
                }
                SEARCH_BY_FROM -> {
                    dataVector = DataQuery.searchByFrom(content)
                    UpdateTableListener.clearHighLight(table)
                }
            }
            val newTableModel = table.model as DefaultTableModel//
            if (dataVector!!.size == 0) {
                JOptionPane.showMessageDialog(this, "未找到对应数据", "警告", JOptionPane.WARNING_MESSAGE)
            } else {
                newTableModel.setDataVector(dataVector, columnName)
                setColumn(table, 0, 60)
                setColumn(table, 1, 550)
                table.updateUI()
            }
        }
    }

    private fun initSearchBox() {
        searchBox.addItemListener { e ->
            // 只处理选中的状态
            if (e.stateChange == ItemEvent.SELECTED) {
                searchType = searchBox.selectedIndex
                println("选中: " + searchBox.selectedIndex + " = " + searchBox.selectedItem)
            }
        }
        val searchPane = JPanel()
        searchPane.add(searchBox)
        hBox0.add(searchPane)
        hBox0.add(Box.createHorizontalStrut(20))
        hBox0.add(searchBtn)
    }

    private fun initTextFiled() {
        searchInfoFiled = JTextField()
        searchInfoFiled!!.columns = 10
        hBox0.add(searchInfoFiled)
    }

    private fun setColumn(table: JTable, i: Int, width: Int) {
        val firstColumn = table.columnModel.getColumn(i)
        firstColumn.preferredWidth = width
        firstColumn.maxWidth = width
        firstColumn.minWidth = width
    }

    private fun deleteRow() {
        while (table.selectedRows.isNotEmpty()) {
            val row = table.selectedRow// 获得选择的第一行
            val id = table.getValueAt(row, 0).toString()
            val hikotoko = table.getValueAt(row, 1).toString()
            val from = table.getValueAt(row, 2).toString()
            println(id + hikotoko + from + row)
            (table.model as DefaultTableModel).removeRow(row)
            SqlExecutor.deleteById(id)
        }
    }

    companion object {
        private const val SEARCH_BY_ID = 0
        private const val SEARCH_BY_FROM = 1
    }
}

