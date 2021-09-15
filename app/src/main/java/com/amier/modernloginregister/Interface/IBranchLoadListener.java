package com.amier.modernloginregister.Interface;

import com.amier.modernloginregister.model.Centers;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Centers> centersList);
    void onBranchLoadFailed(String message);
}
