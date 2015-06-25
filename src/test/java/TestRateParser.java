import junit.framework.Assert;
import org.htmlparser.util.ParserException;
import org.junit.Test;
import parser.Rate;
import parser.RateParser;

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
    List<Rate> rates = testFilePath("ratePages/singleDay/1.html");
    Assert.assertEquals(rates.size(), 6);
    print(rates);
  }

  @Test
  public void test6DayRates() throws ParserException {
    List<Rate> rates = testFilePath("ratePages/sixDay/6.html");
    Assert.assertEquals(rates.size(), 36);
    print(rates);
  }

  @Test
  public void test2DayRates() throws ParserException {
    List<Rate> rates = testFilePath("ratePages/twoDay/2.html");
    Assert.assertEquals(rates.size(), 12);
    print(rates);
  }

  @Test
  public void testSingleDayRateFromString() throws ParserException, IOException {
    List<Rate> rates = testHtmlString("ratePages/singleDay/1.html");
    Assert.assertEquals(rates.size(), 6);
    print(rates);
  }

  @Test
  public void test6DayRatesFromString() throws ParserException, IOException {
    List<Rate> rates = testHtmlString("ratePages/sixDay/6.html");
    Assert.assertEquals(rates.size(), 36);
    print(rates);
  }

  @Test
  public void test2DayRatesFromString() throws ParserException, IOException {
    List<Rate> rates = testHtmlString("ratePages/twoDay/2.html");
    Assert.assertEquals(rates.size(), 12);
    print(rates);
  }

  private void print(List<Rate> rates) {
    System.out.println("size :" + rates.size());
    for (Rate r : rates) {
      System.out.println(r);
    }
  }

  private List<Rate> testFilePath(String fileName) throws ParserException {
    File file = getFile(fileName);
    RateParser parser = new RateParser(file.getAbsolutePath());
    return parser.getAllRates();
  }

  private List<Rate> testHtmlString(String fileName) throws ParserException, IOException {
    String file = readFile(getFile(fileName).getPath());
    RateParser parser = new RateParser(file);
    return parser.getAllRates();
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
