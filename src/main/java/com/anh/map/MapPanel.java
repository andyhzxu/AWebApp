package com.anh.map;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.concurrent.*;

/**
 * java类简单作用描述
 *
 * @Description: java类作用描述
 * @Author: anh6
 * @CreateDate: 2018/3/13 10:09
 * @UpdateUser:
 * @UpdateRemark:
 */
public class MapPanel extends JPanel {

    // 是否在线地图
    private boolean onLine = false;

    // 是否采用多线程方式加载瓦片图
    private boolean multiThread = false;

    // 拖拽开始时，按钮点击的位置
    private Point mpPressedPoint = null;

    // 初始缩放级别，原点的图片
    private int mpBaseZoom = 10;
    private int mpBaseX = 800;
    private int mpBaseY = 380;

    // 原点。需要通过原点，初始化加载以及拖拽时新的填充
    private Point origin = null;

    // x,y轴上可视区域图片的数量
    private int imageXSize = 0;
    private int imageYSize = 0;

    public MapPanel(int imageXSize, int imageYSize) {
        this.imageXSize = imageXSize;
        this.imageYSize = imageYSize;
    }

    /**
     * 多线程绘制，一张图一个线程
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        // 需要调用super.paint，否则会有重影。且需要放在drawImage前，否则会覆盖drawImage效果
        super.paint(g);

        // 多线程时，需要等所有绘制线程都结束，才能退出paint方法，否则绘制无效果
        ExecutorService es = null;

        for (int i = 0; i < imageXSize; i++) {
            int x = mpBaseX + i;
            int insertX = origin.x + 256 * i;
            for (int j = 0; j < imageYSize; j++) {
                try {
                    if (multiThread) {
                        if (es == null) {
                            es = new ThreadPoolExecutor(imageXSize * imageYSize, imageXSize * imageYSize,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>());
                        }
                        int tempJ = j;
                        es.execute(new Runnable() {
                            @Override
                            public void run() {
                                draw(g, tempJ, x, insertX, onLine);
                            }
                        });
                    } else {
                        draw(g, j, x, insertX, onLine);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 等待所有线程运行结束
        if (es != null) {
            es.shutdown();
            try {
                es.awaitTermination(3, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param g       Graphics
     * @param j
     * @param x
     * @param insertX
     * @param onLine
     */
    private void draw(Graphics g, int j, int x, int insertX, boolean onLine) {
        try {
            int y = mpBaseY + j;
            int insertY = origin.y + 256 * j;
            Image img = null;
            String mpBaseUrl = null;
            if (onLine) {
                mpBaseUrl = "http://mt0.google.cn/vt/lyrs=s";
                // 在线方式
                URL u = new URL(mpBaseUrl + "&x=" + x + "&y=" + y + "&z=" + mpBaseZoom);
                img = ImageIO.read(u);
            } else {
                // 离线方式
                mpBaseUrl = "F:\\googlemap\\tiles";
                String url = mpBaseUrl + "\\" + mpBaseZoom + "\\" + x + "\\" + y + ".png";
                img = ImageIO.read(new File(url));
            }
            System.out.println("insertX:" + insertX + ", insertY:" + insertY);
            g.drawImage(img, insertX, insertY, 256, 256, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public Point getMpPressedPoint() {
        return mpPressedPoint;
    }

    public void setMpPressedPoint(Point mpPressedPoint) {
        this.mpPressedPoint = mpPressedPoint;
    }

    public int getMpBaseZoom() {
        return mpBaseZoom;
    }

    public void setMpBaseZoom(int mpBaseZoom) {
        this.mpBaseZoom = mpBaseZoom;
    }

    public int getMpBaseX() {
        return mpBaseX;
    }

    public void setMpBaseX(int mpBaseX) {
        this.mpBaseX = mpBaseX;
    }

    public int getMpBaseY() {
        return mpBaseY;
    }

    public void setMpBaseY(int mpBaseY) {
        this.mpBaseY = mpBaseY;
    }

    public int getImageXSize() {
        return imageXSize;
    }

    public void setImageXSize(int imageXSize) {
        this.imageXSize = imageXSize;
    }

    public int getImageYSize() {
        return imageYSize;
    }

    public void setImageYSize(int imageYSize) {
        this.imageYSize = imageYSize;
    }
}
