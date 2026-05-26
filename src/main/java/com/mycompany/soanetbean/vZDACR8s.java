/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soanetbean;
/**
 *
 * @author Admin
 */

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import GRPC.*;

public class vZDACR8s {
    
    public static void main(String[] args) {
        String examIp = "36.50.135.242";
        int port = 2240;
        String studentCode = "B22DCCN378"; 
        String qCode = "vZDACR8s"; 
        ManagedChannel channel = ManagedChannelBuilder.forAddress(examIp, port)
                .usePlaintext() // Bắt buộc phải có vì đề yêu cầu không TLS
                .build();

        try {
            System.out.println("Dang ket noi toi server...");
            JudgeServiceGrpc.JudgeServiceBlockingStub stub = JudgeServiceGrpc.newBlockingStub(channel);

            // =========================================================
            // BƯỚC A: ĐÓNG GÓI YÊU CẦU GET DỮ LIỆU
            // Dùng đúng khuôn JudgeRequest của thầy
            // =========================================================
            JudgeRequest requestParams = JudgeRequest.newBuilder()
                    .setStudentCode(studentCode)
                    .setQuestionAlias(qCode)
                    .build();

            System.out.println("Dang xin du lieu tu server...");
            JudgeResponse response = stub.request(requestParams); 

            // =========================================================
            // BƯỚC B & C: MOI DỮ LIỆU VÀ TÍNH TOÁN
            // =========================================================
            String reqId = response.getRequestId();
            String dataStr = response.getData(); // Ví dụ: "1,2,3,4"
            
            System.out.println("=> RequestId: " + reqId);
            System.out.println("=> Mang tho: " + dataStr);

            // Chặt chuỗi bằng dấu phẩy và tính tổng
            long totalSum = 0;
            String[] numbers = dataStr.split(",");
            for (String num : numbers) {
                totalSum += Long.parseLong(num.trim());
            }
            System.out.println("=> Tong tinh duoc (Answer): " + totalSum);

            // =========================================================
            // BƯỚC D: ĐÓNG GÓI KẾT QUẢ VÀ SUBMIT
            // Dùng khuôn SubmitRequest của thầy
            // =========================================================
            SubmitRequest submitParams = SubmitRequest.newBuilder()
                    .setStudentCode(studentCode)
                    .setQuestionAlias(qCode)
                    .setRequestId(reqId)
                    .setAnswer(String.valueOf(totalSum)) 
                    .build();

            System.out.println("Dang nop bai...");
            SubmitResponse finalResult = stub.submit(submitParams);

            // In ra trạng thái cuối cùng (thường là AC hoặc báo lỗi)
            System.out.println("==> TRANG THAI: " + finalResult.getStatus()); 
            System.out.println("==> TIN NHAN TU SERVER: " + finalResult.getMessage()); 

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // --- 4. DỌN DẸP ---
            System.out.println("Dong ket noi.");
            channel.shutdown();
        }
    }
}
