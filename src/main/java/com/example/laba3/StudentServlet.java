package com.example.laba3;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@WebServlet(name = "studentServlet", value="/student")
public class StudentServlet extends HttpServlet {
    private static final String FILE_PATH = "C:\\Users\\gstit\\IdeaProjects\\laba3_JSON\\src\\main\\java\\com\\example\\laba3\\student.json";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String studentsJson = readStudentsFromFile();
        response.getWriter().write(studentsJson);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder jsonRequest = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonRequest.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ошибка при чтении запроса");
            return;
        }

        // Преобразование JSON-строки в JSONObject
        JSONObject newStudentJson = new JSONObject(jsonRequest.toString());

        // Добавление нового студента в список
        JSONArray studentsJsonArray = new JSONArray(readStudentsFromFile());
        studentsJsonArray.put(newStudentJson);

        // Запись обновленного списка студентов в файл
        writeStudentsToFile(studentsJsonArray.toString());

        // Отправка обновленного списка студентов
        response.getWriter().write(studentsJsonArray.toString());
    }

    private String readStudentsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    private void writeStudentsToFile(String studentsJson) {
        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            fileWriter.write(studentsJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
