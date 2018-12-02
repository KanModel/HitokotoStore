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

    private val searchList = arrayOf("���", "����", "����")
    private val searchBox = JComboBox(searchList)
    /**
     * ��������
     */
    private var searchType = SEARCH_BY_ID
    private var searchInfoFiled: JTextField? = null

    private val hBox0: Box = Box.createHorizontalBox()
    private val hBox1: Box = Box.createHorizontalBox()
    private val searchBtn = JButton("����")
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

        override fun isCellEditable(row: Int, column: Int): Boolean {
            return column == 1
        }
    }
    private val table: JTable = JTable(tableModel)
    private var dataVector: Vector<Vector<Any>>? = null
    private var columnName = Vector<String>()//�ֶ���
    private val tableStyle = object : TableCellTextAreaRenderer(){}

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
        table.setDefaultRenderer(Any::class.java, tableStyle)
        table.preferredScrollableViewportSize = Dimension(800, 600)
        table.autoResizeMode = JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
        tableModel.addTableModelListener(UpdateTableListener(tableModel, table))

        val jScrollPane = JScrollPane(table)
        columnName.add("���")
        columnName.add("Hitokoto")
        columnName.add("����")
        columnName.add("����")
        dataVector = Vector()
        dataVector = DataQuery.searchByFrom("")
        tableModel.setDataVector(dataVector, columnName)
        setColumn(table, 0, 50)
        setColumn(table, 1, 550)
        table.font = Font("��Բ", Font.PLAIN, 19)


        hBox1.add(jScrollPane)
    }

    private fun initButton() {
//        val addBtn = JButton("���")
//        addBtn.addActionListener {
//            tableModel.insertRow(0, arrayOf<Any>(0, "", ""))
//        }
//        hBox0.add(addBtn)
//        hBox0.add(Box.createHorizontalStrut(20))//todo
        val deleteBtn = JButton("ɾ��")
        deleteBtn.addActionListener {
            val row = table.selectedRow
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "��ѡ��Ҫɾ������")
            } else {
//                val id = table.getValueAt(row, 0).toString()
                deleteRow()
            }
        }
        hBox0.add(deleteBtn)
        hBox0.add(Box.createHorizontalStrut(20))


        val updateBtn = JButton("����")
        updateBtn.addActionListener {
            for (i in UpdateTableListener.updateList) {
                val id = table.getValueAt(i.first, 0).toString()
                val content = table.getValueAt(i.first, 1).toString()
                println("$id $content")
                SqlExecutor.updateContentById(id, content)
            }
            UpdateTableListener.clearHighLight(table)
        }
        hBox0.add(updateBtn)
        hBox0.add(Box.createHorizontalStrut(20))

        searchBtn.addActionListener {
            // �����ı������ݲ�ѯ
            val content = searchInfoFiled!!.text.trim { it <= ' ' }

            when (searchType) {
                SEARCH_BY_ID -> {
                    val pattern = Pattern.compile("[0-9]*")//ʹ��������ʽ�ж��Ƿ�Ϊ����
                    val isNum = pattern.matcher(content)
                    if (isNum.matches()) {
                        dataVector = DataQuery.searchById(if (content == "") 0 else Integer.valueOf(content))
                        UpdateTableListener.clearHighLight(table)
                    } else {
                        JOptionPane.showMessageDialog(this, "��������ȷ����ID", "����", JOptionPane.WARNING_MESSAGE)
                    }
                }
                SEARCH_BY_FROM -> {
                    dataVector = DataQuery.searchByFrom(content)
                    UpdateTableListener.clearHighLight(table)
                }
                SEARCH_BY_CONTENT -> {
                    dataVector = DataQuery.searchByContent(content)
                    UpdateTableListener.clearHighLight(table)
                }
            }
            val newTableModel = table.model as DefaultTableModel//
            if (dataVector!!.size == 0) {
                JOptionPane.showMessageDialog(this, "δ�ҵ���Ӧ����", "����", JOptionPane.WARNING_MESSAGE)
            } else {
                newTableModel.setDataVector(dataVector, columnName)
                setColumn(table, 0, 50)
                setColumn(table, 1, 550)
                table.updateUI()
            }
        }
    }

    private fun initSearchBox() {
        searchBox.addItemListener { e ->
            // ֻ����ѡ�е�״̬
            if (e.stateChange == ItemEvent.SELECTED) {
                searchType = searchBox.selectedIndex
                println("ѡ��: " + searchBox.selectedIndex + " = " + searchBox.selectedItem)
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
        val targetColumn = table.columnModel.getColumn(i)
        targetColumn.preferredWidth = width
        targetColumn.maxWidth = width
        targetColumn.minWidth = width
    }

    private fun deleteRow() {
        while (table.selectedRows.isNotEmpty()) {
            val row = table.selectedRow// ���ѡ��ĵ�һ��
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
        private const val SEARCH_BY_CONTENT = 2
    }
}

