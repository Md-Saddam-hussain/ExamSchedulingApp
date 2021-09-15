package com.amier.modernloginregister.Interface;

import java.util.List;

public interface IAllCitiesLoadListener {
        void onAllCitiesLoadSuccess(List<String> areaNameList);
        void onAllCitiesLoadFailed(String message);
}
