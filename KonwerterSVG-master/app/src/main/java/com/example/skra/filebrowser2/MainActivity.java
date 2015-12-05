package com.example.skra.filebrowser2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends Activity {

    TextView currentPath;
    ListView fileList;
    Button buttonUp;
    public static final String pass = "hello";
    public static final int back = 2;
    private List<FileInfo> item = null;
    private final String root = "/";
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String path = root;

        //przywracanie ewentualnego zapisanego stanu (obórt, pauza itd)
        if (savedInstanceState != null) {

            String p = savedInstanceState.getString("current_dir");
            if (p != null) {
                path = p;
            }
        }

        setContentView(R.layout.activity_main);
        currentPath = (TextView)findViewById(R.id.textView);
        fileList = (ListView)findViewById(R.id.listView);
        buttonUp = (Button)findViewById(R.id.buttonUp);

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonUpAction(v);
            }
        });

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                File file = new File(item.get(position).getPath());
                if (file.isDirectory()) {
                    if (file.canRead()) {
                        getDir(file.getPath());
                    }
                    else {

                        Toast.makeText(getApplicationContext(), "[" + file.getName() + "] folder can't be read!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {

                    //!!!!! ścieżka do wybranego pliku - wywołania intencji podglądu
                    String filePath = file.getPath();
                    Intent i = new Intent(getApplicationContext(), ShowSVG.class);
                    i.putExtra(pass, filePath);
                    startActivityForResult(i, back);
                    //Toast.makeText(getApplicationContext(), filePath, Toast.LENGTH_SHORT).show();
                }

            }
        });
        getDir(path);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == back){
            if(resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
            if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Operation Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Zapisywanie stanu - obsługa pauz, obrotu ekranu itd.
    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);

        // Note: getValues() is a method in your ArrayAdaptor subclass
        savedState.putString("current_dir", currentPath.getText().toString());

    }


    private void buttonUpAction(View v)
    {
        //pobieramy aktualną ścieżkę z textView który nam ją wyświetla
        File f = new File(currentPath.getText().toString());

        //używamy ścieżki katalogu poziom wyżej do wywołania getDir
        String parentPath = f.getParent();
        if (parentPath!=null) getDir(parentPath);
    }

    /*
    Metoda tworząca w ListView listę plików znajdujących się w dirPath
    dirPath przy starcie programu zaczyna od katalogu roota "/"
    przy wybraniu pozycji na ListView i uzyskaniu dostępu metoda wywoływana jest dla nowej ścieżki
     */
    private void getDir(String dirPath)
    {
        //aby zwolnić pamięć zajmowaną przez informacje o wcześniej wyświetlanych plikach
        System.gc();
        currentPath.setText(dirPath);

        //zapisywanie w obiekcie file obecnej ścieżki (w tym wypadku katalogu)
        File f = new File(dirPath);
        if (f == null) {
            Toast.makeText(getApplicationContext(), "file dir path = " + dirPath + " is null", Toast.LENGTH_LONG).show();
            return;
        }

        //tworzymy tablicę obiektów File w której będizemy przechowywać wszystkie pliki znajdujące się w katalogu
        //reprezentowanym przez obiekt f
        File[] files = f.listFiles();
        if (files == null) {
            Toast.makeText(getApplicationContext(), "files[] null", Toast.LENGTH_LONG).show();
            return;
        }

        /*
        Konwersja tablicy do ArrayListy.
        Format listy jest konieczny, aby przekazać informacje do ListView adaptera
        w jaki sposób chcemy wyświetlać naszą listę plików.
        Dodatkowo przyda się do sortowania listy
         */
        item = new ArrayList<FileInfo>();
        for (int i=0; i<files.length; i++)
        {
            //Dodawanie do listy plików które są katalogami albo plikami *.svg
            if (files[i].isDirectory() || (files[i].getName().toUpperCase().endsWith(".SVG")) )
                item.add(new FileInfo(files[i].getName(), files[i].getPath(), files[i].isDirectory()));
        }

        //sortowanie alfabetyczne
        Collections.sort(item, new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo f1, FileInfo f2) {
                //Porównywanie plików po nazwach
                return f1.getName().compareTo(f2.getName());
            }
        });

        /*
        podział posortowanych alfabetycznie plików na katalogi i niekatalogi (pliki)
        Działanie: Przeglądamy listę, jeśli trafimy na plik, to zabieramy go z listy i umieszczamy na końcu
        zmienna files_count jest potrzebna do tego, aby po wyrzuceniu wszystkich plików na koniec listy
        i dojściu do końca listy nie wpaść w nieskończoną pętle (gdyż iterator nie zwiększa się
        gdy trafimy na plik)
         */
        int files_count = 0;
        for(int i=0; i<item.size()-files_count; )
        {
            FileInfo floop = item.get(i);
            if (floop.isDirectory()==false)
            {
                //zabieramy element i dodajemy go na końcu
                item.remove(i);
                item.add(floop);
                files_count++; //zliczamy plik
                continue; //rozpoczynamy pętle od nowa nie przechodząc do następnego elementu listy - następny pojawił się pod tym samym indeksem
            }
            i++;
        }

        //tak przygotowaną listę wysyłamy do adaptera
        adapter = new ListAdapter(this, item);
        fileList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
