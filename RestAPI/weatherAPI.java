package ellyn.han.forecast;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

public class apiTest extends Thread{
    public void func() throws IOException, JSONException {
        String endPoint =  "http://apis.data.go.kr/1360000/VilageFcstInfoService/";
        String serviceKey = "ServiceKey";
        String pageNo = "1";
        String numOfRows = "10";
//        String dataType = "JSON";
        String baseDate = "20210404";
        String baseTime = "1100";
        String nx = "98";
        String ny = "77";

        String sq = endPoint+"getVilageFcst?serviceKey="+serviceKey
                +"&pageNo=" + pageNo
                +"&numOfRows=" + numOfRows
                +"+&dataType=JSON"
                + "&base_date=" + baseDate
                +"&base_time="+baseTime
                +"&nx="+nx
                +"&ny="+ny;

        URL url = new URL(sq);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader bufferedReader;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        String result= stringBuilder.toString();
//        System.out.println(result);
        conn.disconnect();

        JSONObject mainObject = new JSONObject(result);
        JSONArray itemArray = mainObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
//        JSONObject body = response.getJSONObject("body");
//        JSONObject items = body.getJSONObject("items");
//        JSONArray item = items.getJSONArray("item");
        for(int i=0; i<itemArray.length(); i++){
            JSONObject item = itemArray.getJSONObject(i);
            String category = item.getString("category");
            String value = item.getString("fcstValue");
            System.out.println(category+"  "+value);
        }


    }

}

/*
POP	강수확률	%
PTY	강수형태	코드값
R06	6시간 강수량	범주 (1 mm)
REH	습도	%
S06	6시간 신적설	범주(1 cm)
SKY	하늘상태	코드값
T3H	3시간 기온	℃
TMN	아침 최저기온	℃
TMX	낮 최고기온	℃
UUU	풍속(동서성분)	m/s
VVV	풍속(남북성분)	m/s
WAV	파고	M
VEC	풍향	deg
WSD	풍속	m/s
 */
