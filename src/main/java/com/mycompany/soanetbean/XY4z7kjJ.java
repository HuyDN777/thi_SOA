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
//import GRPC.*;
//import java.io.*;
//import java.util.*;
//import io.grpc.*;
//
//public class XY4z7kjJ {
//    public static void main(String[] args) {
//        String examIp = "36.50.135.242", qCode = "XY4z7kjJ", studentCode = "B22DCCN378";
//        int port = 2240;
//        ManagedChannel channel = ManagedChannelBuilder.forAddress(examIp, port).usePlaintext().build();
//        try {
//            TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub = TypedJudgeServiceGrpc.newBlockingStub(channel);
//            TypedJudgeRequest requestParams = TypedJudgeRequest.newBuilder().setStudentCode(studentCode).setQuestionAlias(qCode).build();
//            TypedJudgeResponse response = stub.requestTyped(requestParams);
//            String requestId = response.getRequestId();
//            SensorTelemetryData data = response.getSensorTelemetry();
//            double threshold = data.getThreshold();
//            List<SensorReading> readings = data.getReadingsList();
//            int n = readings.size(), anomalyCnt = 0;
//            double sum = 0;
//            List<Double> allVals = new ArrayList<>();
//            for (SensorReading reading : readings) {   
//                sum += (double) reading.getValue();
//                if ((double) reading.getValue() > threshold) anomalyCnt++;
//                allVals.add(reading.getValue());
//            }
//            Collections.sort(allVals);
//            int p95idx = (int) Math.ceil(n * 0.95) - 1;
//            double p95 = allVals.get(p95idx);
//            double avg = Math.round((sum / n) * 100.0) / 100.0;
//            double roundedP95 = Math.round(p95 * 100.0) / 100.0;
//            SensorTelemetryAnswer sensorTelemetryAnswer = SensorTelemetryAnswer.newBuilder()
//                    .setAverage(avg).setP95(roundedP95)
//                    .setAnomalyCount(anomalyCnt)
//                    .build();
//            TypedSubmitRequest submitRequest = TypedSubmitRequest.newBuilder()
//                    .setStudentCode(studentCode)
//                    .setQuestionAlias(qCode)
//                    .setRequestId(requestId)
//                    .setSensorTelemetryAnswer(sensorTelemetryAnswer)
//                    .build();
//            TypedSubmitResponse submitResponse = stub.submitTyped(submitRequest);
//        } catch (Exception e) {
//        } finally {
//        }
//    }
//}
