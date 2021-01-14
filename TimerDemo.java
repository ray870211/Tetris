import java.util.*;

public class TimerDemo {
    public static void main(String[] args) {
        Timer timer;
        TimerTask task;
        timer = new Timer("task");
        task = new TimerTask() {
            public void run() {
                System.out.println("123");
            }
        };
        timer.schedule(task, 2000, 2000); // 兩秒執行，每兩秒在執行一次

    }
}
