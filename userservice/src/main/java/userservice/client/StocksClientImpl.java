package userservice.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class StocksClientImpl implements StocksClient {
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();

    private final String host;
    private final int port;

    public StocksClientImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Long addCompany(String name) {
        final var uri = String.format("%s:%s/add-company?company-name=%s", host, port, name);
        try {
            final var request = HttpRequest.newBuilder().uri(new URI(uri)).GET().build();
            final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return Long.parseLong(response.body());
            }
        } catch (final URISyntaxException | InterruptedException | IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addStocks(long companyId, long amount) {
        final var uri = String.format("%s:%d/add-stocks?company-id=%d&amount=%d", host, port, companyId, amount);
        return sendRequest(uri);
    }

    @Override
    public boolean setPrice(long companyId, double price) {
        final var uri = String.format("%s:%d/set-stocks-price?company-id=%d&price=%f", host, port, companyId, price);
        return sendRequest(uri);
    }

    public Double getPrice(long companyId) {
        final var uri = String.format("%s:%s/get-stocks-price?company-id=%d", host, port, companyId);
        try {
            final var request = HttpRequest.newBuilder().uri(new URI(uri)).GET().build();
            final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return Double.parseDouble(response.body());
            }
        } catch (final URISyntaxException | InterruptedException | IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean buyStocks(long companyId, long amount) {
        final var uri = String.format("%s:%d/buy-stocks?company-id=%d&amount=%d", host, port, companyId, amount);
        return sendRequest(uri);
    }

    public boolean sellStocks(long companyId, long amount) {
        final var uri = String.format("%s:%d/sell-stocks?company-id=%d&amount=%d", host, port, companyId, amount);
        return sendRequest(uri);
    }

    private boolean sendRequest(String uri) {
        try {
            final var request = HttpRequest.newBuilder().uri(new URI(uri)).GET().build();
            final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (final URISyntaxException | InterruptedException | IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
