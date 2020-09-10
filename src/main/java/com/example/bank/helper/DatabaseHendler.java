package com.example.bank.helper;

import com.example.bank.domen.Client;
import com.example.bank.domen.Credit;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHendler extends dbConfig {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectoinString = "jdbc:postgresql://" + dbHost + "/" + dbName;
        Class.forName("org.postgresql.Driver");
        dbConnection = DriverManager.getConnection(connectoinString, dbUser, dbPass);
        return dbConnection;
    }

    /*
    {"idxRef":"Test12323123",
    "idClient":20,
    "dateBirthday":"1990-04-23",
    "phone":"380991234567",
    "mail":"test@mail.com",
    "address":"Dnepr, Main Str, 19",
    "monthSalary":10000.0,
    "currSalary":"USD",
    "decision":null,
    "limitItog":null}
     */

    // Set Client data to base
    public void setClient(String idxRef, Long idClient, Date dateBirthday, String phone,
                          String mail, String address, Float monthSalary, String currSalary,
                          String decision, Float limitItog) throws SQLException, ClassNotFoundException {

        String req = "INSERT INTO Client (idx_ref, id_client, date_birthday, phone, mail, " +
                "address, month_salary, curr_salary, decision, limit_itog) VALUES (?,?,?,?,?,?,?,?,?,?)";

        // Make request string
        PreparedStatement prSt = getDbConnection().prepareStatement(req);
        prSt.setString(1, idxRef);
        prSt.setLong(2, idClient);
        prSt.setDate(3, dateBirthday);
        prSt.setString(4, phone);
        prSt.setString(5, mail);
        prSt.setString(6, address);
        prSt.setFloat(7, monthSalary);
        prSt.setString(8, currSalary);
        prSt.setString(9, decision);

        if (limitItog != null) {
            prSt.setFloat(10, limitItog);
        } else {
            prSt.setNull(10, 0);
        }
        // Make request
        prSt.executeUpdate();
    }

    // Get all clients credits
    public List <Credit> selectAllByidClient(Long IdClient) throws SQLException, ClassNotFoundException {

        List <Credit> credits_list = new ArrayList<>();

        String req = "SELECT * FROM CREDIT WHERE id_credit = ? and state_credit = 'O'";

        // make request
        PreparedStatement prSt = getDbConnection().prepareStatement(req);
        prSt.setLong(1, IdClient);
        ResultSet rs = prSt.executeQuery();

        // make credits list
        while (rs.next()) {
            Credit credit = new Credit();
            credit.setAmtCredit(rs.getFloat("amt_credit"));
            credit.setDateStart(rs.getDate("date_start"));
            credit.setIdClient(rs.getLong("id_client"));
            credit.setIdCredit(rs.getLong("id_credit"));
            credit.setStateCredit(rs.getString("state_credit"));
            credits_list.add(credit);
        }
        return credits_list;
    }

    // Get all data from the Client table
    public List<Client> selectAllClients() throws SQLException, ClassNotFoundException {
        List<Client> clients = new ArrayList<>();

        // make request string
        String req = "SELECT * FROM CLIENT";
        PreparedStatement prSt = getDbConnection().prepareStatement(req);

        // make request
        ResultSet rs = prSt.executeQuery();

        // Make client list
        while (rs.next()) {
            Client client = new Client();

            client.setIdxRef(rs.getString("idx_ref"));
            client.setIdClient(rs.getLong("id_client"));
            client.setDateBirthday(rs.getDate("date_birthday"));
            client.setPhone(rs.getString("phone"));
            client.setMail(rs.getString("mail"));
            client.setAddress(rs.getString("address"));
            client.setMonthSalary(rs.getFloat("month_salary"));
            client.setCurrSalary(rs.getString("curr_salary"));
            client.setDecision(rs.getString("decision"));
            client.setLimitItog(rs.getFloat("limit_itog"));

            clients.add(client);

        }
        return clients;
    }

    // Update decision
    public void updateDecision(String shortId, String decision) throws SQLException, ClassNotFoundException {

        // make request string
        String req = "UPDATE CLIENT set decision = ? WHERE  idx_ref = ?";
        PreparedStatement prSt = getDbConnection().prepareStatement(req);
        prSt.setString(1, decision);
        prSt.setString(2, shortId);

        // make request
        prSt.executeUpdate();
    }
}
