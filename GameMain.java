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
    private JButton btn_restart = new JButton("重新開始");
    private JLabel jab_point = new JLabel("point");
    private JLabel jLabel_dif = new JLabel("難度調整(建議100~1000)");
    private JTextArea jtex_dif = new JTextArea("500");

    // 遊戲地圖
    private int colMap = 16; // 16是因為4*4的方塊在map上最大最小值會左右各超過兩格
    private int rowMap = 22; // y+2是下面超過的部分
    private int mapGame[][] = new int[colMap][rowMap];
    // 方塊大小
    private int squareW = 15;
    // 座標
    private int x = 5; //
    private int y = 1; //
    // 分數
    private int point = 0;
    // 計數器
    private int count;
    private int dif = 500;
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
            { { 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
                    { 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
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
        jpnr.add(btn_restart);
        jpnr.add(jLabel_dif);
        jpnr.add(jtex_dif);

        this.addWindowFocusListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        btn_start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dif = Integer.valueOf(jtex_dif.getText());
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
        btn_restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        this.addKeyListener(new KeyListener() { // 按下
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP)
                    trunShapes();
                display();
                System.out.print("x=" + x + "," + "y=" + y + "\n");
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    checkLine();
                    y += 1;

                }
                System.out.print("x=" + x + "," + "y=" + y + "\n");
                display();
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    x -= 1;
                    if (x < 2) {
                        x = 2;
                    }
                    for (int i = 3; i >= 0; i--) {
                        for (int j = 3; j >= 0; j--) {
                            if (shapes[shapesX][shapesY][(4 * i) + j] == 1) {
                                if (mapGame[x + i - 1][y + j] == 1) { // 當左邊有方塊十
                                    System.out.print("碰到左");
                                    break;
                                }
                            }
                        }
                    }
                    System.out.print("x=" + x + "," + "y=" + y + "\n");
                    display();
                }

                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    x += 1;
                    if (x > 12) {
                        x = 12;
                    }
                    for (int i = 0; i < 4; i++) {
                        for (int j = 3; j >= 0; j--) {
                            if (shapes[shapesX][shapesY][(4 * i) + j] == 1) {
                                if (mapGame[x + i + 1][y + j] == 1) { // 右邊有方塊時
                                    System.out.print("碰到右");

                                    break;
                                }
                            }
                        }
                    }
                    System.out.print("x=" + x + "," + "y=" + y + "\n");
                    display();
                }
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
        for (int i = 0; i < 4; i++) {
            for (int j = 3; j >= 0; j--) {
                if (shapes[shapesX][shapesY][(4 * i) + j] == 1) { // 如果從當前方塊找到1
                    if (mapGame[x + i][y + 1 + j] == 2 || mapGame[x + i][y + 1 + j] == 1) { // 就看地圖下面那格是1或2
                        for (int k = 0; k < 4; k++) {
                            for (int l = 0; l < 4; l++) {
                                if (shapes[shapesX][shapesY][(4 * k) + l] == 1) { // 寫進地圖
                                    mapGame[x + k][y + l] = 1;
                                }
                            }
                        }
                        // 到底部要執行的寫在這裡
                        removeLine();
                        createShapes();
                        x = 5;
                        y = 1;
                    }
                }
            }
        }
    }

    public void removeLine() { // 專門用來消除
        for (int i = 19; i >= 1; i--) { // 從下往上看map
            count = 0; // 每次迴圈下一行就會計數器歸零
            for (int j = 2; j < 14; j++) {
                if (mapGame[j][i] == 1) {
                    count += 1;
                    // System.out.println("消除消除" + count);
                }
                if (count == 12) {
                    for (int row = i; row >= 1; row--) { // 迴圈
                        for (int col = 2; col < 14; col++) {
                            mapGame[col][row] = mapGame[col][row - 1]; // 把當前y軸的每個格子，放入他上面的那欄
                        }
                    }
                    point += 1;
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
        timer.schedule(task, 300, dif); // 控制開始時間跟落下速度
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
            mapGame[i][20] = 2; // 最後一行是地圖邊界id=2 //這裡陣列一直超過索引，忘記陣列從0算
        }
        for (int j = 0; j < rowMap; j++) {
            mapGame[1][j] = 2;
            mapGame[14][j] = 2;
        }
        for (int i = 2; i <= 13; i++) {
            for (int j = 0; j <= 19; j++) {
                mapGame[i][j] = 0;
            }
        }
    }

    public void restartGame() {
        stopGame();
        initMap();
        display();
        x = 2;
        y = 1;
    }

    public void trunShapes() {
        shapesY += 1;
        if (shapesY == 4) {
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
                    g.fillRect(100 + (squareW * (i + 1)), 100 + (squareW * (j + 1)), squareW, squareW);
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
