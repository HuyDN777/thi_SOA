/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soanetbean;

/**
 *
 * @author Admin
 */
import GRPC.*;
import com.google.gson.Gson;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Locale;

public class wQjuUfs2 {
    // Tạo khuôn để bóc JSON
    static class Product {
        String name;
        double price;
        double taxRate;
        double discount;
    }

    public static void main(String[] args) {
        String examIp = "36.50.135.242";
        int port = 2240;
        String studentCode = "B22DCCN378"; 
        String qCode = "wQjuUfs2"; 

        ManagedChannel channel = ManagedChannelBuilder.forAddress(examIp, port)
                .usePlaintext()
                .build();

        try {
            // 1. Gọi Service cũ (JudgeService)
            JudgeServiceGrpc.JudgeServiceBlockingStub stub = JudgeServiceGrpc.newBlockingStub(channel);

            JudgeRequest requestParams = JudgeRequest.newBuilder()
                    .setStudentCode(studentCode)
                    .setQuestionAlias(qCode)
                    .build();

            JudgeResponse response = stub.request(requestParams);
            String reqId = response.getRequestId();
            
            // 2. Lấy cục JSON từ Server
            String jsonStr = response.getData();
            System.out.println("JSON nhận được: " + jsonStr);

            // 3. Dùng Gson bóc JSON ra Object
            Gson gson = new Gson();
            Product p = gson.fromJson(jsonStr, Product.class);

            // 4. Tính toán theo đúng công thức đề cho
            double finalPrice = p.price * (1 + p.taxRate / 100) - p.discount;

            // Làm tròn 2 chữ số thập phân (Dùng Locale.US để ra dấu chấm thay vì dấu phẩy)
            String ans = String.format(Locale.US, "%.2f", finalPrice);
            System.out.println("Kết quả tính được: " + ans);

            // 5. Đóng gói Nộp bài
            SubmitRequest submitRequest = SubmitRequest.newBuilder()
                    .setStudentCode(studentCode)
                    .setQuestionAlias(qCode)
                    .setRequestId(reqId)
                    .setAnswer(ans) // Nhét chuỗi kết quả vào
                    .build();

            SubmitResponse submitResponse = stub.submit(submitRequest);
            System.out.println("Status: " + submitResponse.getStatus());
            System.out.println("Message: " + submitResponse.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdown();
        }
    }
}
