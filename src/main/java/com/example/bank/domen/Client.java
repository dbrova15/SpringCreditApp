package com.example.bank.domen;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;


// CREATE TABLE client (
//         idxRef varchar(30) NOT NULL, - уникальный идентификатор заявки
//         idClient int NOT NULL, -- идентификатор клиента
//         dateCurr timestamp NOT NULL,  -- текущая дата на момент записи  данных по заявке в базу
//         phone varchar(45) DEFAULT NULL, -- номер телефона
//         mail varchar(45) DEFAULT NULL, -- email
//         address varchar(45) DEFAULT NULL, --адресс клиента
//         monthSalary decimal (15,2) DEFAULT NULL, -- сумма дохода клиента
//         currSalary char(3)-- валюта дохода клиента
//         decision varchar(45) DEFAULT NULL, --решение по кредиту
//         limitItog  decimal (15,2) DEFAULT NULL, -- сумма кредита
//         primary key (idxRef  )
//         )

/**
 * Обьект таблицы Client
 */
@Entity
public class Client {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
//    @Column(length=30)
    @Column(columnDefinition = "varchar(30) NOT NULL")
    private String idxRef;
    @Column(columnDefinition = "int NOT NULL")
    private Long idClient;
    @Basic
//    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(columnDefinition = "timestamp NOT NULL")
    private java.sql.Date dateBirthday;
    //    @Column(length=45)
    @Column(columnDefinition = "varchar(45) DEFAULT NULL")
    private String phone;
    //    @Column(length=45)
    @Column(columnDefinition = "varchar(45) DEFAULT NULL")
    private String mail;
    //    @Column(length=45)
    @Column(columnDefinition = "varchar(45) DEFAULT NULL")
    private String address;
    //    @Column(precision = 15, scale = 2)
    @Column(columnDefinition = "decimal (15,2) DEFAULT NULL")
    private Float monthSalary;
    //    @Column(length=3)
    @Column(columnDefinition = "char(3)")
    private String currSalary;
    //    @Column(length=45)
    @Column(columnDefinition = "varchar(45) DEFAULT NULL")
    private String decision;

    @Column(precision = 15, scale = 2)
    private Float limitItog;

    /**
     * Получение idxRef
     *
     * @return idxRef
     */
    public String getIdxRef() {
        return idxRef;
    }

    /**
     * Присвоение значения idxRef
     */
    public void setIdxRef(String idxRef) {
        this.idxRef = idxRef;
    }

    /**
     * Получение idClient
     *
     * @return idClient
     */
    public Long getIdClient() {
        return idClient;
    }

    /**
     * Присвоение значения idClient
     */
    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    /**
     * Получение dateBirthday
     *
     * @return dateBirthday
     */
    public Date getDateBirthday() {
        return dateBirthday;
    }

    /**
     * Присвоение значения dateBirthday
     */
    public void setDateBirthday(Date dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    /**
     * Получение phone
     *
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Присвоение значения phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Получение mail
     *
     * @return mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * Присвоение значения mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Получение address
     *
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Присвоение значения address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Получение monthSalary
     *
     * @return monthSalary
     */
    public Float getMonthSalary() {
        return monthSalary;
    }

    /**
     * Присвоение значения monthSalary
     */
    public void setMonthSalary(Float monthSalary) {
        this.monthSalary = monthSalary;
    }

    /**
     * Получение currSalary
     *
     * @return currSalary
     */
    public String getCurrSalary() {
        return currSalary;
    }

    /**
     * Присвоение значения currSalary
     */
    public void setCurrSalary(String currSalary) {
        this.currSalary = currSalary;
    }

    /**
     * Получение decision
     *
     * @return decision
     */
    public String getDecision() {
        return decision;
    }

    /**
     * Присвоение значения decision
     */
    public void setDecision(String decision) {
        this.decision = decision;
    }

    /**
     * Получение limitItog
     *
     * @return limitItog
     */
    public Float getLimitItog() {
        return limitItog;
    }

    /**
     * Присвоение значения limitItog
     */
    public void setLimitItog(Float limitItog) {
        this.limitItog = limitItog;
    }

    /**
     * Получить строку с данными обьекта
     *
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
                "idxRef:'" + idxRef + '\'' +
                ", idClient:" + idClient +
                ", dateBirthday:" + dateBirthday +
                ", phone:'" + phone + '\'' +
                ", mail:'" + mail + '\'' +
                ", address:'" + address + '\'' +
                ", monthSalary:" + monthSalary +
                ", currSalary:'" + currSalary + '\'' +
                ", decision:'" + decision + '\'' +
                ", limitItog:" + limitItog +
                '}';
    }

    public String checkData() {
        if (idClient instanceof Long) {
        } else {
            return "ERROR: idClient wrong value";
        }
        if (dateBirthday instanceof Date) {
        } else {
            return "ERROR: dateBirthday wrong value";
        }
        if (phone instanceof String) {
        } else {
            return "ERROR: phone wrong value";
        }
        if (mail instanceof String) {
        } else {
            return "ERROR: mail wrong value";
        }
        if (address instanceof String) {
        } else {
            return "ERROR: address wrong value";
        }
        if (monthSalary instanceof Float) {
        } else {
            return "ERROR: monthSalary wrong value";
        }
        if (currSalary instanceof String) {
        } else {
            return "ERROR: currSalary wrong value";
        }

        return null;
    }
}

