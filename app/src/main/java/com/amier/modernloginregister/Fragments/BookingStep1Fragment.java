package com.amier.modernloginregister.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amier.modernloginregister.Adapter.MyCentersAdapter;
import com.amier.modernloginregister.Common.Common;
import com.amier.modernloginregister.Common.SpacesItemDecoration;
import com.amier.modernloginregister.Interface.IAllCitiesLoadListener;
import com.amier.modernloginregister.Interface.IBranchLoadListener;
import com.amier.modernloginregister.R;
import com.amier.modernloginregister.model.Centers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements IAllCitiesLoadListener, IBranchLoadListener {

    //variable
    CollectionReference allCitiesRef;
    CollectionReference branchRef;

    IAllCitiesLoadListener iAllCitiesLoadListener;
    IBranchLoadListener iBranchLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_cities)
    RecyclerView recycler_cities;

    Unbinder unbinder;
    AlertDialog dialog;

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allCitiesRef = FirebaseFirestore.getInstance().collection("AllCities");
        iAllCitiesLoadListener = this;
        iBranchLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView =  inflater.inflate(R.layout.fragment_booking_step_one,container,false);
        unbinder = ButterKnife.bind(this,itemView);

        LoadAllCities();
        initView();

        return itemView;
    }

    private void initView() {
        recycler_cities.setHasFixedSize(true);
        recycler_cities.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_cities.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void LoadAllCities()
    {
        allCitiesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<String> list = new ArrayList<>();
                            list.add("Please Select City");
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllCitiesLoadListener.onAllCitiesLoadSuccess(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllCitiesLoadListener.onAllCitiesLoadFailed(e.getMessage());
            }
        });
    }

    
    public void onAllCitiesLoadSuccess(List<String> areaNameList) {
        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0){
                    loadBranchOfCity(item.toString());
                }
                else
                {
                    recycler_cities.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadBranchOfCity(String cityName) {
            dialog.show();
            Common.city = cityName;
            branchRef = FirebaseFirestore.getInstance()
                    .collection("AllCities")
                    .document(cityName)
                    .collection("Branch");

            branchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<Centers> list = new ArrayList<>();
                    if(task.isSuccessful())
                    {
                        for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                            //list.add(documentSnapshot.toObject(Centers.class));
                        {
                            Centers centers = documentSnapshot.toObject(Centers.class);
                            //list.add(documentSnapshot.toObject(Centers.class));
                            centers.setCenterId(documentSnapshot.getId());
                            list.add(centers);
                        }
                        iBranchLoadListener.onBranchLoadSuccess(list);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iBranchLoadListener.onBranchLoadFailed(e.getMessage());
                }
            });
    }

    public void onAllCitiesLoadFailed(String message) {
        Toast.makeText(getActivity(), "message", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBranchLoadSuccess(List<Centers> centersList)
    {
        MyCentersAdapter adapter = new MyCentersAdapter(getActivity(),centersList);
        recycler_cities.setAdapter(adapter);
        recycler_cities.setVisibility(View.VISIBLE);
        dialog.dismiss();
    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(getActivity(), "message", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
