package com.example.angelia.term4androidappproject.Utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by arroyo on 28/11/17.
 */

public class JsonProcessing {

    public static HashMap<String, LinkedTreeMap> hashMapify(int resource, Context context){
        String line;
        String output="";

        InputStream inputStream = context.getResources().openRawResource(resource);
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((line=reader.readLine())!=null){
                output = output+line;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        Gson gson = new Gson();
        HashMap<String, LinkedTreeMap> map = gson.fromJson(output, new TypeToken<HashMap<String, Object>>(){}.getType());

        return map;
    }
}
