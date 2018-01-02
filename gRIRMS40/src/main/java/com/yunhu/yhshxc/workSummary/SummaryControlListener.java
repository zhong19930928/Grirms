package com.yunhu.yhshxc.workSummary;

import com.yunhu.yhshxc.workSummary.view.SummaryPhotoView;
import com.yunhu.yhshxc.workSummary.view.SummaryPhotoViewPreview;
import com.yunhu.yhshxc.workSummary.view.SummaryVoiceView;

public interface SummaryControlListener {

    public void contentControl (SummaryPhotoView photoView);
    
    public void contentVoiceControl(SummaryVoiceView voiceView);
    public void contentControl (SummaryPhotoViewPreview photoView);
    
//    public void contentVoiceControl(SummaryVoiceView voiceView);
    
    
}
