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
//import GRPC.*;
//import io.grpc.*;
//import java.io.*;
//import java.util.*;
//
//public class HPqrwujF {
//    public static void main(String[] args) {
//        String examIP = "36.50.135.242", qCode = "HPqrwujF", studentCode = "B22DCCN378";
//        int port = 2240;
//        ManagedChannel channel = ManagedChannelBuilder.forAddress(examIP, port).usePlaintext().build();
//        try {
//            JudgeServiceGrpc.JudgeServiceBlockingStub stub = JudgeServiceGrpc.newBlockingStub(channel);
//            JudgeRequest request = JudgeRequest.newBuilder()
//                    .setStudentCode(studentCode)
//                    .setQuestionAlias(qCode)
//                    .build();
//            JudgeResponse response = stub.request(request);
//            String reqId = response.getRequestId();
//            String str = response.getData();
//            String[] words = str.split(",");
//            Arrays.sort(words);
//            String ans = String.join(",", words);
//            SubmitRequest submitRequest = SubmitRequest.newBuilder()
//                    .setStudentCode(studentCode)
//                    .setQuestionAlias(qCode)
//                    .setRequestId(reqId)
//                    .setAnswer(ans)
//                    .build();
//            SubmitResponse submitResponse = stub.submit(submitRequest);
//        } catch (Exception e) {
//        } finally {
//        }
//    }
//}
