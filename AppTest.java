package CoinMarketCap.CoinMarketCap;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppTest {

@Test
public void captureIdsandConvertPrice() throws Throwable {
String mapResponse = given().when().get(
"https://pro-api.coinmarketcap.com/v1/cryptocurrency/map?CMC_PRO_API_KEY=ce2cba8f-63de-46e7-8be0-4e2efd7367b2")
.then().extract().body().asPrettyString();

JsonReader jsonReader = new JsonReader();
String dataArray = jsonReader.getJsonValue(mapResponse, "data");
JSONParser parser = new JSONParser();
JSONArray data = (JSONArray) parser.parse(dataArray);/*check with chidri*/
HashMap<String, String> countries = new HashMap<>();
for (int i = 0; i < data.size(); i++) {
String str2 = jsonReader.getJsonValue(data.get(i).toString(), "symbol");

if (str2.equals("BTC") || str2.equals("USDT") || str2.equals("ETH"))
countries.put(str2, jsonReader.getJsonValue(data.get(i).toString(), "id"));

}
for (Map.Entry<String, String> entry : countries.entrySet()) {
String conversionResponse = given().queryParam("amount", "1").queryParam("id", entry.getValue())
.queryParam("convert", "BOB").when()
.get("https://pro-api.coinmarketcap.com/v1/tools/price-conversion?CMC_PRO_API_KEY=ce2cba8f-63de-46e7-8be0-4e2efd7367b2")
.then().extract().body().asPrettyString();
String data1 = jsonReader.getJsonValue(conversionResponse, "data");
String quote = jsonReader.getJsonValue(data1, "quote");
String BOB = jsonReader.getJsonValue(quote, "BOB");
String price = jsonReader.getJsonValue(BOB, "price");
System.out.println(
"Final Price: " + price + " for the Currency: " + entry.getKey() + " and id: " + entry.getValue());

}
}

@Test
public void cryptoCurrencyInfoCall() throws Throwable {
String infoResponse = given().queryParam("id", "1027").when().get(
"https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?CMC_PRO_API_KEY=ce2cba8f-63de-46e7-8be0-4e2efd7367b2")
.then().extract().body().asPrettyString();
JsonReader jsonReader = new JsonReader();
System.out.println("infoResponse: " + infoResponse);
String data1 = jsonReader.getJsonValue(infoResponse, "data");
String value = jsonReader.getJsonValue(data1, "1027");
String logoURL = jsonReader.getJsonValue(value, "logo");
Assert.assertEquals(logoURL, "https://s2.coinmarketcap.com/static/img/coins/64x64/1027.png");

String url = jsonReader.getJsonValue(value, "urls");
String technical_doc = jsonReader.getJsonValue(url, "technical_doc");
//System.out.println("technical_doc " + technical_doc);

JSONParser parser = new JSONParser();
JSONArray technicalDocArray = (JSONArray) parser.parse(technical_doc);
for (int i = 0; i < technicalDocArray.size(); i++) {
String techDoc = technicalDocArray.get(i).toString();
System.out.println("techDoc " + techDoc);
Assert.assertEquals(techDoc, "https://github.com/ethereum/wiki/wiki/White-Paper");

}
String symbol = jsonReader.getJsonValue(value, "symbol");
System.out.println("symbol " + symbol);
Assert.assertEquals(symbol, "ETH");

String dateAdded = jsonReader.getJsonValue(value, "date_added");
System.out.println("dateAdded " + dateAdded);
Assert.assertEquals(dateAdded, "2015-08-07T00:00:00.000Z");

String platform = jsonReader.getJsonValue(value, "platform");
System.out.println("platform " + platform);
Assert.assertEquals(platform, null);

String tags = jsonReader.getJsonValue(value, "tags");
System.out.println("tags " + tags);
JSONArray tagsArray = (JSONArray) parser.parse(tags);
boolean bln=false;
for (int i = 0; i < tagsArray.size(); i++) {

String tag = tagsArray.get(i).toString();
System.out.println("tag " + tag);
if(tag.equals("mineable"))
{
System.out.println("Tag matched ");
bln=true;
break;
}

}
Assert.assertTrue(bln, "Tag is not matched :"+tagsArray);

}


@Test
public void firstTenCurrencyInfoCall() throws Throwable {
for (int i = 1; i <= 10 ; i++)
{
String currencyResponse = given().queryParam("id",Integer.toString(i)).when().get(
"https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?CMC_PRO_API_KEY=ce2cba8f-63de-46e7-8be0-4e2efd7367b2")
.then().extract().body().asPrettyString();
JsonReader jsonReader = new JsonReader();
System.out.println("infoResponse: " + currencyResponse);
String respdata = jsonReader.getJsonValue(currencyResponse, "data");
String idn = jsonReader.getJsonValue(respdata, Integer.toString(i));
String strresp = jsonReader.getJsonValue(idn, "tags");
JSONParser parser = new JSONParser();
System.out.println("Tags " + strresp);
JSONArray tagArray = (JSONArray) parser.parse(strresp);
boolean bln=false;
for (int j = 0; j < tagArray.size(); j++) {

String tagvalue = tagArray.get(j).toString();
System.out.println("Tag name is: " +tagvalue);
if(tagvalue.equals("mineable"))
{
System.out.println("Tag name exist");
String Currencyname= jsonReader.getJsonValue(idn,"name");
System.out.println("Currency name is " + Currencyname);
System.out.println();
bln=true;
break;
}
else
{
	System.out.println("Tag name is not matched");
}

}
Assert.assertTrue(bln, "Tag is not matched :"+tagArray);

}
}

}

