package me.kanmodel.nov18.db.ui

import java.awt.Color
import java.awt.Graphics
import javax.swing.ImageIcon
import javax.swing.JTextPane

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: KanModel
 * Date: 2018-11-29-13:58
 */
/**
 * @description: 随机显示hitokoto的面板
 * @author: KanModel
 * @create: 2018-11-29 13:58
 */
class RandomTextPane : JTextPane() {
    init {
        isOpaque = false
        // this is needed if using Nimbus L&F - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6687960
        background = Color(0, 0, 0, 0)
    }

    override fun paintComponent(g: Graphics) {
        // set background green - but can draw image here too
        //            g.setColor(Color.GREEN);
        //            g.fillRect(0, 0, getWidth(), getHeight());

        // uncomment the following to draw an image
//        val imageIcon = ImageIcon("image\\ADA.png")
        val img = HitokotoPanel.imageIcons[HitokotoPanel.indexIcon % 2].image
        g.drawImage(img, 0, 0, this)

        super.paintComponent(g)
    }
}