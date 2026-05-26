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
import java.util.List;

public class TriLmmvd {

    // =========================================================
    // 1. TẠO CÁC KHUÔN ĐỂ HỨNG DỮ LIỆU TỪ SERVER TRẢ VỀ (GET)
    // =========================================================
    static class ServerResponse {
        String requestId;
        List<Transaction> data; // JSON là mảng [] nên dùng List hứng
    }

    static class Transaction {
        double amount;
        String status;
    }

    // =========================================================
    // 2. TẠO CÁC KHUÔN ĐỂ ĐÓNG GÓI DỮ LIỆU GỬI LÊN (POST)
    // =========================================================
    static class AnswerData {
        double capturedTotal;
        double refundedTotal;
        double netTotal;
        int failedCount;

        // Constructor để lát truyền số vào cho lẹ
        public AnswerData(double c, double r, double n, int f) {
            this.capturedTotal = c;
            this.refundedTotal = r;
            this.netTotal = n;
            this.failedCount = f;
        }
    }

    static class SubmitBody {
        String studentCode;
        String qCode;
        String requestId;
        AnswerData answer; // Lồng khuôn AnswerData vào bên trong

        public SubmitBody(String sCode, String qCode, String reqId, AnswerData ans) {
            this.studentCode = sCode;
            this.qCode = qCode;
            this.requestId = reqId;
            this.answer = ans;
        }
    }

    public static void main(String[] args) throws Exception {
        String examIp = "36.50.135.242";
        String studentCode = "B22DCCN378"; 
        String qCode = "TriLmmvd";
        String base = "http://" + examIp + ":2230/api/rest/data";
        
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson(); // Triệu hồi "thợ đúc" Gson

        // ---------------------------------------------------------
        // BƯỚC 1: LẤY JSON TỪ SERVER
        // ---------------------------------------------------------
        String params = "?studentCode=" + studentCode + "&qCode=" + qCode;
        String res = client.send(HttpRequest.newBuilder().uri(URI.create(base + params)).GET().build(), 
                HttpResponse.BodyHandlers.ofString()).body();
        System.out.println("Nhận: " + res);

        // ---------------------------------------------------------
        // BƯỚC 2: BÓC VỎ JSON (ĐỈNH CAO Ở ĐÂY)
        // ---------------------------------------------------------
        // Đổ chuỗi JSON (res) vào khuôn (ServerResponse.class)
        ServerResponse responseObj = gson.fromJson(res, ServerResponse.class);
        
        // ---------------------------------------------------------
        // BƯỚC 3: XỬ LÝ LOGIC (Cực kỳ nhàn và an toàn)
        // ---------------------------------------------------------
        double capturedTotal = 0;
        double refundedTotal = 0;
        int failedCount = 0;

        // Quên đống split() loằng ngoằng đi, giờ cứ chấm gọi biến thôi
        for (Transaction tx : responseObj.data) {
            if (tx.status == null) continue; // Phòng hờ JSON bị thiếu trường
            
            if (tx.status.equals("CAPTURED")) {
                capturedTotal += tx.amount;
            } else if (tx.status.equals("REFUNDED")) {
                refundedTotal += tx.amount;
            } else if (tx.status.equals("FAILED")) {
                failedCount++;
            }
        }
        double netTotal = capturedTotal - refundedTotal;

        // ---------------------------------------------------------
        // BƯỚC 4: ĐÓNG GÓI JSON GỬI ĐI (NẶN KHUÔN)
        // ---------------------------------------------------------
        // Nhét dữ liệu vào khuôn
        AnswerData ans = new AnswerData(capturedTotal, refundedTotal, netTotal, failedCount);
        SubmitBody submitBody = new SubmitBody(studentCode, qCode, responseObj.requestId, ans);
        
        // Nhờ Gson nấu chảy khuôn thành chuỗi JSON (KHÔNG CẦN CỘNG CHUỖI String.format NỮA)
        String jsonBody = gson.toJson(submitBody);
        System.out.println("Chuỗi JSON chuẩn bị gửi: " + jsonBody);

        // ---------------------------------------------------------
        // BƯỚC 5: GỬI LÊN SERVER
        // ---------------------------------------------------------
        String result = client.send(HttpRequest.newBuilder().uri(URI.create(base + "/submit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build(), 
                HttpResponse.BodyHandlers.ofString()).body();
        
        System.out.println("==> Ket qua tu Server: " + result);
    }
}