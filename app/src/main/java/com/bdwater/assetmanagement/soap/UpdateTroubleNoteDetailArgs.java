package com.bdwater.assetmanagement.soap;

import com.bdwater.assetmanagement.model.Image;
import com.bdwater.assetmanagement.model.TroubleNoteDetail;

/**
 * Created by hegang on 17/11/20.
 */

public class UpdateTroubleNoteDetailArgs {
    public TroubleNoteDetail entry;
    public Image[] images;
    public int progressLength;
    public int progressDoneLength;
    public int mode;
}
