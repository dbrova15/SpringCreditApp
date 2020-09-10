package com.example.bank.paternJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RequestJson {
//    {
//        "idClient":1,
//            "dateBirthday":"1990-04-23",
//            "phone":380991234567,
//            "mail":"test@mail.com",
//            "address":"Dnepr, Main Str, 19",
//            "monthSalary":10000,
//            "currSalary":"grn",
//            "requestLimit":40000
//    }

    public Integer idClient;
    public Date dateBirthday;
    public Long phone;
    public String mail;
    public String address;
    public Integer monthSalary;
    public String currSalary;
    public Long requestLimit;

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public Date getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(Date dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMonthSalary() {
        return monthSalary;
    }

    public void setMonthSalary(Integer monthSalary) {
        this.monthSalary = monthSalary;
    }

    public String getCurrSalary() {
        return currSalary;
    }

    public void setCurrSalary(String currSalary) {
        this.currSalary = currSalary;
    }

    public Long getRequestLimit() {
        return requestLimit;
    }

    public void setRequestLimit(Long requestLimit) {
        this.requestLimit = requestLimit;
    }

    @Override
    public String toString() {
        return "{" +
                "idClient:" + idClient +
                ", dateBirthday:'" + dateBirthday + '\'' +
                ", phone:" + phone +
                ", mail:'" + mail + '\'' +
                ", address:'" + address + '\'' +
                ", monthSalary:" + monthSalary +
                ", currSalary:'" + currSalary + '\'' +
                ", requestLimit:" + requestLimit +
                '}';
    }

    public String checkData() {
        if (idClient instanceof Integer) {} else {return "ERROR: idClient wrong value";}
        if (dateBirthday instanceof Date) {} else {return "ERROR: dateBirthday wrong value";}
        if (phone instanceof Long) {} else {return "ERROR: phone wrong value";}
        if (mail instanceof String) {} else {return "ERROR: mail wrong value";}
        if (address instanceof String) {} else {return "ERROR: address wrong value";}
        if (monthSalary instanceof Integer) {} else {return "ERROR: monthSalary wrong value";}
        if (currSalary instanceof String) {} else {return "ERROR: currSalary wrong value";}
        if (requestLimit instanceof Long) {} else {return "ERROR: requestLimit wrong value";}

        return null;
    }

    public void parsJson(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);

        dateBirthday = convertStringToDate(obj.getString("dateBirthday"));
        idClient = obj.getInt("idClient");
        phone = obj.getLong("phone");
        mail = obj.getString("mail");
        address = obj.getString("address");
        monthSalary = obj.getInt("monthSalary");
        currSalary = obj.getString("currSalary");
        requestLimit = obj.getLong("requestLimit");
    }

    private Date convertStringToDate(String dateBirthday) {

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(dateBirthday, formatter);
        Date date_birthday = Date.valueOf(dateTime);
        return date_birthday;
    }
}
