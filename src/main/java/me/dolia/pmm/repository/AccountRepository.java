package me.dolia.pmm.repository;

import java.util.List;
import me.dolia.pmm.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Jpa repository to performs database operations with {@code Account} entity.
 *
 * @author Maksym Dolia
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {

  /**
   * Gets the total balance of all user's accounts.
   *
   * @param email user's email
   * @return total balance
   */
  @Query("select sum(amount) from Account a where a.user.email = :email")
  Double getSumAmountByUserEmail(@Param("email") String email);

  /**
   * Populates all user's accounts.
   *
   * @param email user's email
   * @return list of accounts
   */
  List<Account> findAllByUserEmail(String email);

}
