package com.bdwater.assetmanagement.common;

import android.support.v4.app.Fragment;

import com.bdwater.assetmanagement.CApplication;
import com.bdwater.assetmanagement.model.Permission;
import com.bdwater.assetmanagement.model.PermissionLocal;
import com.bdwater.assetmanagement.model.User;

/**
 * Created by hegang on 17/11/24.
 */

public class ExtFragment extends Fragment {
    protected CApplication getApplication() {
        return (CApplication)getActivity().getApplication();
    }
    protected PermissionLocal getPermissionManager() {
        return ((CApplication)getActivity().getApplication()).PermissionManager;
    }
    protected User getUser() {
        return ((CApplication)getActivity().getApplication()).getUser();
    }
}
