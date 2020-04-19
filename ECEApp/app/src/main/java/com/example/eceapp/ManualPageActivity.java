package com.example.eceapp;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.example.eceapp.dataLayer.ManualClass;


import java.util.ArrayList;

public class ManualPageActivity extends AppCompatActivity {

        Spinner spinner;
        PDFView pdfView;
        Button btnPrint;
        ManualClass man;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_page);

        spinner = findViewById(R.id.spinnerManual);
        pdfView = findViewById(R.id.viewPdf);
        btnPrint = findViewById(R.id.btnPrint);

        ArrayList<ManualClass> manuals = new ArrayList<>();
        ManualClass manual = new ManualClass();
        manuals = manual.GetManualList();

        //ArrayAdapter populates the spinner with manual objects
        ArrayAdapter<ManualClass> adapter = new ArrayAdapter<ManualClass>(this, android.R.layout.simple_spinner_item, manuals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Allows spinner to show the populated objects
        spinner.setAdapter(adapter);

        //Changes the PDF when new item is selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                man = (ManualClass) spinner.getSelectedItem();
                displayManualData(man, pdfView); //when item is selected this function displays the pdf

                btnPrint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        printPDF();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void printPDF() {
        PrintManager printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(ManualPageActivity.this, pdfView, man.getUrl());
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        }
        catch(Exception ex){
            Log.e("Error", ex.getMessage());
        }
    }

    private void displayManualData(ManualClass man, PDFView pdf){
        String url = man.getUrl();

        new RetrievePDFStream(pdf).execute(url);

        //takes the URL out of ManualClass Object and sends it to RetrievePDFStream to be run
    }
}
