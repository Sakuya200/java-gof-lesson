package lesson3;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 里氏替换原则--温控器类
 * @author Sakuya200
 * @since 2026/10/21
 */
public class TemperatureController {

    private int temperature = 15;

    /**
     * <ul>
     *   <li>功能：设置控温器温度</li>
     *   <li>前置条件：setTemperature 只接受 [10, 20] 的整数；凡是落在此区间内的值都必须被接受</li>
     *   <li>异常约定：入参越界时、且仅在此时，抛出 IllegalArgumentException</li>
     *   <li>后置条件：设置成功后，getTemperature() 必须原样返回刚才设置的值</li>
     * </ul>
     * @param celsius 要设置的温度，必须在 [10, 20] 区间内
     */
    public void setTemperature(int celsius) {
        if (celsius < 10 || celsius > 20) {
            throw new IllegalArgumentException("温度必须在 [10, 20] 区间内，实际=" + celsius);
        }
        this.temperature = celsius;
    }

    /**
     * <ul>
     *   <li>功能：获取控温器温度</li>
     *   <li>前置条件：无</li>
     *   <li>异常约定：无</li>
     *   <li>后置条件：返回值必须是最近一次成功设置的温度</li>
     * </ul>
     * @return 最近一次成功设置的温度
     */
    public int getTemperature() {
        return temperature;
    }


    /**
     * 调用方代码
     */
    static class Client {
        /** 这段代码有权假设：25 一定会被拒绝、被拒绝一定是 IllegalArgumentException、成功后一定读回同值 */
        public static void run(TemperatureController controller) {
            try {
                controller.setTemperature(25);
                System.out.println("设置成功，当前=" + controller.getTemperature());
            } catch (IllegalArgumentException e) {
                System.out.println("入参越界，已拒绝：" + e.getMessage());
            }
        }
    }


    /**
     * 正例：入参合法范围、异常类型、set/get 语义全部原样继承。
     * 子类只增加父类从未承诺过的能力，调用方替换前后行为完全一致。
     */
    public class AuditableTemperatureController extends TemperatureController {

        private final List<String> auditLog = new ArrayList<>();

        @Override
        public void setTemperature(int celsius) {
            super.setTemperature(celsius);   // 校验与异常全权交给父类：不加新限制，也不吞异常
            auditLog.add(LocalDateTime.now() + " 设置为 " + celsius);
        }

        /** 新增能力，父类没承诺过，因此调用方也不会依赖它 */
        public List<String> getAuditLog() {
            return Collections.unmodifiableList(auditLog);
        }
    }

    /**
     * 反例 A：加强前置条件 —— 父类承诺 10~20 全部合法，子类却拒收其中的奇数
     */
    static class EvenOnlyTemperatureController extends TemperatureController {
        @Override
        public void setTemperature(int celsius) {
            if (celsius % 2 != 0) {
                throw new IllegalArgumentException("本机型只支持偶数温度");
            }
            super.setTemperature(celsius);
        }
    }

    /**
     * 反例 B：削弱后置条件 —— 越界时静默忽略，调用方失去了感知失败的唯一手段
     */
    public class QuietTemperatureController extends TemperatureController {
        @Override
        public void setTemperature(int celsius) {
            if (celsius < 10 || celsius > 20) {
                return;   // 契约要求抛 IllegalArgumentException，这里却"假装成功"
            }
            super.setTemperature(celsius);
        }
    }

    /**
     * 反例 C：篡改已承诺的行为 —— get 不再返回 set 进去的值
     */
    public class ReportTemperatureController extends TemperatureController {
        @Override
        public int getTemperature() {
            return super.getTemperature() * 10;   // 为了上报方便放大 10 倍，20 变成 200
        }
    }
}
