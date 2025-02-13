package tn.esprit.test;

import tn.esprit.models.Formation;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.MyDatabase;

import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        ServiceFormation sf = new ServiceFormation();
        Date formationDate = Date.valueOf("2025-02-13");
        sf.add(new Formation("java","dev", formationDate ));
        System.out.println(sf.getAll());

    }
}