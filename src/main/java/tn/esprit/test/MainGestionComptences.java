package tn.esprit.test;

import tn.esprit.services.ServiceFormation;
import tn.esprit.services.ServiceTest;

public class MainGestionComptences {
    public static void main(String[] args) {

        // ************ Formation
        ServiceFormation sf = new ServiceFormation();
        //  ADD
//        Date formationDate = Date.valueOf("2025-02-13");
//       sf.add(new Formation("java","dev", formationDate ));
//        System.out.println(sf.getAll());

        //  UPDATE
//        formation = new Formation(1, "Java Basics", "Programming", Date.valueOf("2025-03-01"));
//          sf.update(formation);
//        System.out.println(sf.getAll());
//
        //DELETE
//        sf.delete(formation);
//        System.out.println(sf.getAll());
      //  System.out.println(sf.getById(5));

        /****************************************/
        // Test
            //ADD

        ServiceTest st = new ServiceTest();
//     Test t1= new Test(10, 2,Date.valueOf("2025-03-01"),"60min", Test.TypeTest.TECHNIQUE);
//     st.add(t1);
//        System.out.println(st.getAll());

            //UPDATE
//        Test t2 = new Test(5, 9, 2, Date.valueOf("2025-03-01"), "60min", Test.TypeTest.TECHNIQUE);
//        st.update(t2);
//        System.out.println(st.getAll());

             //DELETE

//        st.delete(t2);
//        System.out.println(st.getAll());
        System.out.println(st.getById(7));

    }
}