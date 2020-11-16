package konovalov.ebayscraper.core;

import android.util.Log;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

public class TerapeakAuthenticator {

    private final OkHttpClient client = HttpClient.getInstance();
    private final LoginStatusListener loginStatusListener;

    private final HttpUrl mainPageUrl = HttpUrl.parse("https://www.ebay.com/");
    private final HttpUrl loginPageUrl = HttpUrl.parse("https://signin.ebay.com/ws/eBayISAPI.dll?SignIn");
//    private final HttpUrl loginPageUrl = HttpUrl.parse("https://signin.ebay.com/ws/eBayISAPI.dll?SignIn&ru=");

    public TerapeakAuthenticator(LoginStatusListener loginStatusListener) {
        this.loginStatusListener = loginStatusListener;
    }

    public void checkIfLoggedIn() {
        Request request = new Request.Builder()
//                .url("https://www.ebay.com/sh/research/")
//                .url("https://www.ebay.com/myb/Summary")
                .url("https://www.ebay.com/sh/research/api/search?keywords=metallica")
                .headers(basicHeaders)
//                .header("Cookie", "npii=btguid/a734e2fa1750a7647ce782f2ffe188e363751eb9^cguid/a734e2e31750a1b7a212bff6ff98a45063751eb9^; " +
//                        "dp1=bu1p/c29ybnk4Ng**63751eb9^kms/in63751eb9^pbf/%232000000e400e000000180020000046193eb39^u1f/Rick63751eb9^tzo/1a45fb2c5c5^expt/" +
//                        "000160483027487360985e03^bl/US63751eb9^; nonsession=BAQAAAXQoEGjGAAaAAAQAB2GT6zBzb3JueTg2ABAAB2GT6zlzb3JueTg2ADMADmGT6zk1NTMwOS0" +
//                        "4MjA1LFVTQQBAAAdhk+s5c29ybnk4NgCaAAhftVqwc29ybnk4NmcAnAA4YZPrOW5ZK3NIWjJQckJtZGo2d1ZuWStzRVoyUHJBMmRqNndKblllbERwZUhwUWlkaj" +
//                        "Z4OW5ZK3NlUT09AJ0ACGGT6zkwMDAwMDAwMADKACBjdR65YTczNGUyZmExNzUwYTc2NDdjZTc4MmYyZmZlMTg4ZTMAywACX7K+wTMwAWQAB2N1HrkjMDAwMDBho" +
//                        "9rRjVPU1YqD70kmEJ/5ayXJVC0*; cid=tEpGlU8Bejllgme4%23178500405; DG_IID=6D474828-0CF9-3946-994F-093096DC1759; " +
//                        "DG_UID=40E42B7E-9DEF-3CC8-B29C-EA929C70C9A7; DG_ZID=BE9E70A4-FC51-32F3-83AC-75C84D635D96; DG_ZUID=98C0DA47-5853-3199-A942-" +
//                        "F6108822711A; DG_HID=153CC203-7FEC-327B-8F04-52CD63BAB200; DG_SID=93.157.175.39:YAt4dxjKDvEIlIjkBLMiy9QtA6rn83rt6XM1pSpnIB4; " +
//                        "ns1=BAQAAAXQoEGjGAAaAAKUAF2GT6zkxMTg3Njk3NDc1LzA7NTQ2NzU0NC8wO2wt/1HvJsDZIZkUBDMw3xD9eCeS; s=BAQAAAXQoEGjGAAWAAAEAB1+0CTBzb3J" +
//                        "ueTg2AAMAAV+0CTU3AAwAB1+0CTU1NDY3NTQ0ABEADF+yvGAwMDAwMHNvcm55ODYAPQAHX7QJNXNvcm55ODYAqAABX7QJMDEA+AAgX7QJNWE3MzRlMmZhMTc1MGE" +
//                        "3NjQ3Y2U3ODJmMmZmZTE4OGUzAUUACGGT6zk1ZTdjMTg4OQFlAANftAk1IzAyIGMYAGuNUSGm2lueysGk4YnkHuU*; ebay=%5Ejs%3D1%5EsfLMD%3D15252898" +
//                        "90%5Esin%3Din%5Esbf%3D%2340000004%5E; ds1=ats/1605547952900; JSESSIONID=4EA1416EAF72DABCB33F1DC6E31DF62F; sru=X; ak_bmsc=E3E" +
//                        "29F8F3B8BF55D3AB77099FE789EF958DDD86C455A000039AEB25F8189F642~plgpKgW135RszqjJFOUih4nkATLgb6e0mgjBLABLZjrGZ1+W0HeZtXptPDXPke" +
//                        "N9quGIfAtcwM5/wg9HRI1FIs+FzW/ubAuQ+q5y81RjnjtBGw4a/6OLZDg9G/W1eTi8wX+WSVS3dI7Yfa2Y9H5cLSyJEB/k+HAVfXSkv/ygo00S28gFhgq4ZGJ8D5" +
//                        "hbqp2d2tkWtIZGHJ4Z7VwQFxrt+x+k4ZZHg0fxL6qZSCnFyAKTM=; bm_sv=00584E0BF251288BA72BDE6ACD8FF0AD~Yrza41rUt614VLa7jt1+RsDVuUwS38" +
//                        "JbUw9x/pn5sJdcobgZNHSHnWB9MJH4lRZrU0kVrn501hiuoAtpCShn4NjEyct4KcVowTd4+iG56X2ev7DLKWGWAA+Cwg/wlyHv+KoFeuiPsBM7nmguUl1nn23g" +
//                        "4pc4WSkJqQL2O+1IJY8=; shs=BAQAAAXXEDSi+AAaAAVUAD2GT6zAyMDI4MDM3MzczMDAwLDK+cl/VnOwkeUobyNBA3SyftAErJQ**; AMCV_A71B5B5B54F607" +
//                        "AB0A4C98A2%40AdobeOrg=-408604571%7CMCMID%7C06676497119578019050460439108869431333%7CMCAAMLH-1606152151%7C6%7CMCAAMB-16061521" +
//                        "51%7C6G1ynYcLPuiQxYZrsz_pkqfLG9yMXBpb2zX5dvJdYQJzPXImdj0y%7CMCCIDH%7C1739545927%7CMCOPTOUT-1605554551s%7CNONE%7CMCSYNCS%7C81" +
//                        "841-18597%7CMCSYNCSOP%7C411-18590%7CvVersion%7C4.6.0; AMCVS_A71B5B5B54F607AB0A4C98A2%40AdobeOrg=1")
                .build();
        client.newCall(request).enqueue(checkLoginCallbackApi);
    }
    public void checkIfLoggedIn(String cookieHeaderValue) {
        Log.d("cookies", "Check for login with Cookie: " + cookieHeaderValue);
        Request request = new Request.Builder()
//                .url("https://www.ebay.com/sh/research/")
                .url("https://www.ebay.com/myb/Summary")
                .headers(basicHeaders)
                .header("Cookie", cookieHeaderValue)
                .build();
        client.newCall(request).enqueue(checkLoginCallbackApi);
    }

