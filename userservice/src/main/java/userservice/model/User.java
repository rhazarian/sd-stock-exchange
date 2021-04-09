package userservice.model;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name="Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "UsersCompaniesStocks", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")})
    @MapKeyColumn(name = "company_id")
    @Column(name = "amount")
    private Map<Long, Long> stocks;

    @Column(name = "balance", nullable = false)
    private Double balance = 0D;

    public Long getId() {
        return id;
    }

    public Map<Long, Long> getStocks() {
        return stocks;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
