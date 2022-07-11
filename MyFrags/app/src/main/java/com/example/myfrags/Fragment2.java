package com.example.myfrags;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Fragment2 extends Fragment {
    private FragsData fragsData;
    private Observer<Integer> numberObserver;

    private TextView text;
    private Button button;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        text = (TextView) view.findViewById(R.id.current);
        button = (Button) view.findViewById(R.id.button_increase);

        //pobranie obiektu ViewModel
        fragsData = new ViewModelProvider(requireActivity()).get(FragsData.class);

        //stworzenie obserwatora dla Integera
        //metoda uruchamiana jest zawsze, kiedy wartosc sie zmienia
        numberObserver = new Observer<Integer>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(Integer newInteger) {
                text.setText(newInteger.toString());
            }
        };

        //podlaczenie obserwatora do obserwowanej zmiennej
        fragsData.counter.observe(getViewLifecycleOwner(), numberObserver);

        button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Integer i = fragsData.counter.getValue();
                  fragsData.counter.setValue(++i);
              }
          }
        );

        return view;
    }
}