    private final Callback checkLoginCallbackApi = new Callback() {

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try (ResponseBody body = response.body()) {
                String bodyContent = body.string();
                Log.d("cookies", bodyContent);
                JsonObject root = new Gson().fromJson(bodyContent.split("\\n")[0], JsonObject.class);
                if (root.getAsJsonObject().get("error") != null) {
                    Log.d("cookies", "ERROR found in JSON");
                    loginStatusListener.onStatusReceived(false);
                } else if (root.getAsJsonObject().get("searchResultsTitle") != null) {
                    Log.d("cookies", "searchResultsTitle found in JSON");
                    loginStatusListener.onStatusReceived(true);
                } else {
                    Log.d("cookies", "Nothing found in JSON :(");
                    loginStatusListener.onStatusReceived(false);
                }
            } catch (Exception e) {
                Log.e("cookies", "Failed to process response: ", e);
                loginStatusListener.onStatusReceived(false);
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            loginStatusListener.onStatusReceived(false);
        }
    };

    private final Callback checkLoginCallback = new Callback() {
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try (ResponseBody body = response.peekBody(Long.MAX_VALUE)){
                String bodyContent = body.string();
                if (response.priorResponse() != null)
                    Log.d("cookies", "Prior URL: " + response.priorResponse().request().url().toString());
                Log.d("cookies", "URL: " + response.request().url());
                Log.d("cookies", bodyContent);
                boolean loggedIn = (response.priorResponse() == null && response.code() == 200)
                        || (response.priorResponse().code() == 200);
                loginStatusListener.onStatusReceived(loggedIn);
                Log.d("cookies", loggedIn ? "LOGGED IN" : "LOGIN FAILED");
            } catch (Exception e) {
                loginStatusListener.onStatusReceived(false);
            }
        }
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            loginStatusListener.onStatusReceived(false);
        }
    };



    public void login(String login, String password) {
            try {
                Request request = new Request.Builder()
                        .url("https://signin.ebay.com/ws/eBayISAPI.dll?SignIn&ru=")
                        .headers(basicHeaders)
                        .build();
                Response response = client.newCall(request).execute();
                try (ResponseBody body = response.peekBody(Long.MAX_VALUE)) {
                    String bodyContent = body.string();
                    Log.d("cookies", bodyContent);
                    Document doc = Jsoup.parse(bodyContent);
                    Elements hiddenInputs = doc.select("input[type=hidden]");
                    System.out.println(hiddenInputs.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
                loginStatusListener.onStatusReceived(false);
            }
    }



    private final Headers basicHeaders = new Headers.Builder()
            .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0")
            .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .add("Connection", "keep-alive")
            .add("Accept-Language", "en-US")
            .build();


    public interface LoginStatusListener {
        void onStatusReceived(boolean loggedIn);
    }

}
