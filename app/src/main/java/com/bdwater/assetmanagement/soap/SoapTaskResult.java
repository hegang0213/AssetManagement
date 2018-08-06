package com.bdwater.assetmanagement.soap;

import org.json.JSONObject;

/**
 * Created by hegang on 17/10/31.
 */

public class SoapTaskResult {
    public TaskResultType Result;
    public Integer Code = 0;
    public JSONObject JSONResult;
    public String Message;
    public Exception Exception;
    public SoapTaskResult() {
    }
    public static SoapTaskResult Failed(Integer code, String message) {
        SoapTaskResult result = new SoapTaskResult();
        result.Result = TaskResultType.Failed;
        result.Code = code;
        result.Message = message;
        return result;
    }
    public static SoapTaskResult Success(JSONObject object) {
        SoapTaskResult result = new SoapTaskResult();
        result.Result = TaskResultType.Success;
        result.JSONResult = object;
        return result;
    }
    public static SoapTaskResult Exception(Exception exception) {
        SoapTaskResult result = new SoapTaskResult();
        result.Code = -1;
        result.Result = TaskResultType.Exception;
        result.Exception = exception;
        result.Message = exception.getMessage();
        return result;
    }

    public enum TaskResultType
    {
        Success,
        Failed,
        Exception,
    }
}
