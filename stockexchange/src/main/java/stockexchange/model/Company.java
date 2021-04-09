package stockexchange.model;

import javax.persistence.*;

@Entity
@Table(name="Companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "stocks_price", nullable = false)
    private Double stocksPrice = 0D;

    @Column(name = "stocks_amount", nullable = false)
    private Long stocksAmount = 0L;

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Double getStocksPrice() {
        return stocksPrice;
    }

    public void setStocksPrice(Double stocksPrice) {
        this.stocksPrice = stocksPrice;
    }

    public Long getStocksAmount() {
        return stocksAmount;
    }

    public void setStocksAmount(Long stocksAmount) {
        this.stocksAmount = stocksAmount;
    }
}
