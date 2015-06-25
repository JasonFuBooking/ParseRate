package parser;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jason on 6/25/2015.
 */
public class RoomRateParser {
  private List<RoomRate> allRoomRates = new ArrayList<RoomRate>();

  /**
   * Constructor
   * @param data could be a URL or a html String
   * @throws ParserException
   */
  public RoomRateParser(String data) throws ParserException {
    parseRatesFrom(data);
  }

  private void parseRatesFrom(String url) throws ParserException {
    Parser parser = new Parser(url);
    List<String> dateList = getDateList(parser);
    HasAttributeFilter filter = new HasAttributeFilter("class", "RslTbl");
    NodeList nodeList = parser.parse(filter);
    if (nodeList.size() != dateList.size()) return;
    for (int i = 0; i < nodeList.size(); ++i) {
      Node node = nodeList.elementAt(i);
      if (!(node instanceof TableTag)) continue;
      TableTag table = (TableTag) node;
      for (int j = 0; j < table.getRowCount(); ++j) {
        TableRow row = table.getRow(j);
        String className = row.getAttribute("class");
        if (className != null && !className.equals("RslValDB")) continue;

        TableColumn[] columns = row.getColumns();
        if (columns.length != 12) continue;
        String[] rateValue = getRateValue(row);
        String[] roomRateMap = getRoomRateMapString(columns[7]);
        RoomRate roomRate = new RoomRate(dateList.get(i), roomRateMap[1], roomRateMap[0], rateValue[0], rateValue[1]);
        allRoomRates.add(roomRate);
      }
    }
  }

  private List<String> getDateList(Parser parser) throws ParserException {
    List<String> result = new ArrayList<String>();
    String startDate = getDate(parser, new HasAttributeFilter("name", "date1"));
    String endDate = getDate(parser, new HasAttributeFilter("name", "date2"));
    if (startDate == null || endDate == null) return result;
    Calendar start = Calendar.getInstance();
    Calendar end = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    try {
      start.setTime(sdf.parse(startDate));
      end.setTime(sdf.parse(endDate));
      while (start.before(end)) {
        result.add(sdf.format(start.getTime()));
        start.add(Calendar.DATE, 1);
      }
      result.add(sdf.format(start.getTime()));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return result;
  }

  private String[] getRateValue(TableRow row) {
    String[] result = {"", ""};
    TableColumn ratesColumn = searchColumnByClassName(row, "RslValCP");
    if (ratesColumn == null) return result;
    TableColumn costcolumn = searchColumnByClassName(ratesColumn, "RPCPCost");
    if (costcolumn == null || costcolumn.getChildCount() != 1) return result;
    result[0] = costcolumn.getChild(0).getText().trim();
    TableColumn larColumn = searchColumnByClassName(ratesColumn, "RPCPLar");
    for (int i = 0; i < larColumn.getChildCount(); ++i) {
      Node child = larColumn.getChild(i);
      if (!(child instanceof InputTag)) continue;
      InputTag input = (InputTag) child;
      String id = input.getAttribute("id");
      if (id.startsWith("larAmountDB")) {
        result[1] = input.getAttribute("value");
        break;
      }
    }
    return result;
  }

  private TableColumn searchColumnByClassName(CompositeTag parent, String className) {
    NodeList columns = parent.searchFor(TableColumn.class, true);
    for (int k = 0; k < columns.size(); ++k) {
      TableColumn column = (TableColumn) columns.elementAt(k);
      String name = column.getAttribute("class");
      if (name != null && name.equals(className)) {
        return column;
      }
    }
      return null;
  }

  private String[] getRoomRateMapString(TableColumn column) {
    TableColumn roomRate = column;
    if (roomRate.getChildCount() == 3) {
      String data = roomRate.getChild(1).getText().trim();
      String[] result = data.split("-");
      result[0].trim();
      result[1].trim();
      return result;
    }
    return null;
  }

  private String getDate(Parser parser, HasAttributeFilter dateFilter) throws ParserException {
    Node node = getFirstElement(parser, dateFilter);
    if (node == null) return "";
    String[] data = node.getText().split(" ");
    String resultDate = "";
    for (String d : data) {
      String[] pair = d.split("=");
      if (pair[0].equals("value")) {
        resultDate =  pair[1].substring(1, pair[1].length());
        break;
      }
    }
    parser.reset();
    return resultDate;
  }
  private Node getFirstElement(Parser parser, HasAttributeFilter filter) throws ParserException {
    NodeList nodeList = parser.parse(filter);
    if (nodeList.size() == 1) {
      return nodeList.elementAt(0);
    }
    return null;
  }

  public List<RoomRate> getAllRoomRates() {
    return allRoomRates;
  }
}
