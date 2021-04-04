package ellyn.han.forecast;
import android.util.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

public class apiTest extends Thread{
    public void func() throws IOException{
        String endPoint =  "http://apis.data.go.kr/1360000/VilageFcstInfoService/";
        String serviceKey = "servicekey";
//         String pageNo = "1";
//         String numOfRows = "10";
//         String dataType = "JSON";
//         String ftype = "ODAM";
//         String basedatetime = "202104041100";

        String sq = endPoint+"getFcstVersion?serviceKey="+serviceKey+"&pageNo=1&numOfRows=10&dataType=JSON&ftype=ODAM&basedatetime=202104041100";
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
        System.out.println(result);
        conn.disconnect();
    }
}
