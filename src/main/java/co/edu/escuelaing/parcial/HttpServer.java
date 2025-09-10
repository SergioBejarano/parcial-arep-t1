package co.edu.escuelaing.parcial;

import java.net.*;
import java.io.*;
import java.lang.Math;
import java.util.LinkedList;
/**
 *
 * @author sergio.bejarano-r
 */
public class HttpServer {


    
    private static LinkedList<Number> conjunto = new LinkedList();
      
    public static void main(String[] args) throws IOException {
        conjunto.add(45.8);
        conjunto.add(7.5);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 36000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        try {
            System.out.println("Listo para recibir ...");
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        PrintWriter out = new PrintWriter(
                clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        String request = null;
        boolean isFirstLine = true;
        while ((inputLine = in.readLine()) != null) {
            
            if (isFirstLine){
                request = inputLine;
                isFirstLine = false;
            }
            if (!in.ready()) {
                break;
            }
        }
        outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Title of the document</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Mi propio mensaje</h1>\n"
                + "</body>\n"
                + "</html>\n";
        
        
        String path = request.split(" ")[1];
        System.out.print(path);        
        
        if (path.startsWith("/add")){            
            String x = path.split("=")[1];
           
            outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + x;
        } else if(path.startsWith("/list")){
            outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + listConjunto();
        }else if(path.startsWith("/clear")){
            
            outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + clearConjunto();
        }else if(path.startsWith("/stats")){
            outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "mean: " + statMean() 
                + "\r\n"
                + "stddev: " + statstddev();
        }
        
        
        out.println(outputLine);
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
    
    
    private static String addConjunto(String x){
        Long value = new Long(x);
        conjunto.add(value);
        return String.valueOf(value);
    }
    
    private static String listConjunto(){
        return conjunto.toString();
    }
    
    
    private static String clearConjunto(){
        conjunto.clear();
        return "list_cleared";
        
    } 
    
    private static double statMean(){
        Number counti = 0;
        float count = counti.floatValue();
        for(int i=0; i < conjunto.size(); i++){
            float value = conjunto.get(i).floatValue();
            count += value;    
        }     
        int size = conjunto.size();
        return (count/size);
        
    } 
    
    private static String statstddev(){
        double media = statMean();
        int n = conjunto.size();
        float var = 1/(n-1);
        float suma = 0;
        for(int i=0; i < conjunto.size(); i++){
            float value = conjunto.get(i).floatValue();
            suma += Math.pow(value-media, 2);  
        } 
        double result = Math.sqrt(var*suma);
        return String.valueOf(result);
        
    } 
    
    
}
