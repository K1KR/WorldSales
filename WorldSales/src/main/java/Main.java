import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        DbHandler db = DbHandler.getInstance();
        ArrayList<Sale> sales = Parser.parser();
        db.createTable();



        Map<String, Integer> map = db.getGeneralSoldItems();


        //Вывод ответов к задачам 2 и 3
        String resultCountry = db.getCountryWithHighestProfit("Europe", "Asia");
        String resultCountry2 = db.getCountryWithHighestProfitInterval(
                "Middle East", "North Africa", "Sub-Saharan Africa");
        System.out.println("Ответ на задачу 2: " + resultCountry);
        System.out.println("Ответ на задачу 3: " + resultCountry2);

        //Вывод диаграммы по убыванию

        Graph graph = new Graph(db, map);
        graph.setVisible(true);

    }

}
