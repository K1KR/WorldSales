import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

//Диаграмма
public class Graph extends JFrame {

    private final DbHandler dbHandler;
    private static final int DEFAULT_PADDING = 15;

    public Graph(DbHandler dbHandler, Map<String, Integer> map) throws SQLException {
        this.dbHandler = dbHandler;
        init(sortMapByValue(map));
    }

    private void init(Map<String, Integer> map) {
        JFreeChart chart = createChart(createDataset(map));
        createChartPanel(chart);
        setupFrame();
    }

    private void createChartPanel(JFreeChart chart) {
        JPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(DEFAULT_PADDING, DEFAULT_PADDING,
                DEFAULT_PADDING, DEFAULT_PADDING));
        chartPanel.setBackground(Color.WHITE);
        add(chartPanel, BorderLayout.CENTER);
    }

    private void setupFrame() {
        setTitle("Общее кол-во проданных товаров по регионам");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
    }

    private Map<String, Integer> sortMapByValue(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private CategoryDataset createDataset(Map<String, Integer> map) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        map.forEach((key, value) -> dataset.addValue(value, "region", key));
        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "График о проданных товарах по регионам",
                "Регион",
                "Общее кол-во проданных товаров",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        return chart;
    }
}

