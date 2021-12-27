package com.example.CrazyRoadGame.model.records;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.CrazyRoadGame.MSP;
import com.example.CrazyRoadGame.MyDB;
import com.example.CrazyRoadGame.R;
import com.google.gson.Gson;

import java.util.ArrayList;


public class ListFragment extends Fragment implements CallBack_ListFrag {

    //the inner ArrayList is of a row (whole Record)
    //the external ArrayList is like ArrayList<Record>
    private ArrayList<ArrayList<TextView>> fragRec_TXT_list;
    private ArrayList<ImageButton> fragRec_IMB_listLocation;

    private RecordsActivity activity_RA;

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        activity_RA.setCallBack_LF(this);

        findViews(view);
        initViews();


        return view;
    }

    private void findViews(View view) {
        fragRec_TXT_list = new ArrayList<ArrayList<TextView>>(MyDB.getInstance().get_MAX_RECORDS());

        ArrayList<TextView> rList0 = new ArrayList<TextView>();
        rList0.add(view.findViewById(R.id.fragRec_TXT_date0));
        rList0.add(view.findViewById(R.id.fragRec_TXT_name0));
        rList0.add(view.findViewById(R.id.fragRec_TXT_score0));
        fragRec_TXT_list.add(rList0);

        ArrayList<TextView> rList1 = new ArrayList<TextView>();
        rList1.add(view.findViewById(R.id.fragRec_TXT_date1));
        rList1.add(view.findViewById(R.id.fragRec_TXT_name1));
        rList1.add(view.findViewById(R.id.fragRec_TXT_score1));
        fragRec_TXT_list.add(rList1);

        ArrayList<TextView> rList2 = new ArrayList<TextView>();
        rList2.add(view.findViewById(R.id.fragRec_TXT_date2));
        rList2.add(view.findViewById(R.id.fragRec_TXT_name2));
        rList2.add(view.findViewById(R.id.fragRec_TXT_score2));
        fragRec_TXT_list.add(rList2);

        ArrayList<TextView> rList3 = new ArrayList<TextView>();
        rList3.add(view.findViewById(R.id.fragRec_TXT_date3));
        rList3.add(view.findViewById(R.id.fragRec_TXT_name3));
        rList3.add(view.findViewById(R.id.fragRec_TXT_score3));
        fragRec_TXT_list.add(rList3);

        ArrayList<TextView> rList4 = new ArrayList<TextView>();
        rList4.add(view.findViewById(R.id.fragRec_TXT_date4));
        rList4.add(view.findViewById(R.id.fragRec_TXT_name4));
        rList4.add(view.findViewById(R.id.fragRec_TXT_score4));
        fragRec_TXT_list.add(rList4);

        ArrayList<TextView> rList5 = new ArrayList<TextView>();
        rList5.add(view.findViewById(R.id.fragRec_TXT_date5));
        rList5.add(view.findViewById(R.id.fragRec_TXT_name5));
        rList5.add(view.findViewById(R.id.fragRec_TXT_score5));
        fragRec_TXT_list.add(rList5);

        ArrayList<TextView> rList6 = new ArrayList<TextView>();
        rList6.add(view.findViewById(R.id.fragRec_TXT_date6));
        rList6.add(view.findViewById(R.id.fragRec_TXT_name6));
        rList6.add(view.findViewById(R.id.fragRec_TXT_score6));
        fragRec_TXT_list.add(rList6);

        ArrayList<TextView> rList7 = new ArrayList<TextView>();
        rList7.add(view.findViewById(R.id.fragRec_TXT_date7));
        rList7.add(view.findViewById(R.id.fragRec_TXT_name7));
        rList7.add(view.findViewById(R.id.fragRec_TXT_score7));
        fragRec_TXT_list.add(rList7);

        ArrayList<TextView> rList8 = new ArrayList<TextView>();
        rList8.add(view.findViewById(R.id.fragRec_TXT_date8));
        rList8.add(view.findViewById(R.id.fragRec_TXT_name8));
        rList8.add(view.findViewById(R.id.fragRec_TXT_score8));
        fragRec_TXT_list.add(rList8);

        ArrayList<TextView> rList9 = new ArrayList<TextView>();
        rList9.add(view.findViewById(R.id.fragRec_TXT_date9));
        rList9.add(view.findViewById(R.id.fragRec_TXT_name9));
        rList9.add(view.findViewById(R.id.fragRec_TXT_score9));
        fragRec_TXT_list.add(rList9);

        fragRec_IMB_listLocation = new ArrayList<>(MyDB.getInstance().get_MAX_RECORDS());
        fragRec_IMB_listLocation.add(view.findViewById(R.id.fragRec_BTN_location0));
        fragRec_IMB_listLocation.add(view.findViewById(R.id.fragRec_BTN_location1));
        fragRec_IMB_listLocation.add(view.findViewById(R.id.fragRec_BTN_location2));
        fragRec_IMB_listLocation.add(view.findViewById(R.id.fragRec_BTN_location3));
        fragRec_IMB_listLocation.add(view.findViewById(R.id.fragRec_BTN_location4));
        fragRec_IMB_listLocation.add(view.findViewById(R.id.fragRec_BTN_location5));
        fragRec_IMB_listLocation.add(view.findViewById(R.id.fragRec_BTN_location6));
        fragRec_IMB_listLocation.add(view.findViewById(R.id.fragRec_BTN_location7));
        fragRec_IMB_listLocation.add(view.findViewById(R.id.fragRec_BTN_location8));
        fragRec_IMB_listLocation.add(view.findViewById(R.id.fragRec_BTN_location9));

    }

    private void initViews() {
        initRecordTableUI();

        //initialized the button location list(setOnClickListener)
        int i = 0;
        while (i < fragRec_IMB_listLocation.size()) {
            int finalPosition = i;
            fragRec_IMB_listLocation.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity_RA.onMapButtonClicked(finalPosition);
                }
            });
            i++;
        }
    }

    private void initRecordTableUI() {
        for (int i = 0; i < fragRec_TXT_list.size(); i++) {
            if (!MyDB.getInstance().getAllRecords().isEmpty()) {
                if (i < MyDB.getInstance().getAllRecords().size()) {
                    fragRec_TXT_list.get(i).get(0).setText(MyDB.getInstance().getAllRecords().get(i).getTime());
                    fragRec_TXT_list.get(i).get(1).setText(MyDB.getInstance().getAllRecords().get(i).getPlayerName());
                    fragRec_TXT_list.get(i).get(2).setText(MyDB.getInstance().getAllRecords().get(i).getScore() + "");
                    fragRec_IMB_listLocation.get(i).setVisibility(View.VISIBLE);
                }
            } else {
                fragRec_TXT_list.get(i).get(0).setText("");
                fragRec_TXT_list.get(i).get(1).setText("");
                fragRec_TXT_list.get(i).get(2).setText("");
                fragRec_IMB_listLocation.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void addNewRecord(Record record) {
        MyDB.getInstance().setNewRecord(record);
        initRecordTableUI();
        loadUpdateToMSP();
    }

    private void loadUpdateToMSP() {
        String json = new Gson().toJson(MyDB.getInstance());
        MSP.getInstance().putStringSP("my_db", json);
    }

    public void setActivity(RecordsActivity recordsActivity) {
        this.activity_RA = recordsActivity;
    }

}
