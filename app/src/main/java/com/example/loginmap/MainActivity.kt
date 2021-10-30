package com.example.loginmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.loginmap.databinding.ActivityMainBinding
import com.google.gson.Gson
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            //Toast.makeText(this,"hola",Toast.LENGTH_SHORT).show()
            //val intent = Intent(this,MapsActivity::class.java)
            //startActivity(intent)
            getUsers()
        }

    }

    //function for network call
    fun getUsers() {
        val url: String = "https://enomine.000webhostapp.com/api/login.php"
        //post parameters
        //form field and values
        val params = HashMap<String, String>()
        val nombre = binding.nombre.getText().toString()
        val clave = binding.clave.getText().toString()

        params["nombre"] = nombre
        params["clave"] =  clave
        val jsonObject = JSONObject(params as Map<*, *>)

        //volley post request with parameters
        val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                try {
                   Log.d("gson",response.toString())
                   // Toast.makeText(this,"hola "+response,Toast.LENGTH_SHORT).show()
                    Log.d("Respuesta",response.toString())
                    val gson = Gson()
                    val user:User = gson.fromJson(response.toString(),User::class.java)

                    Log.d("gson","hello "+user.jwt)
                    if(user.message.equals("successful login")){
                        //Toast.makeText(this,"hola ",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,MapsActivity::class.java)
                        startActivity(intent)
                    }

                } catch (e: Exception) {
                   Log.d("gson",e.message.toString())
                }
            }, Response.ErrorListener {
                Log.d("gson","Volley error ")
                Toast.makeText(this,"Incorrecto ",Toast.LENGTH_SHORT).show()
            })

        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT

        )
        VolleySingleton.getInstance(this).addToRequestQueue(request)

    }
}
