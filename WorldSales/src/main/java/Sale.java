import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Sale {
    public final String region;
    public final String country;
    public final String itemType;
    public final String salesChannel;
    public final String orderPriority;
    public final String orderDate;
    public final Integer cUnitsSold;
    public final Double totalProfit;



    public Sale(String region, String country, String itemType, String salesChannel,
                String orderPriority, String orderDate,
                Integer cUnitsSold, Double totalProfit){
        this.region = region;
        this.country = country;
        this.itemType = itemType;
        this.salesChannel = salesChannel;
        this.orderPriority = orderPriority;
        this.orderDate = orderDate;
        this.cUnitsSold = cUnitsSold;
        this.totalProfit = totalProfit;
    }

}

