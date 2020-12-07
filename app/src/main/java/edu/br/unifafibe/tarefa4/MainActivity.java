package edu.br.unifafibe.tarefa4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView notesListView;

    ArrayList<String> listaPaises = new ArrayList<>();
    ArrayList<String> listaLatitudes = new ArrayList<>();
    ArrayList<String> listaLongitudes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //******* DEFINE A LISTA
        notesListView = (ListView) findViewById(R.id.lvLista);

        //****** CRIA A REQUISIÇÃO HTTP QUE RETONA UM JSONARRAY
        String url = "https://restcountries.eu/rest/v2/lang/pt";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++){
                                listaPaises.add(response.getJSONObject(i).getString("name").toString());
                                System.out.println(response.getJSONObject(i).getString("name").toString());
                                listaLatitudes.add(response.getJSONObject(i).getJSONArray("latlng").getString(0).toString());
                                listaLongitudes.add(response.getJSONObject(i).getJSONArray("latlng").getString(1).toString());
                            }

                            //****** MOSTRA A LISTA NA TELA
                            notesListView.setAdapter(new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    android.R.layout.simple_list_item_activated_1,
                                    android.R.id.text1,
                                    listaPaises
                            ));

                        }
                        catch (Exception ex){
                            System.out.println("Não foi possível retornar os países");
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                System.err.println(error);
            }
        });

        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);

        //******** CHAMA A TELA DE MAPA QUANDO O USUÁRIO CLICA
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent it = new Intent(MainActivity.this, MapsActivity.class);
                it.putExtra("Nome", listaPaises.get(position));
                it.putExtra("Lat", Double.parseDouble(listaLatitudes.get(position)));
                it.putExtra("Long", Double.parseDouble(listaLongitudes.get(position)));
                startActivity(it);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
