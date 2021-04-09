package userservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import userservice.client.StocksClient;
import userservice.client.StocksClientImpl;
import userservice.model.User;
import userservice.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final StocksClient stocksClient = new StocksClientImpl("localhost", 8080);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public long addUser(User user) {
        return userRepository.save(user).getId();
    }

    public void addBalance(long userId, double amount) {
        final var user = userRepository.getOne(userId);
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
    }

    public double getBalance(long userId) {
        return userRepository.getOne(userId).getBalance();
    }

    public boolean buyStocks(long userId, long companyId, long amount) {
        final var price = stocksClient.getPrice(companyId);
        final var cost = price * amount;
        final var user = userRepository.getOne(userId);
        if (cost > user.getBalance()) {
            return false;
        }
        if (stocksClient.buyStocks(companyId, amount)) {
            user.setBalance(user.getBalance() - cost);
            user.getStocks().compute(companyId, (key, value) -> Optional.ofNullable(value).orElse(0L) + amount);
        }
        userRepository.save(user);
        return true;
    }

    public boolean sellStocks(long userId, long companyId, long amount) {
        final var user = userRepository.getOne(userId);
        final var stocks = user.getStocks();
        if (stocks.getOrDefault(companyId, 0L) > amount) {
            return false;
        }
        final var price = stocksClient.getPrice(companyId);
        final var cost = price * amount;
        if (stocksClient.sellStocks(companyId, amount)) {
            user.setBalance(user.getBalance() + cost);
            stocks.put(companyId, stocks.get(companyId) - amount);
        }
        userRepository.save(user);
        return true;
    }

    public long getStocksAmount(long userId, long companyId) {
        final var user = userRepository.getOne(userId);
        final var stocks = user.getStocks();
        return stocks.getOrDefault(companyId, 0L);
    }
}
