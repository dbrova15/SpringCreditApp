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


/**
 * Класс контроллера API со свойствами <b>db</b>, * <b>indexPhone</b>,
 * <b>sumAmtCredit</b>, * <b>limitItog</b>, * <b>decision</b>.
 */
@RestController
@RequestMapping("client")
public class ClientControler {
    Logger logger = LogManager.getLogger(ClientControler.class);

    /**
     * Объект базы данных
     */
    private final DatabaseHendler db = new DatabaseHendler();
    /**
     * Коэффициент относительно кода номера телефона
     */
    public double indexPhone;
    /**
     * сумма задолжености по кредитам
     */
    private double sumAmtCredit;
    /**
     * сумма кредита
     */
    private double limitItog;
    /**
     * решение по заявке
     */
    private String decision;

    /**
     * Функция получения данных обо всех клиентах
     *
     * @return возвращает список клиентов
     */
    @GetMapping
    public List<Client> list() {
        List<Client> clients = null;

        try {
            clients = db.selectAllClients();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return clients;
    }

    /**
     * Расчет коэффициента относительно кода номера телефона
     *
     * @return Коэффициент относительно кода номера телефона
     */
    public double checkPhone(String phoneNamber) {
        int start = 2;
        int end = 5;
        char[] dst=new char[end - start];
        phoneNamber.getChars(start, end, dst, 0);

        String s = String.valueOf(dst);

        if (s.equals("093") || s.equals("063")) {
            return 0.93;
        } else if (s.equals("097") || s.equals("067") || s.equals("096") || s.equals("098")) {
            return 0.95;
        } else if (s.equals("066") || s.equals("095")) {
            return  0.94;
        } else {
            return 0.92;
        }
    }

    /**
     * КОнвертация LocalDate в java.sql.Date
     *
     * @return объект java.sql.Date
     */
    public LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    /**
     * Post запрос на создание заявки
     *
     * @param jsonString Example:
     *                   {
     *                   "idClient":2,
     *                   "dateBirthday":"1990-04-23",
     *                   "phone":380991234567,
     *                   "mail":"test@mail.com",
     *                   "address":"Dnepr, Main Str, 19",
     *                   "monthSalary":10000,
     *                   "currSalary":"USD",
     *                   "requestLimit":40000
     *                   }
     * @return String IdxRef
     */

    @PostMapping
    public String create(@RequestBody String jsonString) throws IOException {


        // список кредитов
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
        try {
            double monthSalary_UA = ConverterMoney.ConvertMoney(client.getMonthSalary(), client.getCurrSalary());
        } catch (Exception e) {
            e.printStackTrace();
            String textError = "ERROR: " + e.getMessage();
            logger.error(textError);
            return textError;
        }
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
        } if (monthSalary_UA % limitItog > 60.0) {
            limitItog = 0;
        } if (period.getYears() > 18) {
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
        try {
            db.updateDecision(shortId, decision);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            String textError = "ERROR: " + throwables.getMessage();
            logger.error(textError);
        }

        logger.info("End post request ");
        logger.info(client);

        return "IdxRef: " + client.getIdxRef();
    }
}
