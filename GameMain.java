import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.io.*;

public class GameMain extends JFrame {

    private Container cp;
    private JPanel jpnt = new JPanel(new GridLayout(1, 8, 2, 1));
    private JPanel jpnr = new JPanel(new GridLayout(12, 3, 20, 10));
    private JButton btn_start = new JButton("Start");
    private JButton btn_stop = new JButton("Stop");

    // 遊戲地圖
    private int colMap = 16; // 16是因為4*4的方塊在map上最大最小值會左右各超過兩格
    private int rowMap = 22; // y+2是下面超過的部分
    private int mapGame[][] = new int[colMap][rowMap];
    // 方塊大小
    private int squareW = 15;
    // 座標
    private int x = 2; // 起始座標是115
    private int y = 1; // 起始座標是115

    private int shapesX = 0; // 表示方塊
    private int shapesY = 0; // 表示狀態
    private Timer timer;
    private TimerTask task;

    private final int shapes[][][] = new int[][][] { // 方塊表示，用這個建立4*4的方塊，且不能被改動
            // T型
            { { 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 } },
            // 長方形
            { { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
                    { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 } },
            // 反閃電
            { { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 } },
            // 閃電
            { { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 } },
            // 相反l
            { { 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 }, { 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
            // l
            { { 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
            // 正方形
            { { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } } };

    private void init() {
        this.setBounds(300, 100, 600, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(jpnt, BorderLayout.WEST);
        cp.add(jpnr, BorderLayout.EAST);
        jpnr.add(btn_start);
        jpnr.add(btn_stop);

        this.addWindowFocusListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        btn_start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
                createShapes();
                requestFocus(); // 重新取得焦點
            }
        });
        btn_stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopGame();
            }
        });
        this.addKeyListener(new KeyListener() { // 按下
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP)
                    trunShapes();
                display();
                System.out.print("x=" + x + "," + "y=" + y + "\n");
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    y += 1;
                System.out.print("x=" + x + "," + "y=" + y + "\n");
                display();
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    x -= 1;
                System.out.print("x=" + x + "," + "y=" + y + "\n");
                display();
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    x += 1;
                System.out.print("x=" + x + "," + "y=" + y + "\n");
                display();
            }

            public void keyReleased(KeyEvent e) { // 放開
            }

            public void keyTyped(KeyEvent e) { // 按住與放開
            }
        });
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("Lost Focus");
            }

            public void focusGained(FocusEvent e) {
                System.out.println("Gain Focus");
            }

        });
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false); // 禁用tab 上下左右
    }

    public GameMain() {
        init(); // 建構畫面
        initMap(); // 繪圖邊界
    }

    public void display() {
        // 重畫JPanel
        this.repaint();
    }

    public void checkLine() { // 判斷
        // if (y >= 18) { // 到底劃出方塊
        // for (int i = 0; i < 4; i++) {
        // for (int j = 0; j < 4; j++) {
        // if (shapes[shapesX][shapesY][(4 * i) + j] == 1) { // 劃出4*4的方塊
        // mapGame[(x - 1) + i][(y - 2) + j] = 1;
        // }
        // }
        // }
        // createShapes();
        // System.out.print(shapesX + "\n" + shapesY);
        // x = 4;
        // y = 0;
        // }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (mapGame[x + i][y + j + 1] == 1 || mapGame[x + i][y + j + 1] == 2) { // 如果下一格有方塊，
                    if (shapes[shapesX][shapesY][(4 * i) + j] == 1) {
                        mapGame[(x - 1) + i][(y - 2) + j] = 1;
                    }
                    createShapes();
                    System.out.print(shapesX + "\n" + shapesY);
                    y = 0;
                }
                if (mapGame[x + i + 1][y] == 1) { // 如果左邊有方塊
                    System.out.println("超出範圍");
                }

            }
        }

    }

    public void startGame() { // 開始遊戲 用於每秒下落方塊
        timer = new Timer("task");
        task = new TimerTask() {
            public void run() {
                y += 1;
                System.out.print("x座標:" + x + "\n");
                System.out.print("y座標:" + y + "\n");
                checkLine();
                display();
            }

        };
        timer.schedule(task, 2300, 2300); // 控制開始時間跟落下速度

    }

    public void stopGame() {
        timer.cancel();
    }

    public void createShapes() { // 創建方塊

        Random ran = new Random();
        shapesX = ran.nextInt(7);
        shapesY = 0;
    }

    public void initMap() { // 初始地圖
        for (int i = 0; i < colMap; i++) {
            mapGame[i][21] = 2; // 最後一行是地圖邊界id=2 //這裡陣列一直超過索引，忘記陣列從0算
        }
        for (int j = 0; j < rowMap; j++) {
            mapGame[1][j] = 2;
            mapGame[14][j] = 2;
        }
    }

    public void restartGame() {

    }

    public void trunShapes() {
        shapesY += 1;
        if (shapesY == 3) {
            shapesY = 0;
        }
    }

    public void paint(Graphics g) { // 繪圖

        super.paint(g);
        // g.drawLine(100, 100, 150, 150); 畫一條直線,從座標(100,100)到(150,150)
        // 空心矩形的坐标及其长宽
        // g.drawRect(30, 40, 80, 60);
        // //实心矩形的坐标及其长宽
        // g.fillRect(140, 40, 80, 60);
        for (int i = 0; i < colMap; i++) { // 0-11 col
            for (int j = 0; j < rowMap; j++) { // 0-19 row
                if (mapGame[i][j] == 2) {
                    g.drawRect(100 + (squareW * (i + 1)), 100 + (squareW * (j + 1)), squareW, squareW);
                }
            }
        }
        for (int i = 0; i < colMap; i++) { // 0-11 col
            for (int j = 0; j < rowMap; j++) { // 0-19 row
                if (mapGame[i][j] == 1) {
                    g.fillRect(115 + (squareW * (i + 1)), 115 + (squareW * (j + 1)), squareW, squareW);
                }
            }
        }
        for (int i = 0; i < 4; i++) { // 迴圈16次，但是每當i跑一圈`,j的值不能歸零，例如i=1,j=3 直要是6
            for (int j = 0; j < 4; j++) { // 公式>>4i+j
                if (shapes[shapesX][shapesY][4 * i + (j)] == 1) { //
                    g.fillRect(115 + (x * squareW) + (squareW * i), 115 + (y * squareW) + (squareW * j), squareW,
                            squareW);
                }
            }
        }

    }
}
