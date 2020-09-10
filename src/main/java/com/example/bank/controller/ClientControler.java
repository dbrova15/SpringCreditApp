package com.example.bank.controller;

import com.example.bank.domen.Client;
import com.example.bank.domen.Credit;
import com.example.bank.helper.ConverterMoney;
import com.example.bank.helper.DatabaseHendler;
import com.example.bank.paternJson.RequestJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("client")
public class ClientControler {
    Logger logger = LogManager.getLogger(ClientControler.class);

    private final DatabaseHendler db = new DatabaseHendler();
    private RequestJson RequestJson;
    public double indexPhone;
    private double sumAmtCredit; // сумма задолжености по кредитам
    private double limitItog;
    private String decision;



    @GetMapping
    public List<Client> list() {
        List<Client> clients = null;

        try{
            clients = db.selectAllClients();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return clients;
    };

    // Get coefficient by mobile phone
    public double checkPhone(String phoneNamber) {
        int lifecell1 = phoneNamber.lastIndexOf("093");
        int lifecell2 = phoneNamber.lastIndexOf("063");
        int kievstar1 = phoneNamber.lastIndexOf("097");
        int kievstar2 = phoneNamber.lastIndexOf("067");
        int kievstar3 = phoneNamber.lastIndexOf("096");
        int kievstar4 = phoneNamber.lastIndexOf("098");
        int vodafon1 = phoneNamber.lastIndexOf("066");
        int vodafon2 = phoneNamber.lastIndexOf("095");

        if (lifecell1 != -1) {
        } else if (lifecell2 != -1) {
            return 0.93;
        } else if (kievstar1 != -1) {
            return 0.93;
        } else if (kievstar2 != -1) {
            return 0.95;
        } else if (kievstar3 != -1) {
            return 0.95;
        } else if (kievstar4 != -1) {
            return 0.95;
        } else if (vodafon1 != -1) {
            return 0.94;
        } else if (vodafon2 != -1) {
            return 0.94;
        } else {
            return 0.92;
        }
        return 0.92;
    };

    public LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    @PostMapping("test")
    public Client create_test(@RequestBody Client client) throws IOException, SQLException, ClassNotFoundException {
//        UUID uuid = UUID.randomUUID();
//        String randomUUIDString = uuid.toString();

        String shortId = RandomStringUtils.random(30, "0123456789abcdef");
        client.setIdxRef(shortId);

        db.setClient(client.getIdxRef(), client.getIdClient(), client.getDateBirthday(),
                client.getPhone(), client.getMail(),
                client.getAddress(), client.getMonthSalary(), client.getCurrSalary(),
                client.getDecision(), client.getLimitItog());
        return client;
    }

    @GetMapping("test")
    public String test() {
        logger.trace("A TRACE Message");
        logger.debug("A DEBUG Message");
        logger.info("An INFO Message");
        logger.warn("A WARN Message");
        logger.error("An ERROR Message");
        return "test";
    }


    @PostMapping
    public String create(@RequestBody String jsonString) throws IOException {
        /*
        {
           "idClient":2,
           "dateBirthday":"1990-04-23",
           "phone":380991234567,
           "mail":"test@mail.com",
           "address":"Dnepr, Main Str, 19",
           "monthSalary":10000,
           "currSalary":"USD",
           "requestLimit":40000
        }
         */

        Iterable<Credit> credits = null;

        // Parse Json data
        RequestJson requestClient = new RequestJson();

        try {
            requestClient.parsJson(jsonString);
        } catch (JSONException | DateTimeParseException e) {
            e.printStackTrace();
            String textError = "ERROR: " + e.getMessage();
            logger.error(textError);
            return textError;
        }

        // Validation json data
        String resCheckData = requestClient.checkData();
        if (resCheckData != null) {
            logger.warn(resCheckData);
            return resCheckData;
        }
        // get requestLimit
        Long requestLimit = requestClient.requestLimit;

        // Set requestClient object to client object
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Client client = g.fromJson(requestClient.toString(), Client.class);

        // Validation data in Client object
        String resCheckDataClient = client.checkData();
        if (resCheckDataClient != null) {
            logger.warn(resCheckDataClient);
            return resCheckDataClient;
        }

        // Generate unique application identifier
        String shortId = RandomStringUtils.random(30, "0123456789abcdef");
        client.setIdxRef(shortId);

        // Save data to client table
        try {
            db.setClient(client.getIdxRef(), client.getIdClient(), client.getDateBirthday(),
                    client.getPhone(), client.getMail(),
                    client.getAddress(), client.getMonthSalary(), client.getCurrSalary(),
                    client.getDecision(), client.getLimitItog());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        // Convert the amount of income into national currency
        double monthSalary_UA = ConverterMoney.ConvertMoney(client.getMonthSalary(), client.getCurrSalary());

        // Get all credits client
        try {
            credits = db.selectAllByidClient(client.getIdClient());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            String textError = "ERROR: " + e.getMessage();
            logger.error(textError);
            return textError;
        }

        // sum up the amount of current debt on open loans.
        for (Credit c : credits) {
            sumAmtCredit += c.getAmtCredit();
        }

        // Get coefficient by mobile phone
        indexPhone = checkPhone(client.getPhone());
        // get limitItog
        limitItog = indexPhone * sumAmtCredit;

        // We calculate the client's age
        Date DateBirthday = client.getDateBirthday();
        LocalDate date_start = convertToLocalDateViaSqlDate(DateBirthday);
        Period period = Period.between(date_start, LocalDate.now());

        // We make a decision
        if (limitItog > requestLimit) {
            limitItog = requestLimit;
        } else if (monthSalary_UA % limitItog > 60.0) {
            limitItog = 0;
        } else if (period.getYears() > 18) {
            limitItog = 0;
        }
        if (limitItog > 0) {
            decision = "accept";
        } else {
            decision = "decline";
        }

        // Set decision to client object
        client.setDecision(decision);

        // Update decision in database
        db.updateDecision(shortId, decision);

        logger.info("End post request ");
        logger.info(client);

        return "IdxRef: " + client.getIdxRef();
    }
}
