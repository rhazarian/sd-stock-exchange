package stockexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import stockexchange.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> { }
