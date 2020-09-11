package com.example.bank.domen;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;


//CREATE TABLE     credit   (
//        idClient int NOT NULL,   -- идентификатор клиента
//        idCredit int  NOT NULL,   --идентификатор кредита
//        amtCredit   decimal (15,2) NOT NULL, --сумма задолженности
//        dateStart date  NOT NULL, -- дата старта
//        stateCredit char(1), -- состояние кредита (O - открыт, C - закрыт)
//        primary key ( idClient , idCredit  )
//        )

/**
 * Обьект таблицы Credit
 */
@Entity
public class Credit {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCredit;
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idClient;
    @Column(precision = 15, scale = 2)
    private Float amtCredit;

    @Basic
    @NotNull
    private java.sql.Date dateStart;
    @Column(length = 1)
    private String stateCredit;

    /**
     * Получение idCredit
     *
     * @return idCredit
     */
    public Long getIdCredit() {
        return idCredit;
    }

    /**
     * Присвоение значения idCredit
     */
    public void setIdCredit(Long idCredit) {
        this.idCredit = idCredit;
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
     * Получение amtCredit
     *
     * @return amtCredit
     */
    public Float getAmtCredit() {
        return amtCredit;
    }

    /**
     * Присвоение значения amtCredit
     */
    public void setAmtCredit(Float amtCredit) {
        this.amtCredit = amtCredit;
    }

    /**
     * Получение dateStart
     *
     * @return dateStart
     */
    public Date getDateStart() {
        return dateStart;
    }

    /**
     * Присвоение значения dateStart
     */
    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    /**
     * Получение stateCredit
     *
     * @return stateCredit
     */
    public String getStateCredit() {
        return stateCredit;
    }

    /**
     * Присвоение значения stateCredit
     */
    public void setStateCredit(String stateCredit) {
        this.stateCredit = stateCredit;
    }

    /**
     * Получить строку с данными обьекта
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Credit{" +
                "idCredit=" + idCredit +
                ", idClient=" + idClient +
                ", amtCredit=" + amtCredit +
                ", dateStart=" + dateStart +
                ", stateCredit='" + stateCredit + '\'' +
                '}';
    }
}
