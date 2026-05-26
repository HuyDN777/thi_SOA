/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soanetbean;

/**
 *
 * @author Admin
 */
import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class _3hg8X12J {

    // =========================================================
    // 1. BO KHUON HUNG DU LIEU GET TỪ SERVER
    // =========================================================
    static class TaskData {
        String id;
        String title;
        String status;
    }

    static class ServerResponse {
        String requestId;
        TaskData data; 
    }

    // =========================================================
    // 2. BO KHUON NẶN DU LIEU PUT LEN SERVER
    // =========================================================
    static class AnswerObj {
        String status;
        
        public AnswerObj(String status) { 
            this.status = status; 
        }
    }

    static class SubmitBody {
        String studentCode;
        String qCode;
        // Bai nay khong can requestId trong body nua vi no da nam o URL roi
        AnswerObj answer; 

        public SubmitBody(String s, String q, AnswerObj a) {
            this.studentCode = s;
            this.qCode = q;
            this.answer = a;
        }
    }

    public static void main(String[] args) throws Exception {
        String examIp = "36.50.135.242";
        String studentCode = "B22DCCN378";
        String qCode = "3hg8X12J";
        
        String base = "http://" + examIp + ":2230/api/rest/method"; 

        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        // ---------------------------------------------------------
        // BUOC 1: GET DU LIEU
        // ---------------------------------------------------------
        String urlGet = base + "?studentCode=" + studentCode + "&qCode=" + qCode;
        String res = client.send(HttpRequest.newBuilder().uri(URI.create(urlGet)).GET().build(),
                HttpResponse.BodyHandlers.ofString()).body();
        System.out.println("Nhan du lieu tu Server: " + res);

        // ---------------------------------------------------------
        // BUOC 2: BOC JSON
        // ---------------------------------------------------------
        ServerResponse responseObj = gson.fromJson(res, ServerResponse.class);
        String reqId = responseObj.requestId;

        // ---------------------------------------------------------
        // BUOC 3: NẶN JSON VA GỬI PUT NOP BAI
        // ---------------------------------------------------------
        // De bai bat buoc tao object answer co status = "done"
        AnswerObj ans = new AnswerObj("done");
        SubmitBody submitBody = new SubmitBody(studentCode, qCode, ans);
        
        String jsonBody = gson.toJson(submitBody);
        System.out.println("Chuoi JSON chuan bi nop: " + jsonBody);

        // LUY Y ĐẶC BIỆT CỦA BÀI NÀY NẰM Ở 2 DÒNG DƯỚI ĐÂY:
        // 1. URL nối thêm reqId: /api/rest/method/{requestId}
        String urlPut = base + "/" + reqId; 
        
        // 2. Dùng phương thức PUT() thay vì POST()
        String result = client.send(HttpRequest.newBuilder().uri(URI.create(urlPut))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody)).build(), 
                HttpResponse.BodyHandlers.ofString()).body();

        System.out.println("Ket qua tu Server: " + result);
    }
}
