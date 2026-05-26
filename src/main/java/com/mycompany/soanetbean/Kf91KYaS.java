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

public class Kf91KYaS {

    // =========================================================
    // 1. BO KHUON HUNG DU LIEU GET TỪ SERVER
    // =========================================================
    static class Product {
        String name;
        double price;
        double taxRate;
        double discount;
    }

    static class ServerResponse {
        String requestId;
        Product data; // Data bay gio la 1 Object, khong phai mang []
    }

    // =========================================================
    // 2. BO KHUON NẶN DU LIEU POST LEN SERVER
    // =========================================================
    static class AnswerObj {
        double finalPrice;
        
        public AnswerObj(double f) { 
            this.finalPrice = f; 
        }
    }

    static class SubmitBody {
        String studentCode;
        String qCode;
        String requestId;
        AnswerObj answer; // Answer bay gio cung la 1 Object

        public SubmitBody(String s, String q, String r, AnswerObj a) {
            this.studentCode = s;
            this.qCode = q;
            this.requestId = r;
            this.answer = a;
        }
    }

    public static void main(String[] args) throws Exception {
        String examIp = "36.50.135.242";
        String studentCode = "B22DCCN378";
        String qCode = "Kf91KYaS";
        
        // Chu y: URL de bai nay doi thanh /object roi nhe
        String base = "http://" + examIp + ":2230/api/rest/object"; 

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
        // BUOC 2: BOC JSON VA TINH TOAN
        // ---------------------------------------------------------
        ServerResponse responseObj = gson.fromJson(res, ServerResponse.class);
        Product p = responseObj.data;

        // Ap dung dung cong thuc cua de bai: price * (1 + taxRate / 100) * (1 - discount / 100)
        double rawPrice = p.price * (1 + p.taxRate / 100.0) * (1 - p.discount / 100.0);
        
        // Lam tron 2 chu so thap phan
        double finalPrice = Math.round(rawPrice * 100.0) / 100.0;
        System.out.println("Gia cuoi cung sau khi lam tron: " + finalPrice);

        // ---------------------------------------------------------
        // BUOC 3: NẶN JSON VA POST NOP BAI
        // ---------------------------------------------------------
        AnswerObj ans = new AnswerObj(finalPrice);
        SubmitBody submitBody = new SubmitBody(studentCode, qCode, responseObj.requestId, ans);
        
        String jsonBody = gson.toJson(submitBody);
        System.out.println("Chuoi JSON chuan bi nop: " + jsonBody);

        String result = client.send(HttpRequest.newBuilder().uri(URI.create(base + "/submit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build(),
                HttpResponse.BodyHandlers.ofString()).body();

        System.out.println("Ket qua tu Server: " + result);
    }
}
