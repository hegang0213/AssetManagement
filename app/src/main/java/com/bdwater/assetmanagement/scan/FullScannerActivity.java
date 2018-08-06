package com.bdwater.assetmanagement.scan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AppCompatSwipeBackActivity;
import com.bdwater.assetmanagement.common.IconicsHelper;
import com.bdwater.assetmanagement.devicemgr.DeviceActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hegang on 17/11/15.
 */
//        implements MessageDialogFragment.MessageDialogListener,
//        ZXingScannerView.ResultHandler, FormatSelectorDialogFragment.FormatSelectorDialogListener,
//        CameraSelectorDialogFragment.CameraSelectorDialogListener
public class FullScannerActivity extends AppCompatSwipeBackActivity implements ZXingScannerView.ResultHandler {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.flashImageView)
    public ImageView flashImageView;
    @BindView(R.id.flashTextView)
    public TextView flashTextView;

    @Override
    public void onCreate(Bundle state) {
        Window window = getWindow();
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        super.onCreate(state);
        setContentView(R.layout.activity_scan_full);
        ButterKnife.bind(this);

        this.initUI(state);
    }

    void initUI(Bundle state) {
        if(state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }

        toolbar.setTitle(""); //R.string.scan_hint);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewGroup contentFrame = (ViewGroup)findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        mFlash = mScannerView.getFlash();
        flashTextView.setText(getFlashTitle(mFlash));
        flashImageView.setImageDrawable(getFlashDrawable(mFlash));
        flashImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlash = !mFlash;
                flashTextView.setText(getFlashTitle(mFlash));
                flashImageView.setImageDrawable(getFlashDrawable(mFlash));
                mScannerView.setFlash(mFlash);
            }
        });

        setupFormats();
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    Drawable getFlashDrawable(boolean flash) {
        if(flash)
            return IconicsHelper.getIcon(this, CommunityMaterial.Icon.cmd_flash, 16, 2, Color.WHITE);
        else
            return IconicsHelper.getIcon(this, CommunityMaterial.Icon.cmd_flash_off, 16, 2, Color.WHITE);
    }
    String getFlashTitle(boolean flash) {
        if(flash)
            return getString(R.string.flash_on);
        else
            return getString(R.string.flash_off);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
//            case R.id.menu_flash:
//                mFlash = !mFlash;
//                item.setIcon(getFlashDrawable(mFlash));
//                mScannerView.setFlash(mFlash);
//                return true;
//            case R.id.menu_auto_focus:
//                mAutoFocus = !mAutoFocus;
//                if(mAutoFocus) {
//                    item.setTitle(R.string.auto_focus_on);
//                } else {
//                    item.setTitle(R.string.auto_focus_off);
//                }
//                mScannerView.setAutoFocus(mAutoFocus);
//                return true;
//            case R.id.menu_formats:
//                DialogFragment fragment = FormatSelectorDialogFragment.newInstance(this, mSelectedIndices);
//                fragment.show(getSupportFragmentManager(), "format_selector");
//                return true;
//            case R.id.menu_camera_selector:
//                mScannerView.stopCamera();
//                DialogFragment cFragment = CameraSelectorDialogFragment.newInstance(this, mCameraId);
//                cFragment.show(getSupportFragmentManager(), "camera_selector");
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {}
        this.checkAndRunDeviceID(rawResult.getText());
    }
    void checkAndRunDeviceID(String result) {
        if(isGuidString(result)) {
            Intent intent = new Intent(this, DeviceActivity.class);
            intent.putExtra(DeviceActivity.DEVICE_ID_STRING, result);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "不能识别的内容", Toast.LENGTH_SHORT).show();
            mScannerView.startCamera();
        }
    }
    boolean isGuidString(String guidString) {
        if(guidString.length() != 36) return false;
        String[] array = guidString.split("-");
        if(array.length != 5) return false;
        if(array[0].length() != 8) return false;
        if(array[1].length() != 4) return false;
        if(array[2].length() != 4) return false;
        if(array[3].length() != 4) return false;
        if(array[4].length() != 12) return false;
        return true;
    }

//    public void showMessageDialog(String message) {
//        DialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
//        fragment.show(getSupportFragmentManager(), "scan_results");
//    }
//
//    public void closeMessageDialog() {
//        closeDialog("scan_results");
//    }
//
//    public void closeFormatsDialog() {
//        closeDialog("format_selector");
//    }
//
//    public void closeDialog(String dialogName) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
//        if(fragment != null) {
//            fragment.dismiss();
//        }
//    }
//
//    @Override
//    public void onDialogPositiveClick(DialogFragment dialog) {
//        // Resume the camera
//        mScannerView.resumeCameraPreview(this);
//    }
//
//    @Override
//    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
//        mSelectedIndices = selectedIndices;
//        setupFormats();
//    }
//
//    @Override
//    public void onCameraSelected(int cameraId) {
//        mCameraId = cameraId;
//        mScannerView.startCamera(mCameraId);
//        mScannerView.setFlash(mFlash);
//        mScannerView.setAutoFocus(mAutoFocus);
//    }
//
    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for(int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for(int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
//        closeMessageDialog();
//        closeFormatsDialog();
    }
}