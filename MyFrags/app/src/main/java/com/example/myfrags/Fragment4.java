package com.example.myfrags;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Fragment4 extends Fragment {
    private FragsData fragsData;
    private Observer<Integer> numberObserver;

    private EditText edit;
    private TextWatcher textWatcher;
    private boolean turnOffWatcher;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_4, container, false);

        edit = view.findViewById(R.id.editTextNumber);

        //pobranie obiektu ViewModel
        fragsData = new ViewModelProvider(requireActivity()).get(FragsData.class);

        //stworzenie obserwatora
        numberObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer newInteger) {
                turnOffWatcher = true;
                edit.setText(newInteger.toString());
            }
        };

        //podlaczenie obserwatora do obserwowanej zmiennej
        fragsData.counter.observe(getViewLifecycleOwner(), numberObserver);

        //sworzenie obiektu textwatchera
        //w metodzie afterTextChanged odczytywany jest wpisywany tekst i podejmowana jest
        //proba konwersji tego tekstu na liczbe calkowita - jezeli konwersja jest udana
        //to jest to nowa wartosc wspolnej zmiennej counter
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!turnOffWatcher) {
                    Integer i;
                    try {
                        i = Integer.parseInt( s.toString() );
                    } catch (NumberFormatException e) {
                        i = fragsData.counter.getValue();
                    }
                    fragsData.counter.setValue(i);
                } else {
                    turnOffWatcher = !turnOffWatcher;
                }
            }
        };
        //podlaczenie TextWatchera do pola edycyjnego
        edit.addTextChangedListener(textWatcher);

        return view;
    }
}