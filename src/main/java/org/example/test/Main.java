package org.example.test;

import org.example.models.Service;
import org.example.models.Contrat;
import org.example.services.ServiceContrat;
import org.example.services.ServiceService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;


public class Main {
    public static void main(String[] args) {
         ServiceContrat servContrat = new ServiceContrat();
        ServiceService servService = new ServiceService();

        // Ajouter un télétravail
        //Contrat cont = new Contrat("CDI", "12/02/2025" , "12/02/2026" , "En cours" , 1700 , "kmar" , "kmarb.mbarek@gmail.com" , 1 );
        //servContrat.add(cont);
    }
}
