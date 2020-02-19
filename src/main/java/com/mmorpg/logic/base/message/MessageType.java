package com.mmorpg.logic.base.message;

/**
 * 消息提示类型
 *
 * @author Ariescat
 * @version 2020/2/19 12:09
 */
public enum MessageType {

    /**
     * 跟随提示, 提示窗口会跟随当前鼠标位置
     */
    @Deprecated
    TIPS(1, " 跟随提示"),
    /**
     * 系统成功, 在正下方位置
     */
    SUCCEED(2, "操作成功提示"),
    /**
     * 系统警告,在正下方位置
     */
    WARN(3, "警告"),
    /**
     * 系统错误,在正下方问题
     */
    ERROR(4, "错误"),
    /**
     * 全服公告,走马灯
     */
    ANNOUINCEMENT(5, "全服公告"),
    ;

    private int code;
    private String desc;

    private MessageType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
