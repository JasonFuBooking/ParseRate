import junit.framework.Assert;
import org.htmlparser.util.ParserException;
import org.junit.Test;
import parser.RoomRate;
import parser.RoomRateParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Jason on 6/25/2015.
 */
public class TestRateParser {
  @Test
  public void testSingleDayRate() throws ParserException {
    List<RoomRate> roomRates = testFilePath("ratePages/singleDay/1.html");
    Assert.assertEquals(roomRates.size(), 6);
    print(roomRates);
  }

  @Test
  public void test6DayRates() throws ParserException {
    List<RoomRate> roomRates = testFilePath("ratePages/sixDay/6.html");
    Assert.assertEquals(roomRates.size(), 36);
    print(roomRates);
  }

  @Test
  public void test2DayRates() throws ParserException {
    List<RoomRate> roomRates = testFilePath("ratePages/twoDay/2.html");
    Assert.assertEquals(roomRates.size(), 12);
    print(roomRates);
  }

  @Test
  public void testSingleDayRateFromString() throws ParserException, IOException {
    List<RoomRate> roomRates = testHtmlString("ratePages/singleDay/1.html");
    Assert.assertEquals(roomRates.size(), 6);
    print(roomRates);
  }

  @Test
  public void test6DayRatesFromString() throws ParserException, IOException {
    List<RoomRate> roomRates = testHtmlString("ratePages/sixDay/6.html");
    Assert.assertEquals(roomRates.size(), 36);
    print(roomRates);
  }

  @Test
  public void test2DayRatesFromString() throws ParserException, IOException {
    List<RoomRate> roomRates = testHtmlString("ratePages/twoDay/2.html");
    Assert.assertEquals(roomRates.size(), 12);
    print(roomRates);
  }

  private void print(List<RoomRate> roomRates) {
    System.out.println("size :" + roomRates.size());
    for (RoomRate r : roomRates) {
      System.out.println(r);
    }
  }

  private List<RoomRate> testFilePath(String fileName) throws ParserException {
    File file = getFile(fileName);
    RoomRateParser parser = new RoomRateParser(file.getAbsolutePath());
    return parser.getAllRoomRates();
  }

  private List<RoomRate> testHtmlString(String fileName) throws ParserException, IOException {
    String file = readFile(getFile(fileName).getPath());
    RoomRateParser parser = new RoomRateParser(file);
    return parser.getAllRoomRates();
  }

  private File getFile(String fileName) {
    ClassLoader classLoader = getClass().getClassLoader();
    return new File(classLoader.getResource(fileName).getFile());
  }

  private String readFile(String path) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, StandardCharsets.UTF_8);
  }
}
