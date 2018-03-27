package com.anh.map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * java类简单作用描述
 *
 * @Description: java类作用描述
 * @Author: anh6
 * @CreateDate: 2018/3/13 9:41
 * @UpdateUser:
 * @UpdateRemark:
 */
public class MapLoad {
    // frame窗口的高宽
    private final int frameWidth = 800;
    private final int frameHeight = 600;

    private JFrame frame;

    // 三个panel
    private JPanel mainPanel;
    private MapPanel mapPanel;
    private JPanel layer;

    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;

    public static void main(String[] args) {
        MapLoad mapLoad = new MapLoad();
        mapLoad.createMap();
    }

    /**
     * 创建底图
     */
    private void createMap() {
        // frame设置
        frame = new JFrame("Anh Map");
        frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        initMapImg();
        initLayerPanel();
        mapPanel.setPreferredSize(new Dimension(frameWidth, frameHeight - 100));

        // 主面板
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 0, 0));
//        mainPanel.setOpaque(false);
        // 设置layout
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(mapPanel, BorderLayout.NORTH);
        mainPanel.add(layer, BorderLayout.SOUTH);
        frame.setContentPane(mainPanel);

        frame.pack();
        frame.setVisible(true);
    }

    private void initLayerPanel() {
        layer = new JPanel();
        layer.setBackground(new Color(0, 255, 0));
        layer.setPreferredSize(new Dimension(frameWidth, 70));

        jButton1 = new JButton("OK1");
        jButton1.setPreferredSize(new Dimension(60, 30));
        jButton2 = new JButton("OK2");
        jButton2.setPreferredSize(new Dimension(60, 30));
        jButton3 = new JButton("OK3");
        jButton3.setPreferredSize(new Dimension(60, 30));

        layer.add(jButton1);
        layer.add(jButton2);
        layer.add(jButton3);
    }

    private void initMapImg() {
        // 计算视图内x,y轴上的map图片数量
        Dimension preferredSize = frame.getPreferredSize();
        System.out.println("wh:" + preferredSize.width + ", " + preferredSize.height);
        int imageXSize = preferredSize.width / 256 + 2;
        int imageYSize = preferredSize.height / 256 + 2;

        // panel设置
        mapPanel = new MapPanel(imageXSize, imageYSize);
        mapPanel.setBackground(new Color(80, 134, 180));

        // 设置原点
        mapPanel.setOrigin(new Point(-100, -50));

        mapPanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                System.out.println("mouseWheelMoved");
                mapPanel.repaint();
            }
        });

        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                mapPanel.setMpPressedPoint(point);
            }
        });

        mapPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = e.getPoint();
                Point pressedPoint = mapPanel.getMpPressedPoint();
                int disX = point.x - pressedPoint.x;
                int disY = point.y - pressedPoint.y;
                Point origin = mapPanel.getOrigin();
                origin.x = origin.x + disX;
                origin.y = origin.y + disY;

                // 填充拖动地图后产生的空白区域
                while (origin.x > 0) {
                    origin.x -= 256;
                    mapPanel.setMpBaseX(mapPanel.getMpBaseX() - 1);
                }
                while (origin.x < -256) {
                    origin.x += 256;
                    mapPanel.setMpBaseX(mapPanel.getMpBaseX() + 1);
                }
                while (origin.y > 0) {
                    origin.y -= 256;
                    mapPanel.setMpBaseY(mapPanel.getMpBaseY() - 1);
                }
                while (origin.y < -256) {
                    origin.y += 256;
                    mapPanel.setMpBaseY(mapPanel.getMpBaseY() + 1);
                }

                // 重绘
                mapPanel.repaint();
                mapPanel.setMpPressedPoint(point);
            }

        });
    }
}
