package stockexchange.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stockexchange.model.Company;
import stockexchange.repository.CompanyRepository;

@Service
@Transactional
public class StocksService {
    private final CompanyRepository companyRepository;

    public StocksService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public long addCompany(Company company) {
        return companyRepository.save(company).getId();
    }

    public void addStocksAmount(long companyId, long amount) {
        final var company = companyRepository.getOne(companyId);
        company.setStocksAmount(company.getStocksAmount() + amount);
        companyRepository.save(company);
    }

    public long getStocksAmount(long companyId) {
        return companyRepository.getOne(companyId).getStocksAmount();
    }

    public void setStocksPrice(long companyId, double price) {
        final var company = companyRepository.getOne(companyId);
        company.setStocksPrice(price);
        companyRepository.save(company);
    }

    public double getStocksPrice(long companyId) {
        return companyRepository.getOne(companyId).getStocksPrice();
    }

    public void buyStocks(long companyId, long amount) {
        final var company = companyRepository.getOne(companyId);
        if (company.getStocksAmount() < amount) {
            throw new IllegalArgumentException("insufficient stocks amount");
        }
        company.setStocksAmount(company.getStocksAmount() - amount);
        companyRepository.save(company);
    }

    public void sellStocks(long companyId, long amount) {
        final var company = companyRepository.getOne(companyId);
        company.setStocksAmount(company.getStocksAmount() + amount);
        companyRepository.save(company);
    }
}
