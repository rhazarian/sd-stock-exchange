package stockexchange.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import stockexchange.model.Company;
import stockexchange.service.StocksService;

@Controller
public class StocksController {
    private final StocksService service;

    public StocksController(StocksService service) {
        this.service = service;
    }

    @RequestMapping(value = "/add-company")
    public ResponseEntity<Long> addCompany(@RequestParam("company-name") String name) {
        final var company = new Company();
        company.setName(name);
        return ResponseEntity.ok(service.addCompany(company));
    }

    @RequestMapping(value = "/add-stocks")
    public ResponseEntity<Boolean> addStocks(@RequestParam("company-id") long companyId, @RequestParam("amount") long amount) {
        service.addStocksAmount(companyId, amount);
        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = "/get-stocks-amount")
    public ResponseEntity<Long> getStockCount(@RequestParam("company-id") long companyId) {
        return ResponseEntity.ok(service.getStocksAmount(companyId));
    }

    @RequestMapping(value = "/set-stocks-price")
    public ResponseEntity<Boolean> setStocksPrice(@RequestParam("company-id") long companyId, @RequestParam("price") double price) {
        service.setStocksPrice(companyId, price);
        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = "/get-stocks-price")
    public ResponseEntity<Double> getStocksPrice(@RequestParam("company-id") long companyId) {
        return ResponseEntity.ok(service.getStocksPrice(companyId));
    }

    @RequestMapping(value = "/buy-stocks")
    public ResponseEntity<Boolean> buyStocks(@RequestParam("company-id") long companyId, @RequestParam("amount") long amount) {
        try {
            service.buyStocks(companyId, amount);
            return ResponseEntity.ok(true);
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @RequestMapping(value = "/sell-stocks")
    public ResponseEntity<Boolean> sellStocks(@RequestParam("company-id") long companyId, @RequestParam("amount") long amount) {
        try {
            service.sellStocks(companyId, amount);
            return ResponseEntity.ok(true);
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
