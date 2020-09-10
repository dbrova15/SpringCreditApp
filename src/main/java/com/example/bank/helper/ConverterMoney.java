package com.example.bank.helper;

import com.example.bank.paternJson.PrivatbankJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

// Convert the amount of income into national currency
public class ConverterMoney {
    // Make request to api
    public static String requestToApi(String base_ccy) throws IOException {

        OkHttpClient client = new OkHttpClient();

        // Make request
        Request request = new Request.Builder()
                .url("https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5")
                .build(); // defaults to GET

        // Get response
        Response response = client.newCall(request).execute();

        // Parse json
        Gson gson = new Gson();
        String jsonData = response.body().string();
        PrivatbankJson[] jsonArray = gson.fromJson(jsonData, PrivatbankJson[].class);

        // Get the client's currency rate
        String costMoney = null;
        for(PrivatbankJson jsonObj : jsonArray) {

            if (jsonObj.base_ccy.equals(base_ccy)){
                costMoney = jsonObj.sale;
            }
        }

        return costMoney;
    }

    // Convert Money
    public static double ConvertMoney(Float money, String base_ccy) throws IOException {
        double clients_money;
        // Make request to api
        String str_money = requestToApi(base_ccy);
        Float cost_money = Float.parseFloat(str_money);

        // conversion
        clients_money = money * cost_money;
        return clients_money;
    }
}
