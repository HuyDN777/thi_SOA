///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.mycompany.soanetbean;
//
///**
// *
// * @author Admin
// */
//
//import io.grpc.*;
//import GRPC.*;
//import java.util.*;
//
//public class b6LMbQl7 {
//    public static void main(String[] args) {
//        String examIp = "36.50.135.242", studentCode = "B22DCCN378", qCode = "b6LMbQl7";
//        int port = 2240;
//        ManagedChannel channel = ManagedChannelBuilder.forAddress(examIp, port).usePlaintext().build();
//        try {
//            TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub = TypedJudgeServiceGrpc.newBlockingStub(channel);
//            TypedJudgeRequest requestParams = TypedJudgeRequest.newBuilder()
//                    .setStudentCode(studentCode)
//                    .setQuestionAlias(qCode)
//                    .build();
//            System.out.println("Dang xin du lieu giao dich...");
//            TypedJudgeResponse response = stub.requestTyped(requestParams); 
//
//            // =========================================================
//            // BƯỚC B & C: MOI DỮ LIỆU TỪ "ONEOF" VÀ TÍNH TOÁN
//            // =========================================================
//            String reqId = response.getRequestId();
//            
//            // Vì thầy dùng "oneof", ông phải gọi đúng hàm lấy TransactionRiskBatch ra
//            TransactionRiskBatchData batchData = response.getTransactionRiskBatch();
//            List<TransactionRecord> txList = batchData.getTransactionsList(); 
//            
//            for (TransactionRecord tx : txList) {
//                System.out.println(tx);
//            }
//
//            List<String> highRiskIds = new ArrayList<>();
//            double totalHighRiskAmount = 0;
//
//            // Duyệt danh sách kiểm tra rủi ro
//            for (TransactionRecord tx : txList) {
//                double amount = tx.getAmount();
//                int chargeback = tx.getChargebackCount();
//                boolean isNewDevice = tx.getNewDevice();
//                String country = tx.getCountry();
//
//                // amount >= 5000 hoặc chargeback >= 2 hoặc (new_device=true và country khác VN)
//                if (amount >= 5000 
//                        || chargeback >= 2 
//                        || (isNewDevice && !"VN".equals(country))) {
//                    
//                    highRiskIds.add(tx.getTransactionId());
//                    totalHighRiskAmount += amount;
//                }
//            }
//
//            System.out.println("So giao dich rui ro: " + highRiskIds.size());
//
//            // Làm tròn tổng tiền 2 chữ số thập phân
//            double roundedAmount = Math.round(totalHighRiskAmount * 100.0) / 100.0;
//
//            // =========================================================
//            // BƯỚC D: ĐÓNG GÓI KẾT QUẢ VÀ SUBMIT
//            // =========================================================
//            // 1. Đóng gói cái ruột Answer trước
//            TransactionRiskAnswer riskAnswer = TransactionRiskAnswer.newBuilder()
//                    .addAllHighRiskTransactionIds(highRiskIds)
//                    .setReviewCount(highRiskIds.size())
//                    .setTotalHighRiskAmount(roundedAmount)
//                    .build();
//
//            // 2. Nhét ruột Answer vào gói Submit Request to
//            TypedSubmitRequest submitParams = TypedSubmitRequest.newBuilder()
//                    .setStudentCode(studentCode)
//                    .setQuestionAlias(qCode)
//                    .setRequestId(reqId)
//                    .setTransactionRiskAnswer(riskAnswer) // Gọi đúng lỗ oneof của thầy
//                    .build();
//
//            System.out.println("Dang nop bai...");
//            TypedSubmitResponse finalResult = stub.submitTyped(submitParams);
//
//            System.out.println("==> TRANG THAI: " + finalResult.getStatus()); 
//            System.out.println("==> TIN NHAN: " + finalResult.getMessage());
//        } catch (Exception e) {
//        } finally {
//        }
//    }
//}
