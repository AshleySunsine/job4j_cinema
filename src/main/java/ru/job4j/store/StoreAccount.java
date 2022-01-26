package ru.job4j.store;

import ru.job4j.model.Account;

import java.util.Collection;
import java.util.List;

public interface StoreAccount {
    int saveAccount(Account acc);
    void deleteAccount(int id);
    Account findAccountById(int id);
    Account findAccountByName(String name);
    List<Account> findAllAccount();
}
