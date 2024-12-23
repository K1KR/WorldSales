import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class DbHandler {

    private static final String url = "jdbc:sqlite:src/main/resources/database.db" ;
    private  static DbHandler instance = null;
    private final Connection connection;


    public DbHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(url);

    }


    public static DbHandler getInstance() throws SQLException{
        if ( instance == null){
            instance = new DbHandler();
        }
        return instance;
    }


    public void createTable(){
        String createSales = """
                CREATE TABLE  IF NOT EXISTS World_Sales(
                region TEXT,
                country TEXT,
                itemType TEXT,
                salesChannel TEXT,
                orderPriority TEXT,
                orderDate TEXT,
                cUnitsSold INT,
                totalProfit REAL
                )
                """;
        try (Statement statement = connection.createStatement()){

            statement.execute(createSales);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void fillTable(Sale sale){


        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO World_Sales(region, country, itemType," +
                " salesChannel, orderPriority, orderDate, cUnitsSold, totalProfit) " +
                "VALUES(?,?,?,?,?,?,?,?)")){

            statement.setObject(1, sale.region);
            statement.setObject(2, sale.country);
            statement.setObject(3, sale.itemType);
            statement.setObject(4, sale.salesChannel);
            statement.setObject(5, sale.orderPriority);
            statement.setObject(6, sale.orderDate);
            statement.setObject(7, sale.cUnitsSold);
            statement.setObject(8, sale.totalProfit);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void inputData(ArrayList<Sale> sales){
        try{
            DbHandler database = DbHandler.getInstance();

            for(Sale s : sales){
                database.fillTable(s);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public  ArrayList<Sale> getAllSales() throws SQLException {

        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM World_Sales")){
            return getDataFromResultSet(resultSet);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static ArrayList<Sale> getDataFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Sale> salesLst = new ArrayList<>();
        while(resultSet.next()){
            String region = resultSet.getString("region");
            String country = resultSet.getString("country");
            String itemType = resultSet.getString("itemType");
            String salesChannel = resultSet.getString("salesChannel");
            String orderPriority = resultSet.getString("orderPriority");
            String orderDate = resultSet.getString("orderDate");
            Integer cUnitsSold = resultSet.getInt("cUnitsSold");
            Double totalProfit = resultSet.getDouble("totalProfit");


            Sale sale = new Sale(region, country, itemType, salesChannel,orderPriority,
                    orderDate, cUnitsSold, totalProfit);
            salesLst.add(sale);
        }

        return salesLst;
    }

    // 2 задание
    public String getCountryWithHighestProfit(String region1, String region2){



        try(PreparedStatement statement = connection.prepareStatement( """           
                   Select World_Sales.country, World_Sales.totalProfit
                   from World_Sales
                   Where World_Sales.region = ? OR World_Sales.region = ?
                   Order by World_Sales.totalProfit desc
                   LIMIT 1
                   """);){

            statement.setString(1, region1);
            statement.setString(2, region2);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("country");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
         return null;
    }

    // 3 задание
    public String getCountryWithHighestProfitInterval(String region1, String region2, String region3){


        try ( PreparedStatement statement = connection.prepareStatement( """                   
                Select World_Sales.country, World_Sales.totalProfit
                from World_Sales
                Where World_Sales.region = ? OR World_Sales.region = ?  OR World_Sales.region = ?
                AND World_Sales.totalProfit BETWEEN 420000.0 AND 440000.0
                Order by World_Sales.totalProfit desc
                Limit 1
                """);){

            statement.setString(1, region1);
            statement.setString(2, region2);
            statement.setString(3, region3);

            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    return resultSet.getString("country");
                }
                else {
                    return null ;

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Integer> getGeneralSoldItems() {

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("""
                SELECT World_Sales.region, SUM(World_Sales.cUnitsSold) AS total
                FROM World_Sales
                GROUP BY World_Sales.region
                ORDER BY total DESC
                """)) {

            Map<String, Integer> resultMap = new HashMap<>();

            while (rs.next()) {
                String regionName = rs.getString("region");
                Integer totalUnitsSold = rs.getInt("total");

                resultMap.merge(regionName, totalUnitsSold, Integer::sum);
            }
            return resultMap;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}