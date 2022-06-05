package syn;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JoinDemo {
    public static final int SLEEP_GAP = 500;
    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }

    static class HotWaterThread extends Thread {
        public HotWaterThread(){
            super("** 烧水-Thread");
        }
        public void run(){
            log.info("洗好水壶");
            log.info("灌上凉水");
            log.info("放在火上");
            try {
                Thread.sleep(SLEEP_GAP);
                log.info("水开了");
            } catch (InterruptedException e) {
                log.info("发生异常被中断");
            }
            log.info("运行结束。");
        }
    }

    static class WashThread extends Thread {
        public WashThread(){
            super("$$清洗-Thread");
        }
        public void run(){
            log.info("洗茶壶");
            log.info("洗水杯");
            log.info("拿茶叶");
            try {
                Thread.sleep(SLEEP_GAP);
                log.info("洗完了");
            } catch (InterruptedException e) {
                log.info("发生异常被中断");
            }
            log.info("运行结束");
        }
    }

    public static void main(String[] args) {
        Thread hThread = new HotWaterThread();
        Thread wThread = new WashThread();

        try {
            hThread.start();
            hThread.join();
            wThread.start();
            wThread.join();
            Thread.currentThread().setName("主线程");
            log.info("泡茶喝");
        } catch (InterruptedException e) {
            log.info(getCurThreadName() + "发生异常中断");
        }
        log.info(getCurThreadName() + "运行结束");
    }
}
