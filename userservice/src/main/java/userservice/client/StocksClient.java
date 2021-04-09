package userservice.client;

public interface StocksClient {
    Long addCompany(String name);
    boolean addStocks(long companyId, long amount);
    boolean setPrice(long companyId, double price);
    Double getPrice(long companyId);
    boolean buyStocks(long companyId, long amount);
    boolean sellStocks(long companyId, long amount);
}
