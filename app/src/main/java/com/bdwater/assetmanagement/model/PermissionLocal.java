package com.bdwater.assetmanagement.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hegang on 17/11/23.
 */

public class PermissionLocal {
    private static PermissionLocal instance;
    private void PermissionLocal() {}
    public static PermissionLocal instance() {
        if(instance == null)
            instance = new PermissionLocal();
        return instance;
    }

    public List<ModelPermission> ModelPermissions = new ArrayList<>();
    public boolean get(ModelList model, ActionList action) {
        return true;
    }

    public class ModelPermission {
        public ModelList name;
        public ActionList action;
        public boolean value;
    }

    public enum ModelList {
        Device,
        Question,
        Draft,
    }

    public enum ActionList {
        QuestionCreate,
        QuestionEdit,
        DraftEdit,
        DraftEditOnlyOwner,
        DraftRead,
        DraftReadOnlyOwner,
    }
}
