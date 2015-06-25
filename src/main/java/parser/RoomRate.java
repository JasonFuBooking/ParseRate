package parser;

/**
 * Created by Jason on 6/25/2015.
 */
public class RoomRate {
  private String date;
  private String ratePlanName;
  private String roomTypeName;
  private String rateCost;
  private String rateLar;

  public RoomRate(String date, String ratePlanName, String roomTypeName, String cost, String lar) {
    this.date = date;
    this.ratePlanName = ratePlanName;
    this.roomTypeName = roomTypeName;
    this.rateCost = cost;
    this.rateLar = lar;
  }

  public String getDate() {
    return date;
  }

  public String getRatePlanName() {
    return ratePlanName;
  }

  public String getRoomTypeName() {
    return roomTypeName;
  }

  public String getRateCost() {
    return rateCost;
  }

  public String getRateLar() {
    return rateLar;
  }

  @Override
  public String toString() {
    return "[date: " + date +
        ", ratePlan: " + ratePlanName +
        ", roomType: " + roomTypeName +
        ", rate LAR: " + rateLar +
        ", rate cost: " + rateCost + "]";
  }
}
