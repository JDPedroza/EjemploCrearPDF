package com.example.ejemplocrearpdf;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Documented;

public class MainActivity extends AppCompatActivity {

    //generacion de variables
    String NOMBRE_DIRECTORIO = "MisPDFs";
    String NOMBRE_DOCUMENTO = "MiPDF.pdf";

    EditText etText;
    Button btnGenerar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //buscamos lo elementos en el documento
        etText = findViewById(R.id.etText);
        btnGenerar = findViewById(R.id.btnGenerar);

        //permisos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
        }

        //generar el documento
        btnGenerar.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                crearPDF();
                //mensaje de creacion;
                Toast.makeText(MainActivity.this, "SE CREO EL PDF", Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void crearPDF(){
        Document documento = new Document();
        try {
            File file = crearFichero(NOMBRE_DOCUMENTO);
            FileOutputStream ficheroPDF = new FileOutputStream(file.getAbsoluteFile());

            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPDF);

            //modificacion del documento
            documento.open();
            documento.add(new Paragraph("TABLA\n\n"));
            documento.add(new Paragraph(etText.getText().toString()+"\n\n"));
                //generacion de tabla
            PdfPTable tabla = new PdfPTable(5);
            for(int i= 0; i<15; i++){
                tabla.addCell("CELDA "+i);
            }
            documento.add(tabla);
        }catch (DocumentException e){
        }catch (IOException e){
        }finally {
            documento.close();
        }
     }

     @RequiresApi(api = Build.VERSION_CODES.KITKAT)
     public File crearFichero(String nombreFichero){
        File ruta = getRuta();
        File fichero = null;
        if(ruta != null){
            fichero = new File(ruta, nombreFichero);
        }
        return  fichero;
     }

     @RequiresApi(api = Build.VERSION_CODES.KITKAT)
     public File getRuta(){
        File ruta = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), NOMBRE_DIRECTORIO);
            if(ruta != null){
                if(!ruta.mkdir()){
                    if (!ruta.exists()){
                        return null;
                    }
                }
            }
        }
        return ruta;
     }
}
