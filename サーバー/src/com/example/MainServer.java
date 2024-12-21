package com.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.services.UserService;
import com.utils.Logger;
import com.utils.Validator;
import com.utils.DataManager;

public class MainServer {

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return result;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1] : "";
            result.put(key, value);
        }
        return result;
    }


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ポート: ");
        int port = scanner.nextInt();
        scanner.close();
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/register", new RegisterHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/add_memo", new AddMemoHandler());
        server.createContext("/get_memos", new GetMemosHandler());

        server.setExecutor(null);

        server.start();
        System.out.println("サーバー開始");
    }

    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();

            String query = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = parseQuery(query);
            
            String UserName = queryParams.get("username");
            String Password = queryParams.get("password");

            String response;
            int StatusCode;

            if(UserService.RegisterUser(UserName,Password)){ //成功したらtrue,失敗したらfalseを返すため。
                response = "登録しました。";
                StatusCode = 200;
                Logger.Log("LOG","User Succeed To Register",clientIp);
            }else{
                response = "ユーザーネームかパスワードが不正です。";
                StatusCode = 400;
                Logger.Log("LOG","User Failed To Register",clientIp);
            }
            
            exchange.sendResponseHeaders(StatusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            } catch(IOException e){
                Logger.Log("ERROR", e.getMessage(), "None");
            }
        
    }
}

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();

            String query = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = parseQuery(query);
            
            String UserName = queryParams.get("username");
            String Password = queryParams.get("password");

            String response;
            int StatusCode;

            if(Validator.IsValidateLogin(UserName, Password)){
                response = "ログインしました。";
                StatusCode = 200;
                Logger.Log("LOG","User Succeed To Login.",clientIp);
            }else{
                response = "ユーザーネームもしくはパスワードが異なります。";
                StatusCode = 429;
                Logger.Log("LOG","User Failed To Login.",clientIp);
            }
            
            exchange.sendResponseHeaders(StatusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            } catch(IOException e){
                Logger.Log("ERROR", e.getMessage(), "None");
            }
        
    }
    }

    static class AddMemoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
            
            String response;
            int StatusCode;

            String query = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = parseQuery(query);

            String UserName = queryParams.get("username");
            String Password = queryParams.get("password");
            String Memo = queryParams.get("memo");

            if (Validator.IsValidateLogin(UserName,Password)){
                Logger.Log("LOG","User Succeed To Login.",clientIp);
                if (Validator.IsValidateMemo(Memo)){//通常のケース

                    DataManager.SaveUserMemos(UserName, Memo);
                    response = "メモを保存しました。";
                    StatusCode = 200;
                    Logger.Log("LOG","User Succeed To Save Memo.",clientIp);

                }else{//メモが4000文字を超えた場合
                    response = "メモが長すぎます！4000文字以下です。";
                    StatusCode = 400;
                    Logger.Log("LOG","Memo Is Too Long.",clientIp);
                }

            }else{ //ログイン失敗　ユーザーネームかパスワード
                response = "ユーザーネームもしくはパスワードが異なります。";
                StatusCode = 429;
                Logger.Log("LOG","User Failed To LogIn.",clientIp);
            }
            

            exchange.sendResponseHeaders(StatusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            } catch(IOException e){
                Logger.Log("ERROR", e.getMessage(), "None");
            }
        }
    }
    static class GetMemosHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
            
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = parseQuery(query);

            String response;
            int StatusCode;

            String UserName = queryParams.get("username");
            String Password = queryParams.get("password");
            String Memos;//memo1&memo2...

            if(Validator.IsValidateLogin(UserName,Password)){
                Logger.Log("LOG","User Succeed To Login.",clientIp);
                Memos = DataManager.GetUserMemos(UserName);
                if (Memos != null){
                    response = Memos;
                    StatusCode = 200;
                    Logger.Log("LOG","User Succeed To Get Memos",clientIp);
                }else{
                    response = "まだメモはありません。";
                    StatusCode = 400;
                }
                
            }else{
                response = "ユーザーネームもしくはパスワードが違います。";
                StatusCode = 429;
                Logger.Log("LOG","User Failed To LogIn.",clientIp);
            }

            exchange.sendResponseHeaders(StatusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            } catch(IOException e){
                Logger.Log("ERROR", e.getMessage(), "None");
            }
        }
    }
    
}

