package com.bdwater.assetmanagement.soap;

import android.support.annotation.Nullable;

import com.bdwater.assetmanagement.common.NetworkUtils;
import com.yanzhenjie.recyclerview.swipe.widget.StickyNestedScrollView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by hegang on 17/10/18.
 */

public class SoapClient {
    static final String NAMESPACE = "http://tempuri.org/";
    public static final String LOGIN_METHOD = "Login";
    public static final String CHECK_LOGIN_METHOD = "CheckLogin";

    public static final String GET_DEVICE_LIST_METHOD = "GetDeviceListOrderByDate";
    public static final String GET_DEVICE_LIST_BY_SEARCH_METHOD = "GetDeviceListBySearch";
    public static final String GET_DEVICE_BY_ID_METHOD = "GetDeviceByID";
    public static final String GET_SERVICES_BY_DEVICE_ID_METHOD = "GetServicesByDeviceID";
    public static final String GET_TROUBLE_NOTES_METHOD = "GetTroubleNotes";
    public static final String GET_SITES_METHOD = "GetSites";
    public static final String UPDATE_TROUBLE_NOTE_METHOD = "UpdateTroubleNote";
    public static final String PUBLISH_TROUBLE_NOTE_METHOD = "PublishTroubleNote";
    public static final String UPDATE_TROUBLE_NOTE_DETAIL_METHOD = "UpdateTroubleNoteDetail";
    public static final String UPDATE_RESPONSE_METHOD = "UpdateResponse";

    public static final String UPLOAD_IMAGE_METHOD = "UploadImage";
    public static final String UPLOAD_IMAGE_PART_METHOD = "UploadImagePart";

    public static String Login(String username, String password) throws IOException, XmlPullParserException {
        return CallMethod(LOGIN_METHOD,
                new String[] {"username", "password"},
                new Object[] {username, password});
    }
    public static String CheckLogin(String userIDString) throws IOException, XmlPullParserException {
        return CallMethod(CHECK_LOGIN_METHOD,
                new String[] {"userIDString"},
                new Object[] {userIDString});
    }

    public static String GetDeviceListOrderByDate(String userID, int offset, int size) throws IOException, XmlPullParserException {
        return CallMethod(GET_DEVICE_LIST_METHOD,
                new String[] {"userID", "offset", "size"},
                new Object[] {userID, offset, size});
    }
    public static String GetDeviceListBySearch(String userID, String keyword) throws IOException, XmlPullParserException {
        return CallMethod(GET_DEVICE_LIST_BY_SEARCH_METHOD,
                new String[] {"userID", "keyword"},
                new Object[] {userID, keyword});
    }
    public static String GetDeviceByID(String userID, String deviceIDString) throws IOException, XmlPullParserException {
        return CallMethod(GET_DEVICE_BY_ID_METHOD,
                new String[] {"userID", "deviceIDString"},
                new Object[] {userID, deviceIDString});
    }
    public static String GetServicesByDeviceID(String deviceIDString) throws IOException, XmlPullParserException {
        return CallMethod(GET_SERVICES_BY_DEVICE_ID_METHOD,
                new String[] {"deviceIDString"},
                new Object[] {deviceIDString});
    }
    public static String GetTroubleNotes(String userID, String status, String offset, String size) throws IOException, XmlPullParserException {
        return CallMethod(GET_TROUBLE_NOTES_METHOD,
                new String[] {"userID", "status", "offset", "size"},
                new Object[] {userID, status, offset, size});
    }
    public static String GetSites() throws IOException, XmlPullParserException {
        return CallMethod(GET_SITES_METHOD,
                new String[] {},
                new Object[] {});
    }
    public static String UpdateTroubleNote(int mode, String troubleNoteJsonString) throws IOException, XmlPullParserException {
        return CallMethod(UPDATE_TROUBLE_NOTE_METHOD,
                new String[] {"mode", "troubleNoteJsonString"},
                new Object[] {mode, troubleNoteJsonString});
    }
    public static String PublishTroubleNote(String troubleNoteIDString) throws IOException, XmlPullParserException {
        return CallMethod(PUBLISH_TROUBLE_NOTE_METHOD,
                new String[] {"troubleNoteIDString"},
                new Object[] {troubleNoteIDString});
    }
    public static String UpdateTroubleNoteDetail(int mode, String troubleNoteDetailJsonString) throws IOException, XmlPullParserException {
        return CallMethod(UPDATE_TROUBLE_NOTE_DETAIL_METHOD,
                new String[] {"mode", "troubleNoteDetailJsonString"},
                new Object[] {mode, troubleNoteDetailJsonString});
    }
    public static String UpdateResponse(int mode, int topStatus, String troubleNoteDetailJsonString) throws IOException, XmlPullParserException {
        return CallMethod(UPDATE_RESPONSE_METHOD,
                new String[] {"mode", "topStatus", "troubleNoteDetailJsonString"},
                new Object[] {mode, topStatus, troubleNoteDetailJsonString});
    }
    public static String UploadImage(String imageBase64) throws IOException, XmlPullParserException {
        return CallMethod(UPLOAD_IMAGE_METHOD,
                new String[] {"imageBase64"},
                new Object[] {imageBase64});
    }
    public static String UploadImagePart(String fileName, String imageBase64) throws IOException, XmlPullParserException {
        return CallMethod(UPLOAD_IMAGE_PART_METHOD,
                new String[] {"fileName", "imageBase64"},
                new Object[] {fileName, imageBase64});
    }

    @Nullable
    public static String CallMethod(String methodName, String[] parameterNames, Object[] parameters) throws IOException, XmlPullParserException
    {
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        if(parameterNames != null)
        for (int i = 0; i < parameterNames.length; i++) {
            request.addProperty(parameterNames[i], parameters[i]);
        }

        // 创建SoapSerializationEnvelope对象并传入SOAP协议的版本号
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        // 设置与.NET提供的Web service保持有良好的兼容性
        envelope.dotNet = true;

        // 创建HttpTransportSE传说对象 传入webservice服务器地址
        HttpTransportSE ht = new HttpTransportSE(NetworkUtils.WEBSERVICE_URL);
        ht.debug = true;
        ht.call(NAMESPACE + methodName, envelope);

        if(envelope.getResponse() != null) {
            SoapObject result = (SoapObject) envelope.bodyIn;
            SoapPrimitive sp = (SoapPrimitive)result.getProperty(0);
            return sp.getValue().toString();
        }
        return null;
    }
}
