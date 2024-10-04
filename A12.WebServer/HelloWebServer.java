import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import java.io.OutputStream;

public class HelloWebServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000),0);
        HttpContext context = server.createContext("/");
        context.setHandler(exchange -> {
	    System.out.println("Server Received HTTP Request");
	    MapADT<String, String> map = new PlaceholderMap<String, String>();
	    String query = exchange.getRequestURI().getQuery();
	    String[] pairs = query.split("&");
	    for (String pair : pairs) {
	        String[] keyValue = pair.split("=");
		if (keyValue.length == 2) {
		    String key = keyValue[0];
		    String value = keyValue[1];
		    map.put(key, value);
		}
	    }
	    String response = "Hello " + map.get("name") + "! <br/> I hope you are having a great " + java.time.LocalDateTime.now();
	    exchange.sendResponseHeaders(200,response.length());
	    exchange.getResponseHeaders().add("Content-type", "text/html");
	    OutputStream os = exchange.getResponseBody();
	    os.write(response.getBytes());
	    os.close();
	 });
        server.start();
	System.out.println("Hello Web Server Running...");
    }

}
