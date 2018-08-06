package com.bdwater.assetmanagement.model;

/**
 * Created by hegang on 17/11/22.
 */

public class TroubleNoteDetailStatus {
    public static final int PROCESSING = 0;
    public static final int DONE = 1;
    public static final int RESPONSE = 2;
    public static final int REJECT = 3;
    public static String getName(int status) {
        switch (status) {
            case PROCESSING: return "进行中...";
            case DONE: return "完成";
            case RESPONSE: return "已回复";
            case REJECT: return "驳回";
            default:
                return "";
        }
    }
}
