package com.dpizarro.libraries.autolabelui;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.Label;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dpizarro
 */
public class RecyclerViewFragment extends Fragment {

    private AutoLabelUI mAutoLabel;
    private List<Person> mPersonList;
    private MyRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }

    public RecyclerViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_fragment, container, false);
        findViews(view);
        setListeners();
        setRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState!=null){
            List<Person> people = savedInstanceState.getParcelableArrayList("statePeople");
            if(people != null){
                mPersonList = people;
                adapter.setPeople(people);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private void itemListClicked(int position) {
        Person person = mPersonList.get(position);
        boolean isSelected = person.isSelected();
        boolean success;
        if (isSelected) {
            success = mAutoLabel.removeLabel(position);
        } else {
            success = mAutoLabel.addLabel(person.getName(), position);
        }
        if (success) {
            adapter.setItemSelected(position, !isSelected);
        }
    }

    private void setListeners() {
        mAutoLabel.setOnLabelsCompletedListener(new AutoLabelUI.OnLabelsCompletedListener() {
            @Override
            public void onLabelsCompleted() {
                Toast.makeText(getActivity(), "Completed!", Toast.LENGTH_SHORT).show();
            }
        });

        mAutoLabel.setOnRemoveLabelListener(new AutoLabelUI.OnRemoveLabelListener() {
            @Override
            public void onRemoveLabel(View view, int position) {
                adapter.setItemSelected(position, false);
            }
        });

        mAutoLabel.setOnLabelsEmptyListener(new AutoLabelUI.OnLabelsEmptyListener() {
            @Override
            public void onLabelsEmpty() {
                Toast.makeText(getActivity(), "EMPTY!", Toast.LENGTH_SHORT).show();
            }
        });

        mAutoLabel.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(View v) {
                Toast.makeText(getActivity(), ((Label) v).getText() , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void findViews(View view) {
        mAutoLabel = (AutoLabelUI) view.findViewById(R.id.label_view);
        mAutoLabel.setBackgroundResource(R.drawable.round_corner_background);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        mPersonList = new ArrayList<>();

        //Populate list
        List<String> names = Arrays.asList(getResources().getStringArray(R.array.names));
        List<String> ages = Arrays.asList(getResources().getStringArray(R.array.ages));
        TypedArray photos = getResources().obtainTypedArray(R.array.photos);

        for(int i = 0; i<names.size(); i++){
            mPersonList.add(new Person(names.get(i), ages.get(i), photos.getResourceId(i, -1), false));
        }

        photos.recycle();

        adapter = new MyRecyclerAdapter(mPersonList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                itemListClicked(position);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("statePeople",
                (ArrayList<? extends Parcelable>) adapter.getPeople());

    }
}
