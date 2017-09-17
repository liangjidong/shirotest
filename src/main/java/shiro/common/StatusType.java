package shiro.common;

/**
 * Created by author on 2017/9/15.
 * 值>0表示操作成功
 * 值<=0表示操作有问题
 */
public enum StatusType {
    SUCCESS(1, "成功"), EXCEPTION(0, "系统异常"), OPT_INVALID(-1, "操作不合法"), REGISTER_FAILD(-2, "注册失败"), LOGIN_FAILD(-3, "用户名或密码错误"), USER_LOCK(-4, "账号暂时锁定"), OVER_TIMES(-5, "登陆失败次数太多"), TIME_INVALID(-6, "过期"), NOT_ACTIVE(-7, "未激活");
    private int val;
    private String msg;

    private StatusType(int val, String desc) {
        this.val = val;
        this.msg = desc;
    }

    public int getVal() {
        return this.val;
    }

    public String getMessage() {
        return this.msg;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + val + "," + msg + ")";
    }

    public static StatusType value(int val) {
        StatusType[] types = StatusType.values();
        StatusType type = null;
        for (int i = 0; i < types.length; i++) {
            type = types[i];
            if (val == type.getVal())
                break;
        }
        return type;
    }
}
